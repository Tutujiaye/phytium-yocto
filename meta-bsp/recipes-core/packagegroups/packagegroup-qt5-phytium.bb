# Copyright 2020 PHYTIUM
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "Package group for PHTTIUM Qt5"
LICENSE = "MIT"

inherit packagegroup

# Install fonts
QT5_FONTS = "ttf-dejavu-common ttf-dejavu-sans ttf-dejavu-sans-mono ttf-dejavu-serif "

QT5_IMAGE_INSTALL_common = " \
    packagegroup-qt5-demos \
    ${QT5_FONTS} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'x11', 'libxkbcommon mesa-demos x11perf', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'qtwayland qtwayland-plugins', '', d)}\
    "

QT5_IMAGE_INSTALL = " \
    ${QT5_IMAGE_INSTALL_common} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'x11', 'gstreamer1.0-plugins-good', '', d)} \
"

QT5_IMAGE_INSTALL += "${@bb.utils.contains('DISTRO_FEATURES', 'x11','', \
    'qtbase qtbase-plugins', d)}"

RDEPENDS_${PN} += " \
    ${QT5_IMAGE_INSTALL} \
"
