# common providers of QorIQ targets
PREFERRED_PROVIDER_virtual/kernel ?= "linux-phytium"

SOC_DEFAULT_IMAGE_FSTYPES = "tar.gz ext2.gz.u-boot ext2.gz cpio.gz"
IMAGE_FSTYPES ?= "${SOC_DEFAULT_IMAGE_FSTYPES}"

MACHINE_FEATURES ?= "pci ext2 ext3 serial usbhost alsa touchscreen keyboard"
MACHINE_EXTRA_RRECOMMENDS += "udev-extraconf kernel-modules"

KERNEL_IMAGETYPE ?= "Image"

SERIAL_CONSOLES ?= "115200;ttyS0 115200;ttyS1 115200;ttyAMA1 115200;ttyAMA0"
SERIAL_CONSOLES_CHECK ?= "${SERIAL_CONSOLES}"

# Machines or distros can define which BSP it should use by default. We are
# intending to default for  BSP by default and specific machines or
# DISTROs might change it if need.
#
# Two values are considered valid: mainline
MACHINE_SOCARCH_SUFFIX ?= ""

MACHINE_ARCH_FILTER = "virtual/kernel"

XSERVER ?= "xserver-xorg \
            ${@bb.utils.contains('DISTRO_FEATURES', 'opengl', 'mesa-driver-swrast xserver-xorg-extension-glx', '', d)} \
            xf86-video-fbdev \
            xf86-video-amd \
            xf86-video-ati \
	    xf86-video-modesetting \
"
PREFERRED_VERSION_gstreamer1.0-libav ?= "1.16.0"
PREFERRED_VERSION_xen = "4.15+stableAUTOINC+e25aa9939a"
PREFERRED_VERSION_xen-tools = "4.15+stableAUTOINC+e25aa9939a"
