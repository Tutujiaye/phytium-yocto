# Copyright (C) 2020 Phytium

require recipes-core/images/core-image-minimal.bb

SUMMARY = "Small image commonly used for manufacturing or other small image needs."
DESCRIPTION = "Small image which only includes essential manufacturing \
packages to deploy other big images to large physical media, such as \
a USB stick or a hard drive."

ROOTFS_IMAGE ?= "phytium-image-lxqt"
ROOTFS_RAM ?= "core-image-minimal-initramfs"
do_deploy[depends] += "${ROOTFS_IMAGE}:do_build"
do_deploy[depends] += "${ROOTFS_RAM}:do_build"

inherit phy-utils
ROOTFS_POSTPROCESS_COMMAND += "rootfs_copy_core_image_lxqt;"
#ROOTFS_POSTPROCESS_COMMAND += "rootfs_copy_core_image_ubuntu;"

LICENSE = "MIT"

IMAGE_INSTALL_append = " \
    packagegroup-core-ssh-dropbear \
    packagegroup-phy-mfgtools \
    udev-extraconf \
    file \
    parted \
    lmsensors-sensors \
    sudo \ 
    curl \
    merge-files \
"

export IMAGE_BASENAME = "phytium-image-install"

IMAGE_ROOTFS_EXTRA_SPACE = "262144"
IMAGE_FSTYPES = "ext4.gz wic"
WKS_FILE = "phy-install-bootdisk.wks.in"
