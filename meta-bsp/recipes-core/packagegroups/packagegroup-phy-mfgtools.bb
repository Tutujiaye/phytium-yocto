# Copyright (C) 2020 Phytium
# Released under the MIT license (see COPYING.MIT for the terms)

SUMMARY = "Phytium Package group for development tools"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

PACKAGE_ARCH = "${MACHINE_ARCH}"
inherit packagegroup

PACKAGES = "${PN}"

RDEPENDS_${PN} = " \
    packagegroup-core-boot \
    bash \
    dosfstools \
    mtd-utils \
    mtd-utils-ubifs \
    mtd-utils-jffs2 \
    e2fsprogs-mke2fs \
    hdparm \
    util-linux\
"
