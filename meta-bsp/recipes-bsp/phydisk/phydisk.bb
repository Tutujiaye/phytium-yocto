LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit genimage

DEPENDS += "mtools-native e2fsprogs-native genext2fs-native"
do_genimage[depends] += "virtual/kernel:do_deploy core-image-xfce:do_image_complete"

SRC_URI += "file://genimage.config"

GENIMAGE_ROOTFS_IMAGE ?= "core-image-xfce"
GENIMAGE_ROOTFS_IMAGE_FSTYPE ?= "tar.gz"
GENIMAGE_CREATE_BMAP = "1"
GENIMAGE_IMAGE_FULLNAME = "phydisk.img"

