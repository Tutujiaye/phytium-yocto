require vpu.inc
do_install () {
	mkdir -p ${D}${sysconfdir}/xdg
        mkdir -p ${D}/usr
        mkdir -p ${D}/usr/lib/gstreamer-1.0
	oe_runmake install DESTDIR=${D}
        chown -R root:root  ${D}${sysconfdir}/xdg
        chown -R root:root  ${D}/usr/bin/vdec_test
        chown -R root:root  ${D}/usr/lib
        rm -rf  ${D}/lib
        cp -rf ${D}/usr/lib/aarch64-linux-gnu/gstreamer-1.0/*  ${D}/usr/lib/gstreamer-1.0/
}

FILES:${PN} += " /usr/lib/* /usr/bin/* /etc/xdg/* "
INSANE_SKIP:${PN}-dev += "ldflags dev-elf file-rdeps"

