do_install:append_class-target() {
        install -d ${D}${EFI_FILES_PATH}
        mv ${D}${EFI_FILES_PATH}/${GRUB_IMAGE} ${D}${EFI_FILES_PATH}/${EFI_BOOT_IMAGE}
}

FILES_${PN} += "${EFI_FILES_PATH}/bootaa64.efi"
require ${@oe.utils.conditional('TCMODE', 'external-arm', 'grub-external-arm.inc', '', d)}
