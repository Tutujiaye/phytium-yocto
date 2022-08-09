SYSTEMD_AUTO_ENABLE_anaconda = "disable"

do_install_append_anaconda () {
    install -d ${D}${sysconfdir}/multipath
    install -m 0644 ${WORKDIR}/multipath.conf.example \
    ${D}${sysconfdir}/multipath.conf
}

