DESCRIPTION = "Packages required by target build to help the installation"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit packagegroup

RDEPENDS_${PN} = " \
    sudo \
    kernel-image \
    kernel-modules \
    grubby \
    shadow \
    efibootmgr \
    kmod \
    lvm2 \
    lvm2-udevrules \
    util-linux-mount \
    util-linux-switch-root \
    init-ifupdown \
    ${@bb.utils.contains('DISTRO_FEATURES', 'pam', 'libuser', '', d)} \
    dracut \
    cryptsetup \
    sed \
    grep \
    dosfstools \
    e2fsprogs \
    btrfs-tools \
    grub-efi \
"

COMPATIBLE_HOST_armv7a = 'null'
COMPATIBLE_HOST_armv7ve = 'null'
