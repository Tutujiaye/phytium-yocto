DESCRIPTION = "Set default locale"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit allarch

SRC_URI = "file://locale.conf \
           file://disable_dns_proxy.conf \
           file://tsize.sh"

do_configure[noexec] = '1'

do_compile[noexec] = '1'

do_install() {
    install -d ${D}${sysconfdir}
    install -d ${D}${sysconfdir}/profile.d/
    install -m 0755 ${WORKDIR}/tsize.sh ${D}${sysconfdir}/profile.d/tsize.sh
    install -m 644 ${WORKDIR}/locale.conf ${D}${sysconfdir}/
    install -d ${D}${sysconfdir}/systemd/system/connman.service.d/
    install -m 644 ${WORKDIR}/disable_dns_proxy.conf ${D}${sysconfdir}/systemd/system/connman.service.d/
}

INSANE_SKIP:${PN} += "file-rdeps"
