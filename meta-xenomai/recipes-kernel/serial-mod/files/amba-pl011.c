/*
 * Copyright (C) 2011 Wolfgang Grandegger <wg@denx.de>.
 * Copyright (C) 2005-2007 Jan Kiszka <jan.kiszka@web.de>.
 *
 * Xenomai is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * Xenomai is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Xenomai; if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */

#include <linux/version.h>
#include <linux/module.h>
#include <linux/of.h>
#include <linux/slab.h>
#include <linux/serial.h>
#include <linux/amba/bus.h>
#include <linux/amba/serial.h>
#include <linux/io.h>

#include <rtdm/serial.h>
#include <rtdm/driver.h>
#include <../drivers/tty/serial/amba-pl011.h>

typedef unsigned int __bitwise upstat_t;

#define UART_NR			14

#define SERIAL_AMBA_MAJOR	204
#define SERIAL_AMBA_MINOR	64
#define SERIAL_AMBA_NR		UART_NR

#define AMBA_ISR_PASS_LIMIT	256

#define UART_DR_ERROR (UART011_DR_OE | UART011_DR_BE | UART011_DR_PE | \
						UART011_DR_FE)
#define UART_DUMMY_DR_RX	(1 << 16)

#define IN_BUFFER_SIZE		4096
#define OUT_BUFFER_SIZE		4096

#define SERIAL_IO_MEM	2
#define SERIAL_IO_MEM32	  3
#define UPIO_MEM		(SERIAL_IO_MEM)		/* driver-specific */
#define UPIO_MEM32		(SERIAL_IO_MEM32)	/* 32b little endian */
#define UPSTAT_AUTORTS		((__force upstat_t) (1 << 2))
#define UPSTAT_AUTOCTS		((__force upstat_t) (1 << 3))

#define XENOMAI_PL011_UART_DEBUG

static u16 pl011_std_offsets[REG_ARRAY_SIZE] = {
	[REG_DR] = UART01x_DR,
	[REG_FR] = UART01x_FR,
	[REG_LCRH_RX] = UART011_LCRH,
	[REG_LCRH_TX] = UART011_LCRH,
	[REG_IBRD] = UART011_IBRD,
	[REG_FBRD] = UART011_FBRD,
	[REG_CR] = UART011_CR,
	[REG_IFLS] = UART011_IFLS,
	[REG_IMSC] = UART011_IMSC,
	[REG_RIS] = UART011_RIS,
	[REG_MIS] = UART011_MIS,
	[REG_ICR] = UART011_ICR,
	[REG_DMACR] = UART011_DMACR,
};

/* There is by now at least one vendor with differing details, so handle it */
struct vendor_data {
	const u16		*reg_offset;
	unsigned int		ifls;
	unsigned int		fr_busy;
	unsigned int		fr_dsr;
	unsigned int		fr_cts;
	unsigned int		fr_ri;
	unsigned int		inv_fr;
	bool			access_32b;
	bool			oversampling;
	bool			dma_threshold;
	bool			cts_event_workaround;
	bool			always_enabled;
	bool			fixed_options;

	unsigned int (*get_fifosize)(struct amba_device *dev);
};

static unsigned int get_fifosize_arm(struct amba_device *dev)
{
	return amba_rev(dev) < 3 ? 16 : 32;
}

static struct vendor_data vendor_arm = {
	.reg_offset		= pl011_std_offsets,
	.ifls			= UART011_IFLS_RX4_8|UART011_IFLS_TX4_8,
	.fr_busy		= UART01x_FR_BUSY,
	.fr_dsr			= UART01x_FR_DSR,
	.fr_cts			= UART01x_FR_CTS,
	.fr_ri			= UART011_FR_RI,
	.oversampling		= false,
	.dma_threshold		= false,
	.cts_event_workaround	= false,
	.always_enabled		= false,
	.fixed_options		= false,
	.get_fifosize		= get_fifosize_arm,
};

struct rt_uart_port {
	unsigned char __iomem	*membase;
	resource_size_t		mapbase;		/* for ioremap */
	resource_size_t		mapsize;
	unsigned int		irq;
	unsigned long		irqflags;		/* irq flags  */
	unsigned int		uartclk;		/* base uart clock */
	unsigned int		fifosize;		/* tx fifo size */
	unsigned char		iotype;			/* io access style */
	/*
	 * Must hold termios_rwsem, port mutex and port lock to change;
	 * can hold any one lock to read.
	 */
	upstat_t		status;
};

struct rt_uart_amba_port {
	struct rt_uart_port	port;
	const u16		*reg_offset;
	struct clk		*clk;
	const struct vendor_data *vendor;
	unsigned int		dmacr;		/* dma control reg */
	unsigned int		im;		/* interrupt mask */
	unsigned int		fifosize;	/* vendor-specific */
	unsigned int		old_cr;		/* state during shutdown */
};

struct rt_pl011_uart_ctx {
	struct rtser_config config;	/* current device configuration */

	rtdm_irq_t irq_handle;		/* device IRQ handle */
	rtdm_lock_t lock;		/* lock to protect context struct */

	int in_head;			/* RX ring buffer, head pointer */
	int in_tail;			/* RX ring buffer, tail pointer */
	size_t in_npend;		/* pending bytes in RX ring */
	int in_nwait;			/* bytes the user waits for */
	rtdm_event_t in_event;		/* raised to unblock reader */
	char in_buf[IN_BUFFER_SIZE];	/* RX ring buffer */
	volatile unsigned long in_lock;	/* single-reader lock */
	uint64_t *in_history;		/* RX timestamp buffer */

	int out_head;			/* TX ring buffer, head pointer */
	int out_tail;			/* TX ring buffer, tail pointer */
	size_t out_npend;		/* pending bytes in TX ring */
	rtdm_event_t out_event;		/* raised to unblock writer */
	char out_buf[OUT_BUFFER_SIZE];	/* TX ring buffer */
	rtdm_mutex_t out_lock;		/* single-writer mutex */

	uint64_t last_timestamp;	/* timestamp of last event */
	int ioc_events;			/* recorded events */
	rtdm_event_t ioc_event;		/* raised to unblock event waiter */
	volatile unsigned long ioc_event_lock;	/* single-waiter lock */
	struct rt_uart_amba_port *port; /* Port related data */
};

