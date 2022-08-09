PACKAGE_INSTALL_append = " kernel-modules linux-firmware linux-firmware-radeon \
       ${@bb.utils.contains('MACHINE_FEATURES', 'gpu', '${IMAGE_INSTALL_GPU}', '', d)} \
"
IMAGE_INSTALL_GPU =" xorg-rogue-umlibs-firmware"

ROOTFS_POSTPROCESS_COMMAND_append = " clobber_unused;"

clobber_unused () {
        rm -rf ${IMAGE_ROOTFS}/boot/*
}

INITRAMFS_MAXSIZE = "402865"
