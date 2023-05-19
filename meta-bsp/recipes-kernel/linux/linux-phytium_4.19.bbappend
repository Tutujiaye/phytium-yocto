FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

XEN_PATCHES = " file://xen.cfg" 
SRC_URI:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'xen', '${XEN_PATCHES}', '', d)}"

DELTA_KERNEL_DEFCONFIG = "${@bb.utils.contains('DISTRO_FEATURES', 'xen', 'xen.cfg', '', d)}"
