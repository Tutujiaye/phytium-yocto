DESCRIPTION = "Provides userspace xenomai support and libraries needed to for \
real-time applications using the xenomai RTOS implementation"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://README;md5=d804868a35cdacf02fc7ec9fc0d016a7"
SECTION = "xenomai"
HOMEPAGE = "http://www.xenomai.org/"

XENOMAI_SRC = "xenomai-v3.1.3"
SRC_URI = "https://source.denx.de/Xenomai/xenomai/-/archive/v3.1.3/${XENOMAI_SRC}.tar.bz2"

S = "${WORKDIR}/xenomai-v${PV}"

inherit autotools pkgconfig

includedir = "/usr/include/xenomai"

SRC_URI[md5sum] = "38ba82b70180c2c7a95cdae1767c6de2"

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

