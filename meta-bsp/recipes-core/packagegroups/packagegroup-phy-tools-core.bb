# Copyright (C) 2020 Phytium
# Released under the MIT license (see COPYING.MIT for the terms)

SUMMARY = "Phytium Package group for core tools"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

PACKAGE_ARCH = "${MACHINE_ARCH}"
inherit packagegroup

PACKAGES = "${PN}"

RDEPENDS_${PN} = " \
    e2fsprogs \
    e2fsprogs-badblocks \
    e2fsprogs-e2fsck \
    e2fsprogs-tune2fs  \
    i2c-tools \
    kmod \
    kernel-modules \
    pkgconfig \
    procps \
    minicom \
    coreutils \
    elfutils \
    file \
    psmisc \
    sysfsutils \
    sysklogd \
    sysstat \
    pciutils \
    usbutils \
    mmc-utils \
    acpica \
"
