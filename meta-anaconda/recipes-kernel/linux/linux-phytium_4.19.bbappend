FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += " \
            file://iso.config \
"

DELTA_KERNEL_DEFCONFIG_anaconda = " iso.config"
DELTA_KERNEL_DEFCONFIG = " iso.config"
