IMAGE_FSTYPES:append = " ext4"
IMAGE_FSTYPES:remove = " cpio.gz cpio cpio.gz.u-boot cpio.bz2"

CUSTOMIZE_LOGOS ??= "phytium-compat-logos"

do_image_complete[depends] = "phy-image-anaconda-initramfs:do_image_complete"

build_iso:prepend() {
        install -d ${ISODIR}/dtb
        for DTB in ${DEF_DEVICETREE}; do
                cp ${DEPLOY_DIR_IMAGE}/${DTB} ${ISODIR}/dtb/
        done

}

INITRD_IMAGE_LIVE = "${MLPREFIX}phy-image-anaconda-initramfs"
