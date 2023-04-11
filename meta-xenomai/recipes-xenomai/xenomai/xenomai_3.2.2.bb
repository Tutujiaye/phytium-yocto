DESCRIPTION = "Provides userspace xenomai support and libraries needed to for \
real-time applications using the xenomai RTOS implementation"
LICENSE = "LGPL-2.1-or-later"
LIC_FILES_CHKSUM = "file://README;md5=d804868a35cdacf02fc7ec9fc0d016a7"
SECTION = "xenomai"
HOMEPAGE = "http://www.xenomai.org/"

XENOMAI_SRC = "xenomai-v3.2.2"
SRC_URI = "https://source.denx.de/Xenomai/xenomai/-/archive/v3.2.2/${XENOMAI_SRC}.tar.bz2"

S = "${WORKDIR}/xenomai-v${PV}"

inherit autotools pkgconfig

includedir = "/usr/include/xenomai"

SRC_URI[md5sum] = "76b123d7d9137d36fc60b1ccc7ca60d5"

PACKAGES += "${PN}-demos"

FILES:${PN}-demos = "/usr/demo /usr/share"
FILES:${PN}-dev += "/dev"
FILES:${PN} += " \
  /usr/lib/modechk.wrappers \
  /usr/lib/cobalt.wrappers \
  /usr/lib/dynlist.ld \
"

EXTRA_OECONF += " --enable-pshared --enable-smp \
                --with-core=cobalt"

