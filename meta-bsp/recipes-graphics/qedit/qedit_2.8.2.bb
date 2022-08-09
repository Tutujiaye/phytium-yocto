SUMMARY = "QEdit Text Editor"
HOMEPAGE = "http://hugo.pereira.free.fr/software/index.php?page=package&package_list=software_list_qt&package=qedit&full=0"
SECTION = "x11"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=81bcece21748c91ba9992349a91ec11d"

DEPENDS  = "qtbase qtx11extras"

inherit cmake_qt5

SRC_URI  = " \
    http://hugo.pereira.free.fr/software/tgz/qedit-2.8.2.tar.gz \
    file://qedit.desktop \
    file://0001-Options.cpp-provide-default-for-SIDE_PANEL_TOOLBAR_T.patch \
"
SRC_URI[md5sum] = "7cca177ea042b230678ba0bb36269665"
SRC_URI[sha256sum] = "cc0686927ec8ffe44527f682b3a8d9585f753d643543fae061cb023a3b71ddb0"

EXTRA_OECMAKE = "-DUSE_QT5=ON"
#export EXTRA_OECMAKE = "-DCMAKE_INSTALL_PREFIX=/usr" 
do_configure() {
    # Ensure we get the cmake configure and not qmake
    cmake_do_configure
}

do_install_append () {
    install -d ${D}/${datadir}/applications
    install -m 755 ${WORKDIR}/qedit.desktop ${D}/${datadir}/applications/
}

FILES_${PN} += ""
FILES_${PN}-dev += ""
