DESCRIPTION = "Provides userspace xenomai support and libraries needed to for \
real-time applications using the xenomai RTOS implementation"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://README;md5=d804868a35cdacf02fc7ec9fc0d016a7"
SECTION = "xenomai"
HOMEPAGE = "http://www.xenomai.org/"
PR = "r0"

SRC_URI = "git://source.denx.de/Xenomai/xenomai.git;protocol=https;nobranch=1"

S = "${WORKDIR}/git"

inherit autotools pkgconfig

includedir = "/usr/include/xenomai"

SRCREV = "2f808a258b5ed77bc747c616d9a50d77d2231cbf"

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

