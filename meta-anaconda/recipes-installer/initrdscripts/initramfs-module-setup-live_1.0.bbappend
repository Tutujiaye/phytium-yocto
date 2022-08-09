FILESEXTRAPATHS_append_anaconda := ":${THISDIR}/files"

SRC_URI_append_anaconda = " \
    file://union-fs \
    file://realroot \
    file://umountfs \
"

do_install_append_anaconda() {
    install -m 0755 ${WORKDIR}/union-fs ${D}/init.d/91-union-fs
    install -m 0755 ${WORKDIR}/realroot ${D}/init.d/92-realroot
    install -m 0755 ${WORKDIR}/umountfs ${D}/init.d/93-umountfs
}

FILES_${PN}_append_anaconda = " \
    /init.d/91-union-fs \
    /init.d/92-realroot \
    /init.d/93-umountfs \
"
