FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

GPU_PATCHES = "file://weston.ini \
            file://weston-start \ 
"

SRC_URI_append = " ${@bb.utils.contains('MACHINE_FEATURES', 'gpu', '${GPU_PATCHES}', '', d)}"
SRC_URI_append = " file://desktop.png \
                   file://weston.iniphy"

# To customize weston.ini, start by setting the desired assignment in weston.ini,
# commented out. For example:
#     #xwayland=true
# Then add the assignment to INI_UNCOMMENT_ASSIGNMENTS.
INI_UNCOMMENT_ASSIGNMENTS_append_mx8mp = " \
    use-g2d=1 \
"

update_file() {
    if ! grep -q "$1" $3; then
        bbfatal $1 not found in $3
    fi
    sed -i -e "s,$1,$2," $3
}



do_install_append() {
    if ${@bb.utils.contains('MACHINE_FEATURES','gpu','true','false',d)}; then
        install -Dm0755 ${WORKDIR}/desktop.png ${D}${sysconfdir}/xdg/weston/
        install -D -p -m0644  ${WORKDIR}/weston.ini  ${D}${sysconfdir}/xdg/weston/weston.ini
        install -Dm755 ${WORKDIR}/weston-start ${D}${bindir}/weston-start
        sed -i 's,@DATADIR@,${datadir},g' ${D}${bindir}/weston-start
        sed -i 's,@LOCALSTATEDIR@,${localstatedir},g' ${D}${bindir}/weston-start
        update_file "ExecStart=/usr/bin/weston --modules=systemd-notify.so" "ExecStart=/usr/local/bin/weston " ${D}${systemd_system_unitdir}/weston.service
    else 
        install -Dm0755 ${WORKDIR}/desktop.png ${D}${sysconfdir}/xdg/weston/
        install -D -p -m0644  ${WORKDIR}/weston.iniphy  ${D}${sysconfdir}/xdg/weston/weston.ini
    fi
    # FIXME: weston should be run as weston, not as root
    update_file "User=weston" "User=root" ${D}${systemd_system_unitdir}/weston.service
    update_file "Group=weston" "Group=root" ${D}${systemd_system_unitdir}/weston.service

}

SYSTEMD_AUTO_ENABLE = "enable"
