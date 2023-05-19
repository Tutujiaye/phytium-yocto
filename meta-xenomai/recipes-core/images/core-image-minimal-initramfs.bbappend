PACKAGE_INSTALL += " kernel-modules linux-firmware linux-firmware-radeon "

ROOTFS_POSTPROCESS_COMMAND += " clobber_unused;"

clobber_unused () {
        rm -rf ${IMAGE_ROOTFS}/boot/*
}

INITRAMFS_MAXSIZE = "402865"
