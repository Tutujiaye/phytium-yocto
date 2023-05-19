FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += "file://isolinux.bin"

do_install() {
  :
}

do_install:class-target() {
     install -d ${D}${datadir}/syslinux/
     install -m 644 ${WORKDIR}/isolinux.bin ${D}${datadir}/syslinux/
}

do_install:class-native() {
        oe_runmake CC="${CC} ${CFLAGS}" LD="${LD}" \
                   OBJDUMP="${OBJDUMP}" \
                   OBJCOPY="${OBJCOPY}" \
                   AR="${AR}" \
                   STRIP="${STRIP}" \
                   NM="${NM}" \
                   RANLIB="${RANLIB}" \
                   firmware="bios" install INSTALLROOT="${D}"

        install -d ${D}${datadir}/syslinux/
        install -m 644 ${S}/bios/core/ldlinux.sys ${D}${datadir}/syslinux/
        install -m 644 ${S}/bios/core/ldlinux.bss ${D}${datadir}/syslinux/
        install -m 755 ${S}/bios/linux/syslinux-nomtools ${D}${bindir}/
}

COMPATIBLE_HOST:class-target  = '(x86_64|i.86|aarch64).*-(linux|freebsd.*)'
