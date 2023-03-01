SUMMARY = "UnixBench is the original BYTE UNIX benchmark suite"
HOMEPAGE = "https://github.com/kdlucas/byte-unixbench"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=b234ee4d69f5fce4486a80fdaf4a4263"
#SRC_URI = "git://github.com/kdlucas/byte-unixbench.git"
#SRCREV = "e477bc034137f994f2bbaba52952ca6e1de53856"
#S = "${WORKDIR}/git"

SRC_URI = "https://github.com/kdlucas/byte-unixbench/archive/refs/heads/master.zip;downloadfilename=byte-unixbench-${PV}.zip"
SRC_URI[md5sum] = "4eac102bd3655a97d73626b54db2b1f2"
SRC_URI[sha256sum] = "292c31eade39c3f5eb247cf8fd5733917700e21b455183a9a03c550c24ffce05"
S = "${WORKDIR}/byte-unixbench-master"
B = "${S}/UnixBench"

RDEPENDS_${PN} += "perl perl-modules"

EXTRA_OEMAKE = "CC='${CC}' UB_GCC_OPTIONS='${CFLAGS}'"

do_install(){
	install -d ${D}/opt/unixbench
	cp -r ${B}/* ${D}/opt/unixbench
}

PACKAGES = "${PN} ${PN}-dbg"
FILES_${PN} = "/opt/unixbench/"

INSANE_SKIP_${PN} = "ldflags"
