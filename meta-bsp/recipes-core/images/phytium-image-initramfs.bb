DESCRIPTION = "Simple initramfs image for mounting the rootfs over the verity device mapper."

inherit core-image

PACKAGE_INSTALL = " \
    base-files \
    udev \
    systemd-initramfs \
    lvm2 \
    lvm2-udevrules \
    phytium-init \
    linux-firmware linux-firmware-radeon \
    ${@bb.utils.contains('MACHINE_FEATURES', 'gpu', 'xorg-rogue-umlibs-firmware', '', d)} \
"


inherit phy-utils

ROOTFS_POSTPROCESS_COMMAND += "rootfs_copy_initramfs_target;"


# We want a clean, minimal image.
IMAGE_FEATURES = ""
IMAGE_LINGUAS = "en-us en-gb"

IMAGE_FSTYPES = "${INITRAMFS_FSTYPES}"
INITRAMFS_MAXSIZE ??= "630008"

IMAGE_NAME = "initrd.img"