#define PARITY_MASK		0x03
#define DATA_BITS_MASK		0x03
#define STOP_BITS_MASK		0x01
#define FIFO_MASK		0xC0
#define EVENT_MASK		0x0F

static const struct rtser_config default_config = {
	.config_mask = 0xFFFF,
	/* xenomai规定串口波特率的默认值RTSER_DEF_BAUD 9600 */
	.baud_rate = RTSER_DEF_BAUD,
	/* 默认不使能奇偶校验模式 */
	.parity = RTSER_DEF_PARITY,
	/* 默认fifo的数据长度为8bit */
	.data_bits = RTSER_DEF_BITS,
	/* 默认只有1个停止位 */
	.stop_bits = RTSER_DEF_STOPB,
	.handshake = RTSER_DEF_HAND,
	.fifo_depth = RTSER_DEF_FIFO_DEPTH,
	.rx_timeout = RTSER_DEF_TIMEOUT,
	.tx_timeout = RTSER_DEF_TIMEOUT,
	.event_timeout = RTSER_DEF_TIMEOUT,
	.timestamp_history = RTSER_DEF_TIMESTAMP_HISTORY,
	.event_mask = RTSER_DEF_EVENT_MASK,
	/* 默认485不使能 */
	.rs485 = RTSER_DEF_RS485,
};

static unsigned int pl011_reg_to_offset(const struct rt_uart_amba_port *uap,
				unsigned int reg)
{
	return uap->reg_offset[reg];
}

static unsigned int pl011_read(const struct rt_uart_amba_port *uap,
		unsigned int reg)
{
	void __iomem *addr = uap->port.membase + pl011_reg_to_offset(uap, reg);

	return (uap->port.iotype == UPIO_MEM32) ?
		readl_relaxed(addr) : readw_relaxed(addr);
}

static void pl011_write(unsigned int val, 
		const struct rt_uart_amba_port *uap,
		unsigned int reg)
{
	void __iomem *addr = uap->port.membase + pl011_reg_to_offset(uap, reg);

	if (uap->port.iotype == UPIO_MEM32)
		writel_relaxed(val, addr);
	else
		writew_relaxed(val, addr);
}

static bool pl011_split_lcrh(const struct rt_uart_amba_port *uap)
{
	return pl011_reg_to_offset(uap, REG_LCRH_RX) !=
	       pl011_reg_to_offset(uap, REG_LCRH_TX);
}

static void pl011_write_lcr_h(struct rt_uart_amba_port *uap,
			unsigned int lcr_h)
{
	pl011_write(lcr_h, uap, REG_LCRH_RX);
	if (pl011_split_lcrh(uap)) {
		int i;
		/*
		 * Wait 10 PCLKs before writing LCRH_TX register,
		 * to get this delay write read only register 10 times
		 */
		for (i = 0; i < 10; ++i)
			pl011_write(0xff, uap, REG_MIS);
		pl011_write(lcr_h, uap, REG_LCRH_TX);
	}
}

/* DMA后续支持 */
/* Blank functions if the DMA engine is not available */
static inline void pl011_dma_probe(struct rt_uart_amba_port *uap)
{
}

static inline void pl011_dma_remove(struct rt_uart_amba_port *uap)
{
}

static inline void pl011_dma_startup(struct rt_uart_amba_port *uap)
{
}

static inline void pl011_dma_shutdown(struct rt_uart_amba_port *uap)
{
}

static inline bool pl011_dma_tx_irq(struct rt_uart_amba_port *uap)
{
	return false;
}

static inline void pl011_dma_tx_stop(struct rt_uart_amba_port *uap)
{
}

static inline bool pl011_dma_tx_start(struct rt_uart_amba_port *uap)
{
	return false;
}

static inline void pl011_dma_rx_irq(struct rt_uart_amba_port *uap)
{
}

static inline void pl011_dma_rx_stop(struct rt_uart_amba_port *uap)
{
}

static inline int pl011_dma_rx_trigger_dma(struct rt_uart_amba_port *uap)
{
	return -EIO;
}

static inline bool pl011_dma_rx_available(struct rt_uart_amba_port *uap)
{
	return false;
}

static inline bool pl011_dma_rx_running(struct rt_uart_amba_port *uap)
{
	return false;
}

#define pl011_dma_flush_buffer	NULL

/*
 * Enable interrupts, only timeouts when using DMA
 * if initial RX DMA job failed, start in interrupt mode
 * as well.
 */
static void pl011_enable_int(struct rt_pl011_uart_ctx *ctx)
{
	struct rt_uart_amba_port *uap = ctx->port;
	rtdm_lockctx_t lock_ctx;
	unsigned int i;

	rtdm_lock_get_irqsave(&ctx->lock, lock_ctx);

	/* Clear out any spuriously appearing RX interrupts */
	pl011_write(UART011_RTIS | UART011_RXIS, uap, REG_ICR);

	/*
	 * RXIS is asserted only when the RX FIFO transitions from below
	 * to above the trigger threshold.  If the RX FIFO is already
	 * full to the threshold this can't happen and RXIS will now be
	 * stuck off.  Drain the RX FIFO explicitly to fix this:
	 */
	for (i = 0; i < uap->fifosize * 2; ++i) {
		if (pl011_read(uap, REG_FR) & UART01x_FR_RXFE)
			break;

		pl011_read(uap, REG_DR);
	}
	/* 使能接收中断 */
	uap->im = UART011_RTIM;
	if (!pl011_dma_rx_running(uap))
		uap->im |= UART011_RXIM;
	pl011_write(uap->im, uap, REG_IMSC);
	rtdm_lock_put_irqrestore(&ctx->lock, lock_ctx);
}

static void pl011_disable_int(struct rt_pl011_uart_ctx *ctx)
{
	struct rt_uart_amba_port *uap = ctx->port;
	rtdm_lockctx_t lock_ctx;
	rtdm_lock_get_irqsave(&ctx->lock, lock_ctx);

	/* mask all interrupts and clear all pending ones */
	uap->im = 0;
	pl011_write(uap->im, uap, REG_IMSC);
	pl011_write(0xffff, uap, REG_ICR);

	rtdm_lock_put_irqrestore(&ctx->lock, lock_ctx);
}

