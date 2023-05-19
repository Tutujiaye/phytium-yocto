FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI += " file://client.conf "

do_install:append() {
    if [ -e "${WORKDIR}/client.conf" ]; then
        install -m 0644 ${WORKDIR}/client.conf ${D}${sysconfdir}/pulse/client.conf
    fi
}

PACKAGECONFIG:append = " autospawn-for-root"
