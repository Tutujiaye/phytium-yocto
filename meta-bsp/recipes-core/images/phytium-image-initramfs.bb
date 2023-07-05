DESCRIPTION = "Simple initramfs image for mounting the rootfs over the verity device mapper."

inherit core-image

PACKAGE_INSTALL = " \
    base-files \
    udev \
    systemd-initramfs \
    ${@bb.utils.contains('DISTRO_FEATURES', 'anaconda-support', 'lvm2 lvm2-udevrules', '', d)} \
    phytium-init \
    wireless-regdb-static \
    kernel-modules \
"
PACKAGE_EXCLUDE = "kernel-image-*" 

inherit phy-utils

ROOTFS_POSTPROCESS_COMMAND += "rootfs_copy_initramfs_target;"

# We want a clean, minimal image.
IMAGE_FEATURES = ""
IMAGE_LINGUAS = "en-us en-gb"

IMAGE_FSTYPES = "${INITRAMFS_FSTYPES}"
INITRAMFS_MAXSIZE ??= "380874"

IMAGE_NAME = "initrd.img"
