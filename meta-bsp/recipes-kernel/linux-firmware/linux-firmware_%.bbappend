do_install_append() {
      rm -rf  ${D}/lib/firmware/ti-connectivity/
      rm -rf ${D}/lib/firmware/RTL8192E/
      rm -rf ${D}/lib/firmware/atmel/
      rm -rf ${D}/lib/firmware/atusb/
      rm -rf ${D}/lib/firmware/av7110/
      rm -rf ${D}/lib/firmware/bnx2x*
      rm -rf ${D}/lib/firmware/brcm/
      rm -rf ${D}/lib/firmware/cadence/
      rm -rf ${D}/lib/firmware/cavium/
      rm -rf ${D}/lib/firmware/cpia2/
      rm -rf ${D}/lib/firmware/dabusb/
      rm -rf ${D}/lib/firmware/dpaa2/
      rm -rf ${D}/lib/firmware/dsp56k/
      rm -rf ${D}/lib/firmware/dvb*
      rm -rf ${D}/lib/firmware/edgeport/
      rm -rf ${D}/lib/firmware/emi26/
      rm -rf ${D}/lib/firmware/emi62/
      rm -rf ${D}/lib/firmware/ess/
      rm -rf ${D}/lib/firmware/go7007/
      rm -rf ${D}/lib/firmware/inside-secure/
      rm -rf ${D}/lib/firmware/intel/
      rm -rf ${D}/lib/firmware/kaweth/
      rm -rf ${D}/lib/firmware/keyspan*/
      rm -rf ${D}/lib/firmware/korg/
      rm -rf ${D}/lib/firmware/libertas/
      rm -rf ${D}/lib/firmware/matrox/
      rm -rf ${D}/lib/firmware/mediatek/
      rm -rf ${D}/lib/firmware/mellanox/
      rm -rf ${D}/lib/firmware/meson/
      rm -rf ${D}/lib/firmware/microchip/
      rm -rf ${D}/lib/firmware/moxa/
      rm -rf ${D}/lib/firmware/mrvl/
      rm -rf ${D}/lib/firmware/mwl8k/
      rm -rf ${D}/lib/firmware/mwlwifi/
      rm -rf ${D}/lib/firmware/myricom/
      rm -rf ${D}/lib/firmware/nvidia/
      rm -rf ${D}/lib/firmware/qcom/
      rm -rf ${D}/lib/firmware/r128/
      rm -rf ${D}/lib/firmware/rockchip/
      rm -rf ${D}/lib/firmware/rsi/
      rm -rf ${D}/lib/firmware/rtl_bt/
      rm -rf ${D}/lib/firmware/rtlwifi/
      rm -rf ${D}/lib/firmware/rtw88/
      rm -rf ${D}/lib/firmware/sb16/
      rm -rf ${D}/lib/firmware/slicoss/
      rm -rf ${D}/lib/firmware/sxg/
      rm -rf ${D}/lib/firmware/ti-keystone/
      rm -rf ${D}/lib/firmware/ttusb-budget/
      rm -rf ${D}/lib/firmware/vicam/
      rm -rf ${D}/lib/firmware/ueagle-atm/
      rm -rf ${D}/lib/firmware/yam/
      rm -rf ${D}/lib/firmware/yamaha/
      rm -rf ${D}/lib/firmware/cbfw-3.2.1.1.bin
      rm -rf ${D}/lib/firmware/cbfw-3.2.3.0.bin
      rm -rf ${D}/lib/firmware/ct2fw-3.2.1.1.bin
      rm -rf ${D}/lib/firmware/ct2fw-3.2.3.0.bin
      rm -rf ${D}/lib/firmware/ctfw-3.2.1.1.bin
      rm -rf ${D}/lib/firmware/ctfw-3.2.3.0.bin
      rm -rf ${D}/lib/firmware/i2400m*
      rm -rf ${D}/lib/firmware/i6050-fw-usb-1.5.sbcf
      rm -rf ${D}/lib/firmware/agere*
      rm -rf ${D}/lib/firmware/mt7650.bin
      rm -rf ${D}/lib/firmware/mt7662.bin
      rm -rf ${D}/lib/firmware/ar5523.bin
      rm -rf ${D}/lib/firmware/as102*
      rm -rf ${D}/lib/firmware/atmsar11.fw
      rm -rf ${D}/lib/firmware/ath3k-1.fw
      rm -rf ${D}/lib/firmware/f2255usb.bin
      rm -rf ${D}/lib/firmware/hfi1*
      rm -rf ${D}/lib/firmware/r8a779x*
      rm -rf ${D}/lib/firmware/s5p-mfc*
      rm -rf ${D}/lib/firmware/sms1xxx*
      rm -rf ${D}/lib/firmware/v4l-*
      rm -rf ${D}/lib/firmware/lbtf_usb.bin
      rm -rf ${D}/lib/firmware/tlg2300_firmware.bin
      rm -rf ${D}/lib/firmware/wil6210*
      rm -rf ${D}/lib/firmware/wsm_22.bin
      rm -rf ${D}/lib/firmware/myri10ge_rss_ethp_big_z8e.dat
      rm -rf ${D}/lib/firmware/myri10ge_rss_eth_big_z8e.dat
      rm -rf ${D}/lib/firmware/myri10ge_ethp_big_z8e.dat
      rm -rf ${D}/lib/firmware/myri10ge_eth_big_z8e.dat
      rm -rf ${D}/lib/firmware/tr_smctr.bin
      rm -rf ${D}/lib/firmware/whiteheat*
      rm -rf ${D}/lib/firmware/sdd_sagrad_1091_1098.bin
      rm -rf ${D}/lib/firmware/tdmb_nova_12mhz.inp
      rm -rf ${D}/lib/firmware/ti_3410.fw
      rm -rf ${D}/lib/firmware/ti_5052.fw
      rm -rf ${D}/lib/firmware/rp2.fw
      rm -rf ${D}/lib/firmware/rsi_91x.fw
      rm -rf ${D}/lib/firmware/mts_*
      rm -rf ${D}/lib/firmware/mt7662_rom_patch.bin
      rm -rf ${D}/lib/firmware/lgs8g75.fw
      rm -rf ${D}/lib/firmware/isdbt*
      rm -rf ${D}/lib/firmware/intelliport2.bin
      rm -rf ${D}/lib/firmware/*.txt
      rm -rf ${D}/lib/firmware/cmmb*
      rm -rf ${D}/lib/firmware/ctefx.bin
      rm -rf ${D}/lib/firmware/ctspeq.bin
      rm -rf ${D}/lib/firmware/*firmware.bin
      rm -rf ${D}/lib/firmware/3com/3C359.bin
      rm -rf ${D}/lib/firmware/bnx2/
      rm -rf ${D}/lib/firmware/cxgb4/bcm8483.bin
      rm -rf ${D}/lib/firmware/cxgb4/aq1202_fw.cld
      rm -rf ${D}/lib/firmware/cxgb4/t5fw-1.14.4.0.bin
      rm -rf ${D}/lib/firmware/cxgb4/t5fw-1.15.37.0.bin
      rm -rf ${D}/lib/firmware/cxgb4/t4fw-1.14.4.0.bin
      rm -rf ${D}/lib/firmware/cxgb4/t4fw-1.15.37.0.bin
      rm -rf ${D}/lib/firmware/qed/qed_init_values-8*
      rm -rf ${D}/lib/firmware/qed/qed_init_values_zipped-8.33.*
      rm -rf ${D}/lib/firmware/qed/qed_init_values_zipped-8.4.2.0.bin
      rm -rf ${D}/lib/firmware/qed/qed_init_values_zipped-8.42.2.0.bin
      rm -rf ${D}/lib/firmware/netronome/bpf/
      rm -rf ${D}/lib/firmware/netronome/flower/
      rm -rf ${D}/lib/firmware/netronome/nic-sriov/
      rm -rf ${D}/lib/firmware/netronome/nic/nic_AMDA0058*
      rm -rf ${D}/lib/firmware/netronome/nic/nic_AMDA0078-0011_1x100.nffw
      rm -rf ${D}/lib/firmware/tigon/tg357766.bin
      rm -rf ${D}/lib/firmware/qlogic/isp1000.bin
      rm -rf ${D}/lib/firmware/qlogic/sd7220.fw
      rm -rf ${D}/lib/firmware/qed/qed_init_values_zipped-8.10.10.0.bin
      rm -rf ${D}/lib/firmware/qed/qed_init_values_zipped-8.10.5.0.bin
      rm -rf ${D}/lib/firmware/qed/qed_init_values_zipped-8.15.3.0.bin
      rm -rf ${D}/lib/firmware/qed/qed_init_values_zipped-8.20.0.0.bin
      rm -rf ${D}/lib/firmware/qed/qed_init_values_zipped-8.7.3.0.bin
      rm -rf ${D}/lib/firmware/qed/qed_init_values_zipped-8.37.2.0.bin
      rm -rf ${D}/lib/firmware/iwlwifi*
      rm -rf ${D}/lib/firmware/LICENCE*
      rm -rf ${D}/lib/firmware/htc_*
      rm -rf ${D}/lib/firmware/ar7010*
}

