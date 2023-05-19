require vpu.inc
do_install () {
	mkdir -p ${D}/lib/firmware
	chown -R root:root  ${D}/lib/firmware
        cp ${S}/e2000/lib/firmware/*  ${D}/lib/firmware/
}

FILES:${PN} += " /lib/firmware/*"
