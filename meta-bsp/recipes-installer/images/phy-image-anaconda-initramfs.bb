# Simple initramfs image. Mostly used for live images.
DESCRIPTION = "Small image capable of booting a device. The kernel includes \
the Minimal RAM-based Initial Root Filesystem (initramfs), which finds the \
first 'init' program more efficiently."

INITRAMFS_SCRIPTS ?= "\
                      initramfs-framework-base \
                      initramfs-module-setup-live \
                      initramfs-module-udev \
                      initramfs-module-install \
                      initramfs-module-install-efi \
                     "

PACKAGE_INSTALL = "${INITRAMFS_SCRIPTS} ${VIRTUAL-RUNTIME_base-utils} udev base-passwd ${ROOTFS_BOOTSTRAP_INSTALL}"

# Do not pollute the initrd image with rootfs features
IMAGE_FEATURES = ""

export IMAGE_BASENAME = "${MLPREFIX}phy-image-anaconda-initramfs"
IMAGE_LINGUAS = "en-us en-gb"

LICENSE = "MIT"

IMAGE_FSTYPES = "${INITRAMFS_FSTYPES}"
inherit core-image

IMAGE_ROOTFS_SIZE = "8192"
IMAGE_ROOTFS_EXTRA_SPACE = "0"

PACKAGE_INSTALL += " kernel-modules vpu-firmware \
                     lvm2 lvm2-udevrules initramfs-module-lvm  \ 
                     ${@bb.utils.contains('MACHINE_FEATURES', 'gpu', '${PACKAGE_INSTALL_GPU}', '', d)} \
"
PACKAGE_EXCLUDE = "kernel-image-*"

PACKAGE_INSTALL_GPU = " "

INITRAMFS_MAXSIZE ??= "423270"