static void pl011_shutdown_channel(struct rt_uart_amba_port *uap,
					unsigned int lcrh)
{
      unsigned long val;

      val = pl011_read(uap, lcrh);
      val &= ~(UART01x_LCRH_BRK | UART01x_LCRH_FEN);
      pl011_write(val, uap, lcrh);
}

static void pl011_disable_uart(struct rt_pl011_uart_ctx *ctx)
{
	struct rt_uart_amba_port *uap = ctx->port;
	unsigned int cr;
	rtdm_lockctx_t lock_ctx;

	uap->port.status &= ~(UPSTAT_AUTOCTS | UPSTAT_AUTORTS);
	rtdm_lock_get_irqsave(&ctx->lock, lock_ctx);
	cr = pl011_read(uap, REG_CR);
	uap->old_cr = cr;
	cr &= UART011_CR_RTS | UART011_CR_DTR;
	cr |= UART01x_CR_UARTEN | UART011_CR_TXE;
	pl011_write(cr, uap, REG_CR);
	rtdm_lock_put_irqrestore(&ctx->lock, lock_ctx);

	/*
	 * disable break condition and fifos
	 */
	pl011_shutdown_channel(uap, REG_LCRH_RX);
	if (pl011_split_lcrh(uap))
		pl011_shutdown_channel(uap, REG_LCRH_TX);
}

/* 
 * 创建一个干净的环境
 * 参考linux kernel社区如下函数
 * 	pl011_register_port
 */
static void pl011_hw_clear(struct rt_uart_amba_port *uap)
{
	unsigned int val;
	/* Disable the UART */
	pl011_write(0, uap, REG_CR);

	/* Ensure interrupts from this UART are masked and cleared */
	val = pl011_read(uap, REG_IMSC);
	/* 芯片手册要求bit15:11不要修改, 写0屏蔽所有中断 */
	val &= ~(UART011_OEIM | UART011_BEIM | UART011_PEIM |
			UART011_FEIM | UART011_RTIM | UART011_TXIM |
			UART011_RXIM | UART011_DSRMIM | UART011_DCDMIM |
			UART011_CTSMIM | UART011_RIMIM);
	pl011_write(val, uap, REG_IMSC);
	val = pl011_read(uap, REG_IMSC);
	printk("%s read after modify IMSC 0x%x\n", __FUNCTION__, val);

	/* clear existing interrupts, 写1清除所有当前中断 */
	/* 芯片手册要求bit15:11不要修改 */
	val = UART011_OEIS | UART011_BEIS | UART011_PEIS |
			UART011_FEIS | UART011_RTIS | UART011_TXIS |
			UART011_RXIS | UART011_DSRMIS | UART011_DCDMIS |
			UART011_CTSMIS | UART011_RIMIS;
	pl011_write(val, uap, REG_ICR);
	val = pl011_read(uap, REG_ICR);
	printk("%s read after modify ICR 0x%x\n", __FUNCTION__, val);
	return;
}

static int pl011_clock_init(struct rt_pl011_uart_ctx *ctx)
{
	struct rt_uart_amba_port *uap = ctx->port;
	/* Optionaly enable pins to be muxed in and configured */
	/*
	 * Try to enable the clock producer.
	 */
	int retval = clk_prepare_enable(uap->clk);
	if (retval)
		return retval;

	uap->port.uartclk = clk_get_rate(uap->clk);

	return 0;
}

/* 硬件寄存器初始化 */
static void pl011_hw_init(struct rt_pl011_uart_ctx *ctx)
{
	struct rt_uart_amba_port *uap = ctx->port;
	rtdm_lockctx_t lock_ctx;
	unsigned int cr, val;

	rtdm_lock_get_irqsave(&ctx->lock, lock_ctx);
	val = pl011_read(uap, REG_IFLS);
	printk("%s read IFLS 0x%x\n", __FUNCTION__, val);
	pl011_write(uap->vendor->ifls, uap, REG_IFLS);
	val = pl011_read(uap, REG_IFLS);
	printk("%s read after modify IFLS 0x%x\n", __FUNCTION__, val);

	cr = UART01x_CR_UARTEN | UART011_CR_RXE | UART011_CR_TXE;
	pl011_write(cr, uap, REG_CR);

	val = pl011_read(uap, REG_LCRH_RX);
	printk("%s read REG_LCRH_RX 0x%x\n", __FUNCTION__, val);
	/* 
	 * 使能fifo前要先写入1个字符
	 * 非fifo模式貌似不需要, 
	 * 而fifo模式需要, 否则tx无法触发第一次中断
	 */
	pl011_write(0, uap, REG_DR);
	/* 使能fifo, 一次接收和发送的数据长度为8 */
	pl011_write(UART01x_LCRH_FEN | UART01x_LCRH_WLEN_8, uap, REG_LCRH_RX);

	val = pl011_read(uap, REG_LCRH_RX);
	printk("%s read again REG_LCRH_RX 0x%x\n", __FUNCTION__, val);
	rtdm_lock_put_irqrestore(&ctx->lock, lock_ctx);

	/* Startup DMA */
	pl011_dma_startup(uap);
}

static inline int rt_pl011_uart_rx_int(struct rt_pl011_uart_ctx *ctx,
					uint64_t * timestamp)
{
	int rbytes = 0;
	int c;
	unsigned int status;
	struct rt_uart_amba_port *uap = ctx->port;
	while (1) {
		status = pl011_read(uap, REG_FR);
		if (status & UART01x_FR_RXFE) {
			printk("%s fifo empty\n", __FUNCTION__);
			break;
		}
		c = pl011_read(uap, REG_DR);	/* read input char */
		printk("%s get char %c\n", __FUNCTION__, (char)c);
		printk("%s get char %d\n", __FUNCTION__, (char)c);

		ctx->in_buf[ctx->in_tail] = (char)c;
		if (ctx->in_history)
			ctx->in_history[ctx->in_tail] = *timestamp;
		ctx->in_tail = (ctx->in_tail + 1) & (IN_BUFFER_SIZE - 1);

		if (++ctx->in_npend > IN_BUFFER_SIZE) {
			ctx->in_npend--;
		}

		rbytes++;
	};
	return rbytes;
}

