DESCRIPTION = "Userspace libraries for PowerVR Rogue GPU in Phytium SoCs"
HOMEPAGE = "https://gitee.com/phytium_embedded/phytium-rogue-umlibs.git"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://README.md;md5=d368237c2530f972311f84ee015a42b7"

inherit features_check

REQUIRED_MACHINE_FEATURES = "gpu"

PACKAGE_ARCH = "${MACHINE_ARCH}"

BRANCH = "master"

SRC_URI = "git://git@gitee.com:22/phytium_embedded/phytium-rogue-umlibs.git;branch=${BRANCH};protocol=ssh"

SRC_URI += "file://0001-pc-update.patch"
SRCREV = "${AUTOREV}"

PVR_SOC ?= "phytium-linux"
PVR_WS = "nulldrmws"

INITSCRIPT_NAME = "rc.pvr"
INITSCRIPT_PARAMS = "defaults 8"

inherit update-rc.d

DEPENDS += "python3"
DEPENDS += "expat"
RDEPENDS_${PN} += "bash"

PROVIDES += "virtual/egl virtual/libgl virtual/libglapi virtual/libgbm virtual/libgles1 virtual/libgles2"

S = "${WORKDIR}/git"

do_install () {
    oe_runmake install DESTDIR=${D} TARGET_PRODUCT=${PVR_SOC}  WINDOW_SYSTEM=${PVR_WS}
    chown -R root:root ${D}
    chmod -x ${D}/usr/local/bin/*.py
    chmod -x ${D}/usr/local/bin/pvrlogsplit
    mkdir -p ${D}/usr/lib/pkgconfig
    mkdir -p ${D}${includedir}
    mkdir -p ${D}/usr/local/lib/dri/
    cp ${S}/targetfs/${PVR_SOC}/${PVR_WS}/usr/local/lib/libEGL.so.1.0.0  ${D}${libdir}
    cp ${S}/targetfs/${PVR_SOC}/${PVR_WS}/usr/local/lib/libGLESv2.so.2.0.0  ${D}${libdir}
    cp ${S}/targetfs/${PVR_SOC}/${PVR_WS}/usr/local/lib/libGL.so.1.2.0  ${D}${libdir}
    cp ${S}/targetfs/${PVR_SOC}/${PVR_WS}/usr/local/lib/libGLESv1_CM.so.1.1.0  ${D}${libdir}
    cp ${S}/targetfs/${PVR_SOC}/${PVR_WS}/usr/local/lib/libglapi.so.0.0.0  ${D}${libdir}
    cp ${S}/targetfs/${PVR_SOC}/${PVR_WS}/usr/local/lib/libgbm.so.1.0.0  ${D}${libdir}

    install -m 0644 ${S}/targetfs/${PVR_SOC}/${PVR_WS}/usr/local/lib/pkgconfig/dri.pc ${D}${libdir}/pkgconfig/dri.pc
    install -m 0644 ${S}/targetfs/${PVR_SOC}/${PVR_WS}/usr/local/lib/pkgconfig/egl.pc ${D}${libdir}/pkgconfig/egl.pc
    install -m 0644 ${S}/targetfs/${PVR_SOC}/${PVR_WS}/usr/local/lib/pkgconfig/glesv2.pc ${D}${libdir}/pkgconfig/glesv2.pc
    install -m 0644 ${S}/targetfs/${PVR_SOC}/${PVR_WS}/usr/local/lib/pkgconfig/gbm.pc ${D}${libdir}/pkgconfig/gbm.pc
    install -m 0644 ${S}/targetfs/${PVR_SOC}/${PVR_WS}/usr/local/lib/dri/*  ${D}/usr/local/lib/dri/
    install -m 0644 ${S}/targetfs/${PVR_SOC}/${PVR_WS}/usr/local/lib/pkgconfig/glesv1_cm.pc ${D}${libdir}/pkgconfig/glesv1_cm.pc

    cp -r ${S}/targetfs/${PVR_SOC}/${PVR_WS}/usr/local/include/* ${D}${includedir}
    
    find ${D}${libdir} -type f -exec chmod 644 {} \;
    find ${D}${includedir} -type f -exec chmod 644 {} \;
    
    rm -rf ${D}/usr/local/lib/lib*
    rm -rf ${D}/usr/include/zlib.h
    rm -rf ${D}/usr/include/libsync.h
    rm -rf ${D}/usr/include/zconf.h
    rm -rf ${D}/usr/include/expat*.h
    rm -rf ${D}/usr/include/pciaccess.h
    rm -rf ${D}/usr/lib/libpciaccess.so.0.11.1
    rm -rf ${D}/usr/lib/libexpat*
    rm -rf ${D}/usr/lib/libz.a
    rm -rf ${D}/usr/lib/pkgconfig/zlib.pc 
    rm -rf ${D}/usr/lib/libz.so.1.2.11
    rm -rf ${D}/usr/lib/pkgconfig/pciaccess.pc
    rm -rf ${D}/usr/share/util-macros
    rm -rf ${D}/usr/share/pkgconfig/xorg-macros.pc
    rm -rf ${D}/usr/share/aclocal/xorg-macros.m4
    rm -rf ${D}/usr/lib/pkgconfig/expat.pc
    rm -rf ${D}/usr/lib/pkgconfig/pthread-stubs.pc
    rm -rf ${D}/usr/include/xf86drmMode.h
    rm -rf ${D}/usr/include/xf86drm.h
    rm -rf ${D}/usr/include/libdrm
    rm -rf ${D}/usr/include/libkms
    ln -sf libGL.so.1.2.0 ${D}${libdir}/libGL.so.1.2
    ln -sf libGL.so.1.2.0 ${D}${libdir}/libGL.so.1
    ln -sf libGL.so.1.2.0 ${D}${libdir}/libGL.so
    ln -sf libglapi.so.0.0.0 ${D}${libdir}/libglapi.so
    ln -sf libglapi.so.0.0.0 ${D}${libdir}/libglapi.so.0
    ln -sf libEGL.so.1.0.0 ${D}${libdir}/libEGL.so.1
    ln -sf libEGL.so.1.0.0 ${D}${libdir}/libEGL.so
    ln -sf libgbm.so.1.0.0 ${D}${libdir}/libgbm.so
    ln -sf libgbm.so.1.0.0 ${D}${libdir}/libgbm.so.1
    rm -rf ${D}/usr/lib/libdrm.so.2.4.0
    ln -sf libGLESv2.so.2.0.0 ${D}${libdir}/libGLESv2.so.2
    ln -sf libGLESv2.so.2.0.0 ${D}${libdir}/libGLESv2.so
    ln -sf libGLESv1_CM_PVR_MESA.so.1.15.5986747 ${D}${libdir}/libGLESv1_CM_PVR_MESA.so
    ln -sf libGLESv2_PVR_MESA.so.1.15.5986747  ${D}${libdir}/libGLESv2_PVR_MESA.so
    ln -sf libGL_PVR_MESA.so.1.15.5986747    ${D}${libdir}/libGL_PVR_MESA.so
    ln -sf libpvr_dri_support.so.1.15.5986747  ${D}${libdir}/libpvr_dri_support.so
    ln -sf libPVRScopeServices.so.1.15.5986747  ${D}${libdir}/libPVRScopeServices.so
    ln -sf libsutu_display.so.1.15.5986747  ${D}${libdir}/libsutu_display.so
    ln -sf libusc.so.1.15.5986747    ${D}${libdir}/libusc.so
    ln -sf libsrv_um.so.1.15.5986747   ${D}${libdir}/libsrv_um.so
    ln -sf libufwriter.so.1.15.5986747  ${D}${libdir}/libufwriter.so
}

FILES_${PN} += " ${base_libdir}/firmware/"
FILES_${PN} += " ${datadir}/ /usr/bin /usr/include /usr/local/*"

PACKAGES =+ "${PN}-plugins"
FILES_${PN}-plugins = "${libdir}/libGLESv2.so ${libdir}/libGLESv1_CM.so ${libdir}/libEGL.so /usr/local/lib/dri/pvr_dri.so"
RDEPENDS_${PN} += "${PN}-plugins"

ALLOW_EMPTY_${PN}-plugins = "1"

INSANE_SKIP_${PN} += "ldflags arch already-stripped file-rdeps dev-so staticdev"
INSANE_SKIP_${PN}-plugins = "dev-so"
CLEANBROKEN = "1"
