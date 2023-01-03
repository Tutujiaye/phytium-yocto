SUMMARY = "Gst-Shark Tracers"
DESCRIPTION = "Benchmarks and profiling tools for GStreamer"
HOMEPAGE = "https://developer.ridgerun.com/wiki/index.php?title=GstShark"
SECTION = "multimedia"
LICENSE = "GPLv2+"

LIC_FILES_CHKSUM = "file://Makefile;md5=ec13c0fd422eb9684f2c898e3516f6c3"

SRCBRANCH ?= "master"

SRC_URI = "git://git@gitee.com/phytium_embedded/vpu-lib.git;branch=${SRCBRANCH};protocol=ssh"

SRCREV = "a3109a35b230a57d860bce66d4369a74c56f12a8"

S = "${WORKDIR}/git"

do_install () {
	 mkdir -p ${D}${sysconfdir}/xdg
         mkdir -p ${D}/usr
         mkdir -p ${D}/usr/lib/gstreamer-1.0
	 oe_runmake install DESTDIR=${D}
         chown -R root:root  ${D}${sysconfdir}/xdg
         chown -R root:root  ${D}/usr/bin/vdec_test
         chown -R root:root  ${D}/usr/lib
         cp ${D}/usr/lib/aarch64-linux-gnu/gstreamer-1.0/*  ${D}/usr/lib/gstreamer-1.0/
}

FILES_${PN} += " /usr/lib/* /usr/bin/* /lib/firmware/* /etc/xdg/*"
INSANE_SKIP_${PN} += "ldflags dev-elf dev-deps file-rdeps"
INSANE_SKIP_${PN}-dev += "ldflags dev-elf file-rdeps"