static bool pl011_tx_char(struct rt_uart_amba_port *uap, unsigned int c,
			  bool from_irq)
{
	if (unlikely(!from_irq) &&
	    pl011_read(uap, REG_FR) & UART01x_FR_TXFF)
		return false; /* unable to transmit character */

	pl011_write(c, uap, REG_DR);
	return true;
}

/* 外层已经加锁 */
static void pl011_enable_tx_int(struct rt_pl011_uart_ctx *ctx)
{
	struct rt_uart_amba_port *uap = ctx->port;
	uap->im |= UART011_TXIM; /* 设置发送中断使能 */
	pl011_write(uap->im, uap, REG_IMSC);
	printk("%s read REG_IMSC 0x%x\n", __FUNCTION__, uap->im);
}
static void pl011_disable_tx_int(struct rt_pl011_uart_ctx *ctx)
{
	struct rt_uart_amba_port *uap = ctx->port;
	uap->im &= ~UART011_TXIM; /* 设置发送中断禁止 */
	pl011_write(uap->im, uap, REG_IMSC);
	printk("%s read REG_IMSC 0x%x\n", __FUNCTION__, uap->im);
}

static void rt_pl011_tx_fill(struct rt_pl011_uart_ctx *ctx, bool from_irq)
{
	struct rt_uart_amba_port *uap = ctx->port;
	int count = uap->fifosize >> 1;
	printk("%s fifosize %d, out_npend %d\n",
		__FUNCTION__, uap->fifosize, (unsigned int)ctx->out_npend);
	for (count = uap->fifosize >> 1; /* 非fifo模式 */
	     (count > 0) && (ctx->out_npend > 0);
	     count--, ctx->out_npend--) {		
		unsigned int c = ctx->out_buf[ctx->out_head];	
		bool ret = pl011_tx_char(uap, c, from_irq);
		printk("%s fifosize put char %c, left out_npend %d\n", 
				__FUNCTION__,
				ctx->out_buf[ctx->out_head],
				(int)ctx->out_npend);
		if (!ret) {	/* 不能再写了, fifo满了 */
			printk("%s fifo full\n", __FUNCTION__);
			break;
		}
		ctx->out_head++;
		ctx->out_head &= (OUT_BUFFER_SIZE - 1);
	}
}

/*
 * 不同芯片的串口寄存器定义不同,
 * 但是我们要保持config命令字统一, 
 * 所以需要对config命令字进行翻译转换
 * 目前只实现如下配置
 *	波特率
 *	fifo数据长度
 *	stop停止位长度
 * 该函数参考如下函数的实现
 * 	linux kernel社区 pl011_set_termios
 * 	xenomai社区 rt_16550_set_config
 * 	xenomai社区 rt_mpc52xx_uart_set_config
 */
static int rt_pl011_uart_set_config(struct rt_pl011_uart_ctx *ctx,
				      const struct rtser_config *config,
				      uint64_t **in_history_ptr)
{
	rtdm_lockctx_t lock_ctx;
	unsigned int lcr_h, baud, quot, uartclk, old_cr, ibrd, fbrd;

	/* make line configuration atomic and IRQ-safe */
	rtdm_lock_get_irqsave(&ctx->lock, lock_ctx);

	/* 第一步, 先读取寄存器, 获取当前值 */
	ibrd = pl011_read(ctx->port, REG_IBRD);
	printk("%s read current IBRD 0x%x\n", __FUNCTION__, ibrd);
	fbrd = pl011_read(ctx->port, REG_FBRD);
	printk("%s read current FBRD 0x%x\n", __FUNCTION__, fbrd);

	lcr_h = pl011_read(ctx->port, REG_LCRH_RX);
	printk("%s read current LCRH 0x%x\n", __FUNCTION__, lcr_h);

	old_cr = pl011_read(ctx->port, REG_CR);
	printk("%s read current CR 0x%x\n", __FUNCTION__, old_cr);
	
	/* 第二步, 先进行软件配置 */
	if (config->config_mask & RTSER_SET_BAUD) {
		ctx->config.baud_rate = config->baud_rate;
		uartclk = ctx->port->port.uartclk;
		/* 根据波特率计算REG_FBRD和REG_IBRD */
		baud = ctx->config.baud_rate;
		if (baud > (uartclk / 16))
			quot = DIV_ROUND_CLOSEST(uartclk * 8, baud);
		else
			quot = DIV_ROUND_CLOSEST(uartclk * 4, baud);
		fbrd = quot & 0x3f;
		ibrd = quot >> 6;
		printk("%s get new baud %d\n", __FUNCTION__, baud);
	}
	if (config->config_mask & RTSER_SET_PARITY) {
		ctx->config.parity = config->parity & PARITY_MASK;
		lcr_h &= 0xF9; /* 去掉UARTLCR_H的EPS和PEN, 即bit2:1 */
		/* 奇偶校验翻译 */
		switch (ctx->config.parity) {
		case RTSER_ODD_PARITY:
			lcr_h |= UART01x_LCRH_PEN;
			break;
		case RTSER_EVEN_PARITY:
			lcr_h |= UART01x_LCRH_PEN | UART01x_LCRH_EPS;
			break;
		case RTSER_NO_PARITY:
		default:
			break;
		}
	}
	if (config->config_mask & RTSER_SET_DATA_BITS) {
		ctx->config.data_bits = config->data_bits & DATA_BITS_MASK;
		lcr_h &= 0x9F; /* 去掉UARTLCR_H的WLEN, 即bit6:5 */
		/* 数据长度翻译 */
		switch (ctx->config.data_bits) {
		case RTSER_5_BITS:
			lcr_h |= UART01x_LCRH_WLEN_5;
			break;
		case RTSER_6_BITS:
			lcr_h |= UART01x_LCRH_WLEN_6;
			break;
		case RTSER_7_BITS:
			lcr_h |= UART01x_LCRH_WLEN_7;
			break;
		default: /* RTSER_DEF_BITS */
			lcr_h |= UART01x_LCRH_WLEN_8;
			break;
		}
	}
	if (config->config_mask & RTSER_SET_STOP_BITS) {
		ctx->config.stop_bits = config->stop_bits & STOP_BITS_MASK;
		lcr_h &= 0xF7; /* 去掉UARTLCR_H的STP2, 即bit3 */
		if (ctx->config.stop_bits == RTSER_2_STOPB)
			lcr_h |= UART01x_LCRH_STP2;	
	}
	if (config->config_mask & RTSER_SET_HANDSHAKE) {
		ctx->config.handshake = config->handshake;
		old_cr &= 0xCFFF; /* 去掉REG_CR的CTSEn和RTSEn, 即bit15:14 */
		if (ctx->config.handshake == RTSER_RTSCTS_HAND) {
			old_cr |= UART011_CR_CTSEN | UART011_CR_RTSEN;
		}
	}

	/* 第三步, 写寄存器进行硬件配置 */
	/* first, disable everything */		
	pl011_write(0, ctx->port, REG_CR);

	/* Set baud rate */
	pl011_write(fbrd, ctx->port, REG_FBRD);
	pl011_write(ibrd, ctx->port, REG_IBRD);
	/*
	 * ----------v----------v----------v----------v-----
	 * NOTE: REG_LCRH_TX and REG_LCRH_RX MUST BE WRITTEN AFTER
	 * REG_FBRD & REG_IBRD.
	 * ----------^----------^----------^----------^-----
	 */
	pl011_write_lcr_h(ctx->port, lcr_h);
	pl011_write(old_cr, ctx->port, REG_CR);

	ibrd = pl011_read(ctx->port, REG_IBRD);
	printk("%s read new IBRD 0x%x\n", __FUNCTION__, ibrd);
	fbrd = pl011_read(ctx->port, REG_FBRD);
	printk("%s read new FBRD 0x%x\n", __FUNCTION__, fbrd);

	lcr_h = pl011_read(ctx->port, REG_LCRH_RX);
	printk("%s read new LCRH 0x%x\n", __FUNCTION__, lcr_h);

	old_cr = pl011_read(ctx->port, REG_CR);
	printk("%s read new CR 0x%x\n", __FUNCTION__, old_cr);

	ctx->ioc_events &= ~RTSER_EVENT_ERRPEND;

	rtdm_lock_put_irqrestore(&ctx->lock, lock_ctx);
	return 0;
}

