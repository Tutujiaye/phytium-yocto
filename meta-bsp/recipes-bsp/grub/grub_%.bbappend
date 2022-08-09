FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += "file://0001-10_linux.in-update-Image-for-aarch64.patch \   
           file://0001-10linux.in-udpate-for-initrd.img.patch \
"

EXTRA_OECONF_append= " --with-bootdir=boot/efi/EFI/BOOT"

require ${@oe.utils.conditional('TCMODE', 'external-arm', 'grub-external-arm.inc', '', d)}
