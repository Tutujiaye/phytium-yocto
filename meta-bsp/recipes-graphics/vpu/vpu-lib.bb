SUMMARY = "vpu binary static library"
DESCRIPTION = "vpu binary static library"
HOMEPAGE = "https://gitee.com/phytium_embedded/vpu-lib"
SECTION = "multimedia"
LICENSE = "PSLA"

LIC_FILES_CHKSUM = "file://LICENSE;md5=91f9c930eb49a8a38abed6fd1e182588"
SRCBRANCH ?= "master"

SRC_URI = "git://git@gitee.com/phytium_embedded/vpu-lib.git;branch=${SRCBRANCH};protocol=ssh"

SRCREV = "1c5de03cadb170d7389ae43bc297592f047f865e"

S = "${WORKDIR}/git"

do_install () {
	 mkdir -p ${D}${sysconfdir}/xdg
         mkdir -p ${D}/usr
         mkdir -p ${D}/usr/lib/gstreamer-1.0
	 oe_runmake install DESTDIR=${D}
         chown -R root:root  ${D}${sysconfdir}/xdg
         chown -R root:root  ${D}/usr/bin/vdec_test
         chown -R root:root  ${D}/usr/lib
         cp -rf ${D}/usr/lib/aarch64-linux-gnu/gstreamer-1.0/*  ${D}/usr/lib/gstreamer-1.0/
}

FILES_${PN} += " /usr/lib/* /usr/bin/* /lib/firmware/* /etc/xdg/*"
INSANE_SKIP_${PN} += "ldflags dev-elf dev-deps file-rdeps"
INSANE_SKIP_${PN}-dev += "ldflags dev-elf file-rdeps"

CLEANBROKEN = "1"
