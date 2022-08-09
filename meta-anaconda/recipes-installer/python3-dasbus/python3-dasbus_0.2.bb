SUMMARY = "Dasbus is a DBus library written in Python 3, based on GLib and inspired by pydbus."
HOMEPAGE = "https://dasbus.readthedocs.io"
SECTION = "devel"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1803fa9c2c3ce8cb06b4861d75310742"

SRC_URI = "git://github.com/rhinstaller/dasbus.git;protocol=https;"
SRCREV = "ede66cfebf5bdc17145cac9a27bba4ca4301351b"
S = "${WORKDIR}/git"

inherit setuptools3

RDEPENDS_${PN} = "python3-pygobject"
