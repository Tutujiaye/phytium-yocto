SUMMARY = "UnixBench is the original BYTE UNIX benchmark suite"
HOMEPAGE = "https://github.com/kdlucas/byte-unixbench"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=b234ee4d69f5fce4486a80fdaf4a4263"
#SRC_URI = "git://github.com/kdlucas/byte-unixbench.git"
#SRCREV = "e477bc034137f994f2bbaba52952ca6e1de53856"
#S = "${WORKDIR}/git"

SRC_URI = "https://github.com/kdlucas/byte-unixbench/archive/refs/heads/master.zip;downloadfilename=byte-unixbench-${PV}.zip"
SRC_URI[md5sum] = "7f0e19328f04bf3e60668297a5622171"
SRC_URI[sha256sum] = "0c4e5a553b24ffb65927d0fafb70ecac48a4c4f8305902afbd9b7a97d3282ebc"
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