void rt_pl011_uart_cleanup_ctx(struct rt_pl011_uart_ctx *ctx)
{
	rtdm_event_destroy(&ctx->in_event);
	rtdm_event_destroy(&ctx->out_event);
	rtdm_event_destroy(&ctx->ioc_event);
	rtdm_mutex_destroy(&ctx->out_lock);
}

/* 中断处理, 目前只关心tx和rx中断 */
static int rt_pl011_uart_int(rtdm_irq_t * irq_context)
{
	struct rt_pl011_uart_ctx *ctx;
	struct rt_uart_amba_port *uap;
	unsigned int status;
	uint64_t timestamp = rtdm_clock_read();
	int rbytes = 0;
	int events = 0;
	int ret = RTDM_IRQ_NONE;

	ctx = rtdm_irq_get_arg(irq_context, struct rt_pl011_uart_ctx);
	uap = ctx->port;
	rtdm_lock_get(&ctx->lock);

	/* 读取中断状态寄存器 */
	status = pl011_read(uap, REG_RIS);
	printk("%s begin, get status 0x%x, im 0x%x\n",
			__FUNCTION__, status, uap->im);
	status = status & uap->im;
	if (status) {
		int val;
		do {		
			val = (status & 
				~(UART011_TXIS | UART011_RTIS | UART011_RXIS));
			/* 清除其它中断, 保留rx timeout, tx和rx */
			pl011_write(status, uap, REG_ICR);
			printk("%s val 0x%x, clear int\n", __FUNCTION__, val);
			/* 输入中断处理 */
			if (status & (UART011_RTIS | UART011_RXIS)) {
				if (pl011_dma_rx_running(uap))
					pl011_dma_rx_irq(uap);
				else {
					rbytes += rt_pl011_uart_rx_int(ctx, &timestamp);
					printk("%s get rbytes %d\n", __FUNCTION__, rbytes);
					events |= RTSER_EVENT_RXPEND;
				}
			}

			/* 输出中断处理 */
			if (status & UART011_TXIS) {
				printk("%s tx continue\n", __FUNCTION__);
				rt_pl011_tx_fill(ctx, true);
				/* update interrupt mask, disable tx irq */
				if (ctx->out_npend == 0) {
					pl011_disable_tx_int(ctx);
					rtdm_event_signal(&ctx->out_event);
					printk("%s wakeup write thread\n", __FUNCTION__);
				}
			}

			status = pl011_read(uap, REG_RIS);
			printk("%s get status again 0x%x, im 0x%x\n",
				__FUNCTION__, status, uap->im);
			status = status & uap->im;
		} while (status != 0);
	}

	if (ctx->in_nwait > 0) {
		if (ctx->in_nwait <= rbytes) {
			ctx->in_nwait = 0;
			rtdm_event_signal(&ctx->in_event);
			printk("%s wakeup read thread\n", __FUNCTION__);
		} else
			ctx->in_nwait -= rbytes;
	}

	if (events & ctx->config.event_mask) {
		int old_events = ctx->ioc_events;

		ctx->last_timestamp = timestamp;
		ctx->ioc_events = events;

		if (!old_events)
			rtdm_event_signal(&ctx->ioc_event);
	}

	rtdm_lock_put(&ctx->lock);

	return ret;
}

