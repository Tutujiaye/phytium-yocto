FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += " \
	file://grub.cfg \
	"

do_install_append() {
        install -d ${D}${EFI_FILES_PATH}
        install grub.cfg ${D}${EFI_FILES_PATH}/grub.cfg
}

