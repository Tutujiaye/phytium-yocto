# Copyright (C) 2020 Phytium
# Released under the MIT license (see COPYING.MIT for the terms)

SUMMARY = "Phytium Package group for core networking tools"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

PACKAGE_ARCH = "${MACHINE_ARCH}"
inherit packagegroup

PACKAGES = "${PN} ${PN}-server"

RDEPENDS_${PN} = " \
    attr \
    ethtool \
    iproute2 \
    iproute2-tc \
    iptables \
    iputils \
    inetutils \
    inetutils-hostname \
    inetutils-ifconfig \
    inetutils-logger \
    inetutils-telnet \
    inetutils-traceroute \
    net-tools \
    watchdog \
"

RDEPENDS_${PN}-server = " \
    inetutils-inetd \
    inetutils-rshd \
    inetutils-telnetd \
"