static int rt_pl011_uart_open(struct rtdm_fd *fd, int oflags)
{
	struct rt_pl011_uart_ctx *ctx;
	uint64_t *dummy;
	int ret;

	/* 第一步, 获取ctx并初始化 */
	/* 
     * ctx由rt_pl011_uart_open的调用者__rtdm_dev_open
     * 在函数create_instance里面分配
     */
	ctx = rtdm_fd_to_private(fd);
	/* IPC initialisation - cannot fail with used parameters */
	rtdm_lock_init(&ctx->lock);
	rtdm_event_init(&ctx->in_event, 0);
	rtdm_event_init(&ctx->out_event, 0);
	rtdm_event_init(&ctx->ioc_event, 0);
	rtdm_mutex_init(&ctx->out_lock);

	ctx->in_head = 0;
	ctx->in_tail = 0;
	ctx->in_npend = 0;
	ctx->in_nwait = 0;
	ctx->in_lock = 0;
	ctx->in_history = NULL;

	ctx->out_head = 0;
	ctx->out_tail = 0;
	ctx->out_npend = 0;

	ctx->ioc_events = 0;
	ctx->ioc_event_lock = 0;

	ctx->port = (struct rt_uart_amba_port *)rtdm_fd_device(fd)->device_data;

	/* 第二步, 硬件初始化 */
	ret = pl011_clock_init(ctx);
	if (ret) {
		printk("%s, pl011_clock_init fail, value 0x%x", 
			__FUNCTION__, ret);
	}
	
	pl011_hw_init(ctx);
	rt_pl011_uart_set_config(ctx, &default_config, &dummy);

	/* 第三步, 挂接中断 */
	ret = rtdm_irq_request(&ctx->irq_handle, ctx->port->port.irq,
			       rt_pl011_uart_int, 0,
			       rtdm_fd_device(fd)->name, ctx);
	if (ret) {
		/* 需要增加一步硬件的清理动作*/
		/* 软件清理动作 */
		printk("%s, rtdm_irq_request fail, value 0x%x", 
			__FUNCTION__, ret);
		rt_pl011_uart_cleanup_ctx(ctx);

		return ret;
	}

	/* 第四步, 使能中断 */
	pl011_enable_int(ctx);
	return 0;
}

static void rt_pl011_uart_close(struct rtdm_fd *fd)
{
	struct rt_uart_amba_port *uap;
	struct rt_pl011_uart_ctx *ctx;

	ctx = rtdm_fd_to_private(fd);
	ctx->port = (struct rt_uart_amba_port *)rtdm_fd_device(fd)->device_data;
	uap = ctx->port;
	pl011_disable_int(ctx);

	pl011_dma_shutdown(uap);

	rtdm_irq_free(&ctx->irq_handle);

	pl011_disable_uart(ctx);

	/*
	 * Shut down the clock producer
	 */
	clk_disable_unprepare(uap->clk);
	/* Optionally let pins go into sleep states */

	rtdm_irq_free(&ctx->irq_handle);

	rt_pl011_uart_cleanup_ctx(ctx);
	return;
}

/* 设置动作, 例如波特率的调整, 后续增加 */
static int rt_pl011_uart_ioctl(struct rtdm_fd *fd,
				 unsigned int request, void *arg)
{
	struct rt_pl011_uart_ctx *ctx;
	int err = 0;
	printk("%s request 0x%x\n", __FUNCTION__, request);
	ctx = rtdm_fd_to_private(fd);

	switch (request) {
	case RTSER_RTIOC_GET_CONFIG:
		if (rtdm_fd_is_user(fd))
			err = rtdm_safe_copy_to_user(fd, arg,
						     &ctx->config,
						     sizeof(struct
							    rtser_config));
		else
			memcpy(arg, &ctx->config, sizeof(struct rtser_config));
		break;

	case RTSER_RTIOC_SET_CONFIG: {
		struct rtser_config *config;
		struct rtser_config config_buf;
		uint64_t *hist_buf = NULL;

		config = (struct rtser_config *)arg;

		if (rtdm_fd_is_user(fd)) {
			err = rtdm_safe_copy_from_user(fd, &config_buf,
						       arg,
						       sizeof(struct
							      rtser_config));
			if (err)
				return err;

			config = &config_buf;
		}
		printk("%s, config_mask 0x%x, baud_rate %d, data_bits %d, "
				"stop_bits %d, parity %d, flow ctrl %d\n", __FUNCTION__,
			config->config_mask, config->baud_rate, config->data_bits,
			config->stop_bits, config->parity, config->handshake);

		if ((config->config_mask & RTSER_SET_BAUD) &&
		    (config->baud_rate <= 0))
			/* invalid baudrate for this port */
			return -EINVAL;

		if (config->config_mask & RTSER_SET_TIMESTAMP_HISTORY) {
			/*
			 * Reflect the call to non-RT as we will likely
			 * allocate or free the buffer.
			 */
			if (rtdm_in_rt_context())
				return -ENOSYS;

			if (config->timestamp_history & RTSER_RX_TIMESTAMP_HISTORY)
				hist_buf = kmalloc(IN_BUFFER_SIZE *
						   sizeof(nanosecs_abs_t),
						   GFP_KERNEL);
		}

		rt_pl011_uart_set_config(ctx, config, &hist_buf);

		if (hist_buf)
			kfree(hist_buf);

		break;
	}

	default:
		err = -ENOTTY;
	}

	return err;
}

