FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

PHY_BACKEND = \
    "${@bb.utils.contains('DISTRO_FEATURES', 'x11 wayland', 'x11',\
        bb.utils.contains('DISTRO_FEATURES',     'wayland', 'wayland', \
                                                             'fb', d), d)}"

SRC_URI_append = " \
    file://qt5-${PHY_BACKEND}.sh \
"
SRC_URI_APPEND_3D_NOT_X11 = " \
    file://0001-Add-eglfs-to-GPU.patch \
    file://0002-add-elgfs-support.patch \
"

GPU_EGLFS = "${@bb.utils.contains('DISTRO_FEATURES', 'x11', '', '${SRC_URI_APPEND_3D_NOT_X11}', d)}"

SRC_URI_append = " ${@bb.utils.contains('MACHINE_FEATURES', 'gpu', '${GPU_EGLFS}', '', d)}"

# support eglfs include  x11 lib
# add below env
# DEPENDS += " libxcb xcb-util-wm xcb-util-image xcb-util-keysyms xcb-util-renderutil libxext"
# EGLFS = "-eglfs -kms -no-rpath -accessibility -make examples -compile-examples -xcb -xcb-xlib -no-bundled-xcb-xinput"
# QT_CONFIG_FLAGS = " ${EGLFS}"

PACKAGECONFIG_append = " accessibility ${PHY_EXA}"

PHY_EXA = "${@bb.utils.contains('DISTRO_FEATURES', 'x11', 'examples', '', d)}"

PACKAGECONFIG_GL = "gles2 gbm kms"
#PACKAGECONFIG_PLATFORM = " eglfs "
PACKAGECONFIG_PLATFORM          = ""
PACKAGECONFIG += "${PACKAGECONFIG_PLATFORM}"

do_install_append () {
    if ls ${D}${libdir}/pkgconfig/Qt5*.pc >/dev/null 2>&1; then
        sed -i 's,-L${STAGING_DIR_HOST}/usr/lib,,' ${D}${libdir}/pkgconfig/Qt5*.pc
    fi
    install -d ${D}${sysconfdir}/profile.d/
    install -m 0755 ${WORKDIR}/qt5-${PHY_BACKEND}.sh ${D}${sysconfdir}/profile.d/qt5.sh
    if [ "${PHY_EXA}" = "examples" ]; then
    	chmod +777 -R  ${D}/usr/share/examples
    fi
}

FILES_${PN} += "${sysconfdir}/profile.d/qt5.sh"

PARALLEL_MAKEINST = ""
PARALLEL_MAKE_task-install = "${PARALLEL_MAKEINST}"
