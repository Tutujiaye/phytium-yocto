GPU_UMLIBS = "git://git@gitee.com/phytium_embedded/phytium-rogue-umlibs.git;branch=master;protocol=https;destsuffix=git/;name=umlibs"

SRC_URI_append = " ${@bb.utils.contains('MACHINE_FEATURES', 'gpu', '${GPU_UMLIBS}', '', d)}"
SRCREV_umlibs = "${AUTOREV}"

PVR_SOC ?= "phytium-linux"

BACKEND = \
    "${@bb.utils.contains('DISTRO_FEATURES', 'x11 wayland', 'xorg',\
        bb.utils.contains('DISTRO_FEATURES',     'wayland', 'wayland', \
                                                             'nulldrmws', d), d)}"
PVR_WS = "${BACKEND}"

do_install_class_target_append () {
    if ${@bb.utils.contains('MACHINE_FEATURES','gpu','true','false',d)}; then
        install -m 0644 ${WORKDIR}/git/targetfs/${PVR_SOC}/${PVR_WS}/usr/local/lib/libdrm.so.2.4.0 ${D}${libdir}
    fi
}
