SUMMARY = "CoreMark's primary goals are simplicity and providing a method for testing only a processor's core features."
HOMEPAGE = "https://github.com/eembc/coremark"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=0a18b17ae63deaa8a595035f668aebe1"
#SRC_URI = "git://github.com/eembc/coremark.git;protocol=https;branch=main"
#SRCREV = "${AUTOREV}"
#S = "${WORKDIR}/git"

SRC_URI = "https://github.com/eembc/coremark/archive/refs/heads/main.zip;downloadfilename=${PN}-${PV}.zip"
SRC_URI[md5sum] = "c45f8e30ddaa228dce49c236bea619fa"
SRC_URI[sha256sum] = "19506f966695f2df5fc3700f1ae84660735c44b85bc6dba8346344043369fe6e"

S = "${WORKDIR}/coremark-main"

EXTRA_OEMAKE = "./coremark PORT_DIR='linux' CC='${CC}' EXE=''"

do_install(){
	install -d ${D}${bindir}
	install -m 0755 ${S}/coremark ${D}${bindir}
}

INSANE_SKIP_${PN} = "ldflags"