static ssize_t rt_pl011_uart_read(struct rtdm_fd *fd, void *buf,
				    size_t nbyte)
{
	struct rt_pl011_uart_ctx *ctx;
	rtdm_lockctx_t lock_ctx;
	size_t read = 0;
	int pending;
	int block;
	int subblock;
	int in_pos;
	char *out_pos = (char *)buf;
	rtdm_toseq_t timeout_seq;
	ssize_t ret = -EAGAIN;	/* for non-blocking read */
	int nonblocking;
	printk("%s nbyte %d\n", __FUNCTION__, (unsigned int)nbyte);
	if (nbyte == 0)
		return 0;

	if (rtdm_fd_is_user(fd) && !rtdm_rw_user_ok(fd, buf, nbyte))
		return -EFAULT;

	ctx = rtdm_fd_to_private(fd);

	rtdm_toseq_init(&timeout_seq, ctx->config.rx_timeout);

	/* non-blocking is handled separately here */
	nonblocking = (ctx->config.rx_timeout < 0);

	/* only one reader allowed, stop any further attempts here */
	if (test_and_set_bit(0, &ctx->in_lock))
		return -EBUSY;

	rtdm_lock_get_irqsave(&ctx->lock, lock_ctx);

	while (1) {
		pending = ctx->in_npend;

		if (pending > 0) {
			block = subblock = (pending <= nbyte) ? pending : nbyte;
			in_pos = ctx->in_head;

			rtdm_lock_put_irqrestore(&ctx->lock, lock_ctx);

			/* Do we have to wrap around the buffer end? */
			if (in_pos + subblock > IN_BUFFER_SIZE) {
				/* Treat the block between head and buffer end
				   separately. */
				subblock = IN_BUFFER_SIZE - in_pos;

				if (rtdm_fd_is_user(fd)) {
					if (rtdm_copy_to_user
					    (fd, out_pos,
					     &ctx->in_buf[in_pos],
					     subblock) != 0) {
						ret = -EFAULT;
						goto break_unlocked;
					}
				} else
					memcpy(out_pos, &ctx->in_buf[in_pos],
					       subblock);

				read += subblock;
				out_pos += subblock;

				subblock = block - subblock;
				in_pos = 0;
			}

			if (rtdm_fd_is_user(fd)) {
				if (rtdm_copy_to_user(fd, out_pos,
						      &ctx->in_buf[in_pos],
						      subblock) != 0) {
					ret = -EFAULT;
					goto break_unlocked;
				}
			} else
				memcpy(out_pos, &ctx->in_buf[in_pos], subblock);

			read += subblock;
			out_pos += subblock;
			nbyte -= block;

			rtdm_lock_get_irqsave(&ctx->lock, lock_ctx);

			ctx->in_head =
			    (ctx->in_head + block) & (IN_BUFFER_SIZE - 1);
			if ((ctx->in_npend -= block) == 0)
				ctx->ioc_events &= ~RTSER_EVENT_RXPEND;

			if (nbyte == 0)
				break; /* All requested bytes read. */

			continue;
		}

		if (nonblocking)
			/* ret was set to EAGAIN in case of a real
			   non-blocking call or contains the error
			   returned by rtdm_event_wait[_until] */
			break;

		ctx->in_nwait = nbyte;

		rtdm_lock_put_irqrestore(&ctx->lock, lock_ctx);

		ret = rtdm_event_timedwait(&ctx->in_event,
					   ctx->config.rx_timeout,
					   &timeout_seq);
		if (ret < 0) {
			if (ret == -EIDRM) {
				/* Device has been closed -
				   return immediately. */
				return -EBADF;
			}

			rtdm_lock_get_irqsave(&ctx->lock, lock_ctx);

			nonblocking = 1;
			if (ctx->in_npend > 0) {
				/* Final turn: collect pending bytes
				   before exit. */
				continue;
			}

			ctx->in_nwait = 0;
			break;
		}

		rtdm_lock_get_irqsave(&ctx->lock, lock_ctx);
	}

	rtdm_lock_put_irqrestore(&ctx->lock, lock_ctx);

break_unlocked:
	/* Release the simple reader lock, */
	clear_bit(0, &ctx->in_lock);

	if ((read > 0) && ((ret == 0) || (ret == -EAGAIN) ||
			   (ret == -ETIMEDOUT) || (ret == -EINTR)))
		ret = read;

	return ret;
}

static ssize_t rt_pl011_uart_write(struct rtdm_fd *fd,
				     const void *buf,
				     size_t nbyte)
{
	struct rt_pl011_uart_ctx *ctx;
	rtdm_lockctx_t lock_ctx;
	size_t written = 0;
	int free;
	int block;
	int subblock;
	int out_pos;
	char *in_pos = (char *)buf;
	rtdm_toseq_t timeout_seq;
	ssize_t ret;
	printk("%s buf %s, len %d\n",
		__FUNCTION__, (char *)buf, (unsigned int)nbyte);
	if (nbyte == 0)
		return 0;

	if (rtdm_fd_is_user(fd) && !rtdm_read_user_ok(fd, buf, nbyte))
		return -EFAULT;

	ctx = rtdm_fd_to_private(fd);

	rtdm_toseq_init(&timeout_seq, ctx->config.tx_timeout);

	/* Make write operation atomic. */
	ret = rtdm_mutex_timedlock(&ctx->out_lock, ctx->config.tx_timeout,
				   &timeout_seq);
	if (ret)
		return ret;

	while (nbyte > 0) {
		rtdm_lock_get_irqsave(&ctx->lock, lock_ctx);

		free = OUT_BUFFER_SIZE - ctx->out_npend;

		if (free > 0) {
			block = subblock = (nbyte <= free) ? nbyte : free;
			out_pos = ctx->out_tail;

			rtdm_lock_put_irqrestore(&ctx->lock, lock_ctx);

			/* Do we have to wrap around the buffer end? */
			if (out_pos + subblock > OUT_BUFFER_SIZE) {
				/* Treat the block between head and buffer
				   end separately. */
				subblock = OUT_BUFFER_SIZE - out_pos;

				if (rtdm_fd_is_user(fd)) {
					if (rtdm_copy_from_user
					    (fd,
					     &ctx->out_buf[out_pos],
					     in_pos, subblock) != 0) {
						ret = -EFAULT;
						break;
					}
				} else
					memcpy(&ctx->out_buf[out_pos], in_pos,
					       subblock);

				written += subblock;
				in_pos += subblock;

				subblock = block - subblock;
				out_pos = 0;
			}

			if (rtdm_fd_is_user(fd)) {
				if (rtdm_copy_from_user
				    (fd, &ctx->out_buf[out_pos],
				     in_pos, subblock) != 0) {
					ret = -EFAULT;
					break;
				}
			} else
				memcpy(&ctx->out_buf[out_pos], in_pos, block);

			written += subblock;
			in_pos += subblock;
			nbyte -= block;

			rtdm_lock_get_irqsave(&ctx->lock, lock_ctx);

			ctx->out_tail =
			    (ctx->out_tail + block) & (OUT_BUFFER_SIZE - 1);
			ctx->out_npend += block;
			/* 填充数据到fifo */
			rt_pl011_tx_fill(ctx, false);
			/* unmask tx interrupt */
			if (ctx->out_npend)
				pl011_enable_tx_int(ctx);

			rtdm_lock_put_irqrestore(&ctx->lock, lock_ctx);
			continue;
		}

		rtdm_lock_put_irqrestore(&ctx->lock, lock_ctx);

		ret = rtdm_event_timedwait(&ctx->out_event,
					 ctx->config.tx_timeout,
					 &timeout_seq);
		if (ret < 0) {
			if (ret == -EIDRM) {
				/* Device has been closed -
				   return immediately. */
				return -EBADF;
			}
			if (ret == -EWOULDBLOCK) {
				/* Fix error code for non-blocking mode. */
				ret = -EAGAIN;
			}
			break;
		}
	}

	rtdm_mutex_unlock(&ctx->out_lock);

	if ((written > 0) && ((ret == 0) || (ret == -EAGAIN) ||
			      (ret == -ETIMEDOUT) || (ret == -EINTR)))
		ret = written;

	return ret;
}

