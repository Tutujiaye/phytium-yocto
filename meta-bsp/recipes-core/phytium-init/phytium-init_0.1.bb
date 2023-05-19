SUMMARY = "Init script for phytium logo"
LICENSE = "MIT"
SRC_URI = "file://initrd-release \
           file://systemd.conf \
           file://system.conf \
           file://basic.targetconf \
"

LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

S = "${WORKDIR}"

do_install() {
	install -d ${D}/sysroot
        install -d ${D}/etc
        install -d ${D}/etc/systemd
        install -d ${D}/etc/conf.d
        install -d ${D}/usr/lib
        install -m 0755 ${WORKDIR}/initrd-release ${D}/etc/
        install -m 0755 ${WORKDIR}/initrd-release ${D}/usr/lib/
        install -m 0644 ${WORKDIR}/systemd.conf ${D}/etc/conf.d/
        install -m 0644 ${WORKDIR}/basic.targetconf ${D}/etc/
        
}

FILES:${PN} = "/sysroot /etc/initrd-release /usr/lib/initrd-release /etc/conf.d/systemd.conf /etc/systemd/ \
   /etc/basic.targetconf \
"
