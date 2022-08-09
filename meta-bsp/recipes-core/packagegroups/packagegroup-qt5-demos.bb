# Copyright (C) 2020 Phytium
# Copyright 2019-2020 phytium
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "Package group for Qt5 demos"
LICENSE = "MIT"

inherit packagegroup

QT5-EXAMPLES = \
    "${@bb.utils.contains('DISTRO_FEATURES', 'x11 wayland', 'qtbase-examples',\
        bb.utils.contains('DISTRO_FEATURES',   'wayland',     'qtwayland-examples', \
                                                             'qtbase-examples', d), d)}"


RDEPENDS_${PN}_append = " \
    ${QT5-EXAMPLES} \
    qtdeclarative \
    quitindicators \
"

# Install the following apps on SoC with GPU
RDEPENDS_${PN}_append = " \
     ${@bb.utils.contains('DISTRO_FEATURES', 'x11','${QT5_IMAGE_INSTALL_BASE}', '', d)} \
"

QT5_IMAGE_INSTALL_BASE = " \
    qt5ledscreen \
    quitbattery \
    qt5everywheredemo \ 
    qt5nmapcarousedemo \
    qt5nmapper \
    cinematicexperience \
"
