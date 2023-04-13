DESCRIPTION = "Userspace libraries for PowerVR Rogue GPU in Phytium SoCs"
HOMEPAGE = "https://gitee.com/phytium_embedded/phytium-rogue-umlibs.git"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://README.md;md5=d368237c2530f972311f84ee015a42b7"

inherit features_check

REQUIRED_MACHINE_FEATURES = "gpu"

PACKAGE_ARCH = "${MACHINE_ARCH}"

BRANCH = "master"

SRC_URI = "git://git@gitee.com/phytium_embedded/phytium-rogue-umlibs.git;branch=${BRANCH};protocol=https"

SRCREV = "97b939baee78fb8604b10f2fc5de316701b84546"

PVR_SOC ?= "phytium-linux"
PVR_WS = "wayland"

INITSCRIPT_NAME = "rc.pvr"
INITSCRIPT_PARAMS = "defaults 8"

inherit update-rc.d

DEPENDS += "python3"
DEPENDS += "libdrm wayland expat"
RDEPENDS_${PN} += "bash"
RDEPENDS_${PN} += "wayland expat"

S = "${WORKDIR}/git"

do_install () {
    oe_runmake install DESTDIR=${D} TARGET_PRODUCT=${PVR_SOC}  WINDOW_SYSTEM=${PVR_WS}
    chown -R root:root ${D}
    chmod -x ${D}/usr/local/bin/*.py
    chmod -x ${D}/usr/local/bin/pvrlogsplit
}

FILES_${PN} += " ${base_libdir}/firmware/"
FILES_${PN} += " ${datadir}/ /usr/bin /usr/include /usr/local/*"

PACKAGES =+ "${PN}-plugins"
FILES_${PN}-plugins = "${libdir}/libGLESv2.so ${libdir}/libGLESv1_CM.so ${libdir}/libEGL.so ${libdir}/dri/pvr_dri.so"
RDEPENDS_${PN} += "${PN}-plugins"
PRIVATE_LIBS_${PN} = "libdrm.so.2 libz.so.1 libwayland-client.so.0 libwayland-egl.so.1 libwayland-cursor.so.0 libwayland-server.so.0 libexpat.so.1 \
               libxml2.so.2 libkms.so.1  libffi.so.7 libpng16.so.16"

ALLOW_EMPTY_${PN}-plugins = "1"

INSANE_SKIP_${PN} += "ldflags arch already-stripped file-rdeps dev-so staticdev"
INSANE_SKIP_${PN}-plugins = "dev-so"

CLEANBROKEN = "1"
