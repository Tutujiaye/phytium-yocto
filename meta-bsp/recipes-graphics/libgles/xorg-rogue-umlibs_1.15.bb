DESCRIPTION = "Userspace libraries for PowerVR Rogue GPU in Phytium SoCs"
HOMEPAGE = "https://gitee.com/phytium_embedded/phytium-rogue-umlibs.git"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://README.md;md5=d368237c2530f972311f84ee015a42b7"

inherit features_check

REQUIRED_MACHINE_FEATURES = "gpu"

SRC_URI = "git://git@gitee.com/phytium_embedded/phytium-rogue-umlibs.git;branch=${BRANCH};protocol=https"

PACKAGE_ARCH = "${MACHINE_ARCH}"
PACKAGES += "${PN}-firmware"

IMAGE_FSTYPES += "wic"
BRANCH = "master"

SRCREV = "2677e2eed06c92b06b77cf18aae5fec739cd9a2e"

PVR_SOC ?= "phytium-linux"
PVR_WS = "xorg"

INITSCRIPT_NAME = "rc.pvr"
INITSCRIPT_PARAMS = "defaults 8"

DEPENDS += "python3"
S = "${WORKDIR}/git"

do_install () {
    oe_runmake install DESTDIR=${D} TARGET_PRODUCT=${PVR_SOC}  WINDOW_SYSTEM=${PVR_WS}
    chown -R root:root ${D}
    chmod -x ${D}/usr/local/bin/*.py
    chmod -x ${D}/usr/local/bin/pvrlogsplit
    mkdir -p ${D}/usr/local/var/log
}

FILES_${PN}-firmware = "${base_libdir}/firmware/*"
FILES_${PN} += " ${datadir}/ /usr/local/* /usr/lib/*"
FILES_${PN}-staticdev += "/usr/local/lib/*.a"

PRIVATE_LIBS_${PN} = "libz.so.1 libcrypto.so.1.1 libssl.so.1.1 libdrm.so.2 libexpat.so.1 libpciaccess.so.0 libkms.so.1"

INSANE_SKIP_${PN} += "ldflags arch already-stripped file-rdeps dev-so staticdev  dev-elf"
INSANE_SKIP_${PN}-plugins = "dev-so file-rdeps"
INSANE_SKIP_${PN}-dev += "dev-elf"

CLEANBROKEN = "1"
