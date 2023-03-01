SUMMARY = "CoreMark's primary goals are simplicity and providing a method for testing only a processor's core features."
HOMEPAGE = "https://github.com/eembc/coremark"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=0a18b17ae63deaa8a595035f668aebe1"
#SRC_URI = "git://github.com/eembc/coremark.git;protocol=https;branch=main"
#SRCREV = "${AUTOREV}"
#S = "${WORKDIR}/git"

SRC_URI = "https://github.com/eembc/coremark/archive/refs/heads/main.zip;downloadfilename=${BPN}-${PV}.zip"
SRC_URI[md5sum] = "48c3ff241018dbdbc790d5833e662679"
SRC_URI[sha256sum] = "e8f070f2358bb4b20f3b8b88215986717538e4d32e794bb166eda58d6fa516f4"

S = "${WORKDIR}/coremark-main"

EXTRA_OEMAKE = "./coremark PORT_DIR='linux' CC='${CC}' EXE=''"

do_install(){
	install -d ${D}${bindir}
	install -m 0755 ${S}/coremark ${D}${bindir}
}

INSANE_SKIP_${PN} = "ldflags"
