# Copyright (C) 2020 Phytium
# Released under the MIT license (see COPYING.MIT for the terms)

SUMMARY = "Phytium Package group for benchmarks"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

PACKAGE_ARCH = "${MACHINE_ARCH}"
inherit packagegroup

PACKAGES = "${PN}-core ${PN}-extended"

RDEPENDS_${PN}-core = "\
    iozone3 \
    iperf2 \
    lmbench \
    netperf \
    mtd-utils alsa-utils can-utils  netcat devmem2 evtest libgpiod \
    tcpdump memtester fio coremark unixbench bpftrace \
"
RDEPENDS_${PN}-extended = " \
    ${PN}-core \
    perf \
    nbench-byte \
    arm-benchmarks \
" 