static struct rtdm_driver pl011_uart_driver = {
	.profile_info		= RTDM_PROFILE_INFO(pl011_uart,
						    RTDM_CLASS_SERIAL,
						    RTDM_SUBCLASS_16550A,
						    RTSER_PROFILE_VER),
	.device_count		= UART_NR,
	.device_flags		= RTDM_NAMED_DEVICE | RTDM_EXCLUSIVE,
	.context_size		= sizeof(struct rt_pl011_uart_ctx),
	.ops = {
		.open		= rt_pl011_uart_open,
		.close		= rt_pl011_uart_close,
		.ioctl_rt	= rt_pl011_uart_ioctl,
		.ioctl_nrt	= rt_pl011_uart_ioctl,
		.read_rt	= rt_pl011_uart_read,
		.write_rt	= rt_pl011_uart_write,
	},
};

static int pl011_setup_port(struct device *dev, 
				struct rt_uart_amba_port *uap,
				struct resource *mmiobase)
{
	void __iomem *base;

	base = devm_ioremap_resource(dev, mmiobase);
	if (IS_ERR(base))
		return PTR_ERR(base);

	uap->port.mapbase = mmiobase->start;
	uap->port.membase = base;
	uap->port.fifosize = uap->fifosize;

	return 0;
}

/*
 * probe函数主要实现如下功能
 *	设备树的解析, 获取设备树关键的参数, 例如寄存器空间和中断号等
 *	寄存器虚拟地址空间分配
 *	rtdm_device设备的创建和注册
 *	串口寄存器清理
 */
static int pl011_probe(struct amba_device *dev, const struct amba_id *id)
{
	struct rt_uart_amba_port *uap;
	struct vendor_data *vendor = id->data;
	int ret;
	struct rtdm_device *rt_dev;

	printk(KERN_INFO "RTserial: AMBA probe begin\n");

	uap = devm_kzalloc(&dev->dev, 
			sizeof(struct rt_uart_amba_port), GFP_KERNEL);
	if (!uap) {
		printk(KERN_INFO "RTserial: AMBA probe step1 fail\n");
		return -ENOMEM;
	}
	uap->clk = devm_clk_get(&dev->dev, NULL);
	if (IS_ERR(uap->clk)) {
		printk(KERN_INFO "RTserial: AMBA probe step2 fail\n");
		return PTR_ERR(uap->clk);
	}
	uap->reg_offset = vendor->reg_offset;
	uap->vendor = vendor;
	uap->fifosize = vendor->get_fifosize(dev);
	uap->port.iotype = vendor->access_32b ? UPIO_MEM32 : UPIO_MEM;
	uap->port.irq = dev->irq[0];

	/* 解析设备树的资源地址空间给uap赋值 */
	ret = pl011_setup_port(&dev->dev, uap, &dev->res);

	rt_dev = devm_kzalloc(&dev->dev, 
			sizeof(struct rtdm_device), GFP_KERNEL);
	if (!rt_dev) {
		printk(KERN_INFO "RTserial: AMBA probe step3 fail\n");
		return -ENOMEM;
	}

	rt_dev->driver = &pl011_uart_driver;
	rt_dev->label = "rtser%d";
	/* operation函数需要通过该指针获取uap */
	rt_dev->device_data = uap;
	ret = rtdm_dev_register(rt_dev);
	if (ret) {
		printk(KERN_INFO "RTserial: AMBA probe step4 fail, ret 0x%x\n", ret);
		return ret;
	}
	amba_set_drvdata(dev, rt_dev); /* pl011_remove需要 */
	pl011_hw_clear(uap);
	printk(KERN_INFO "RTserial: AMBA probe end\n");
	return 0;
}

static int pl011_remove(struct amba_device *dev)
{
	struct rtdm_device *rt_dev = amba_get_drvdata(dev);
	printk(KERN_INFO "RTserial: AMBA remove begin\n");

	amba_set_drvdata(dev, NULL);
	rtdm_dev_unregister(rt_dev);
	printk(KERN_INFO "RTserial: AMBA remove end\n");
	return 0;
}

/* 根据设备模型驱动的规则, 用于和xenomai串口驱动匹配(match) */
static const struct amba_id pl011_ids[] = {
	{
		.id	= 0x00ffffff,
		.mask	= 0x00ffffff,
		.data	= &vendor_arm,
	},
	{ 0, 0 },
};

static struct amba_driver rt_pl011_driver = {
	.drv = {
		.name	= "rt-uart-pl011",
	},
	.id_table	= pl011_ids,
	.probe		= pl011_probe,
	.remove		= pl011_remove,
};
static int __init rt_pl011_init(void)
{
	if (!rtdm_available()) {
		printk(KERN_INFO "RTserial: rtdm not available\n");
		return -ENODEV;
	}
	return  amba_driver_register(&rt_pl011_driver);
}

static void __exit rt_pl011_exit(void)
{
	amba_driver_unregister(&rt_pl011_driver);
}

module_init(rt_pl011_init);
module_exit(rt_pl011_exit);

MODULE_AUTHOR("zhanglin1040@phytium.com.cn");
MODULE_DESCRIPTION("RTDM-based driver for ARM AMBA serial port");
MODULE_VERSION("1.0.0");
MODULE_LICENSE("GPL");
