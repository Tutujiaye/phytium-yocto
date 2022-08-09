INSTALLER_SERIAL ??= "1"
FILESEXTRAPATHS_prepend_anaconda := "${@bb.utils.contains('INSTALLER_SERIAL', '1', '${THISDIR}/files:', '', d)}"
SRC_URI_append_anaconda = " \
    ${@bb.utils.contains('INSTALLER_SERIAL', '1', 'file://serial-screen-anaconda.sh', '', d)} \
"

do_install_append_anaconda() {
	if [ "${INSTALLER_SERIAL}" = "1" ]; then
		install -d ${D}${sysconfdir}/profile.d
		install -m 644 ${WORKDIR}/serial-screen-anaconda.sh ${D}${sysconfdir}/profile.d/

		sed -i -e 's,@ROOT_HOME@,${ROOT_HOME},g' ${D}${systemd_unitdir}/system/serial-getty@.service
	fi
}
