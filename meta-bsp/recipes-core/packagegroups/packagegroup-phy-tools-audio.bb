SUMMARY = "Set of audio tools for inclusion on images"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit packagegroup

ALSA_INSTALL = " \
    alsa-utils \
    alsa-tools \
"

PULSEAUDIO_INSTALL = " \
    pulseaudio-server \
    pulseaudio-module-cli \
    pulseaudio-misc \
    pulseaudio-module-device-manager \
    ${@bb.utils.contains('DISTRO_FEATURES',"x11", "pulseaudio-module-x11-xsmp \
                                                   pulseaudio-module-x11-publish \
                                                   pulseaudio-module-x11-cork-request \
                                                   pulseaudio-module-x11-bell \
                                                   consolekit", \
                                           "", d)} \
"

RDEPENDS:${PN} = " \
    ${@bb.utils.contains("DISTRO_FEATURES", "alsa",  "${ALSA_INSTALL}", "", d)} \
    ${@bb.utils.contains("DISTRO_FEATURES", "pulseaudio",  "${PULSEAUDIO_INSTALL}", "", d)} \
"
