DESCRIPTION = "Provides userspace xenomai support and libraries needed to for \
real-time applications using the xenomai RTOS implementation"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://README;md5=d804868a35cdacf02fc7ec9fc0d016a7"
SECTION = "xenomai"
HOMEPAGE = "http://www.xenomai.org/"

XENOMAI_SRC = "xenomai-v3.1.2"
SRC_URI = "https://source.denx.de/Xenomai/xenomai/-/archive/v3.1.2/${XENOMAI_SRC}.tar.bz2"

S = "${WORKDIR}/xenomai-v${PV}"

inherit autotools pkgconfig

includedir = "/usr/include/xenomai"

SRC_URI[md5sum] = "3d3583d8fd1fc75e0b0561786d4cebb7"

PACKAGES += "${PN}-demos"

FILES_${PN}-demos = "/usr/demo"
FILES_${PN}-dev += "/dev"
FILES_${PN} += " \
  /usr/lib/modechk.wrappers \
  /usr/lib/cobalt.wrappers \
  /usr/lib/dynlist.ld \
"

EXTRA_OECONF += " --enable-pshared --enable-smp \
                --with-core=cobalt"

