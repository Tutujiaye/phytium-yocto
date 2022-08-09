FILESEXTRAPATHS_prepend_anaconda := "${THISDIR}/files:"
SRC_URI_append_anaconda = " \
    file://0001-set-tty2-as-default-instance.patch \
"

do_install_append_anaconda() {
    # Explicitly enable tty2
    mkdir -p ${D}${sysconfdir}/systemd/system/getty.target.wants
    ln -nsf ${systemd_unitdir}/system/getty@.service \
        ${D}${sysconfdir}/systemd/system/getty.target.wants/getty@tty2.service

    rm -f ${D}${systemd_unitdir}/system/autovt@.service
}

PACKAGECONFIG_append = " cryptsetup \
     gcrypt  \
"
