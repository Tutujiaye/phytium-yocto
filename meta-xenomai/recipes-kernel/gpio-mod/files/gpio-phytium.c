#include <linux/module.h>
#include <rtdm/gpio.h>
/* 
 * xenomai 3.1实现了5种厂家的xenomai驱动, 编号从1到5
 * 目前看到只用于作为exit函数的参数使用, 用于卸载校验
 */
#define RTDM_SUBCLASS_PHYTIUM  6

static int __init phytium_gpio_init(void)
{
	return rtdm_gpiochip_scan_of(NULL, "phytium,gpio", RTDM_SUBCLASS_PHYTIUM);
}
module_init(phytium_gpio_init);

static void __exit phytium_gpio_exit(void)
{
	rtdm_gpiochip_remove_of(RTDM_SUBCLASS_PHYTIUM);
}
module_exit(phytium_gpio_exit);

MODULE_AUTHOR("zhanglin1040@phytium.com.cn");
MODULE_DESCRIPTION("RTDM-based driver for PHYTIUM D2000 GPIO");
MODULE_VERSION("1.0.0");
MODULE_LICENSE("GPL");

