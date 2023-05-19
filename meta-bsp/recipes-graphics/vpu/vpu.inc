SUMMARY = "vpu binary static library"
DESCRIPTION = "vpu binary static library"
HOMEPAGE = "https://gitee.com/phytium_embedded/vpu-lib"
SECTION = "multimedia"
LICENSE = "PSLA"

LIC_FILES_CHKSUM = "file://LICENSE;md5=91f9c930eb49a8a38abed6fd1e182588"
SRCBRANCH ?= "master"

SRC_URI = "git://git@gitee.com/phytium_embedded/vpu-lib.git;branch=${SRCBRANCH};protocol=https"

SRCREV = "1c5de03cadb170d7389ae43bc297592f047f865e"

S = "${WORKDIR}/git"

INSANE_SKIP:${PN} += "ldflags dev-elf dev-deps file-rdeps"
CLEANBROKEN = "1"
