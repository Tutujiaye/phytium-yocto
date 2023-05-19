DESCRIPTION = "Customized settings for the Xfce desktop environment."
SECTION = "x11"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"
S = "${WORKDIR}"

DEPENDS = "shadow-native pseudo-native"
RDEPENDS:${PN} = "bash xfce4-settings xfce4-session xfce4-panel"

homedir = "/home/root"
confdir = "${homedir}/.config"
backgrounddir = "/usr/share/backgrounds/xfce"

SRC_URI = "file://xfce4/desktop/phytium-desktop-background.jpg \
	   file://xfce4/xfconf/xfce-perchannel-xml/xfce4-desktop.xml \
           file://xfce4/panel/launcher-17/file_manager_launcher.desktop \ 
           file://xfce4/panel/launcher-18/terminal_emulator_launcher.desktop \
           file://xfce4/panel/launcher-19/settings_manager_launcher.desktop \
           file://xfce4/panel/launcher-20/display_settings_launcher.desktop \
           file://xfce4/xfconf/xfce-perchannel-xml/xfce4-panel.xml \
"

FILES:${PN} = "${backgrounddir}/phytium-desktop-background.jpg \
               ${confdir}/xfce4/xfconf/xfce-perchannel-xml/xfce4-desktop.xml \
               ${confdir}/xfce4/panel/launcher-17/file_manager_launcher.desktop \
               ${confdir}/xfce4/panel/launcher-18/terminal_emulator_launcher.desktop \
               ${confdir}/xfce4/panel/launcher-19/settings_manager_launcher.desktop \
               ${confdir}/xfce4/panel/launcher-20/display_settings_launcher.desktop \
               ${confdir}/xfce4/xfconf/xfce-perchannel-xml/xfce4-panel.xml \
"

do_install () {
	install -d ${D}${confdir}/xfce4/xfconf/xfce-perchannel-xml
        install -d ${D}${confdir}/xfce4/panel/launcher-17
        install -d ${D}${confdir}/xfce4/panel/launcher-18
        install -d ${D}${confdir}/xfce4/panel/launcher-19
        install -d ${D}${confdir}/xfce4/panel/launcher-20
        install -d ${D}${backgrounddir}
        install -m 0644 ${S}/xfce4/desktop/phytium-desktop-background.jpg ${D}${backgrounddir}/
        install -m 0644 ${S}/xfce4/xfconf/xfce-perchannel-xml/xfce4-desktop.xml ${D}${confdir}/xfce4/xfconf/xfce-perchannel-xml/
        install -m 0644 ${S}/xfce4/panel/launcher-17/file_manager_launcher.desktop ${D}${confdir}/xfce4/panel/launcher-17/
        install -m 0644 ${S}/xfce4/panel/launcher-18/terminal_emulator_launcher.desktop ${D}${confdir}/xfce4/panel/launcher-18/
        install -m 0644 ${S}/xfce4/panel/launcher-19/settings_manager_launcher.desktop ${D}${confdir}/xfce4/panel/launcher-19/
        install -m 0644 ${S}/xfce4/panel/launcher-20/display_settings_launcher.desktop ${D}${confdir}/xfce4/panel/launcher-20/
        install -m 0644 ${S}/xfce4/xfconf/xfce-perchannel-xml/xfce4-panel.xml ${D}${confdir}/xfce4/xfconf/xfce-perchannel-xml/
}
