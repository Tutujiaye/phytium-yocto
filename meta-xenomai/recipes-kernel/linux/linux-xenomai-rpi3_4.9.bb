DESCRIPTION = "Linux/Xenomai kernel for Pi 3"

IPIPE_PATCH = "ipipe-core-4.9.45-arm-custom.patch"
XENOMAI_SRC = "xenomai-3.0.7"

include recipes-kernel/linux/linux-xenomai.inc

LINUX_VERSION ?= "4.9.45"

SRCREV = "3ce72830a8c8bba33c37ebe4bee71ac3177451b0"

SRC_URI = "git://github.com/raspberrypi/linux.git;protocol=git;branch=rpi-4.9.y"

# Xenomai source (prepare_kernel.sh script)
SRC_URI[xeno.md5sum] = "c309036358fd403e9894fbbf53be91a1"

COMPATIBLE_MACHINE = "raspberrypi3"
