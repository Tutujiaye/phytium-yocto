rootfs_copy_core_image() {
    mkdir -p ${IMAGE_ROOTFS}/boot
    for DTB in ${DEF_DEVICETREE}; do
    	cp ${DEPLOY_DIR_IMAGE}/${DTB} ${IMAGE_ROOTFS}/boot/
    done
}
rootfs_copy_core_image_lxqt() {
    mkdir -p ${IMAGE_ROOTFS}/boot
    mkdir -p ${IMAGE_ROOTFS}/copy
    cp ${DEPLOY_DIR_IMAGE}/Image ${IMAGE_ROOTFS}/boot/
    cp ${DEPLOY_DIR_IMAGE}/${DEF_DEVICETREE} ${IMAGE_ROOTFS}/boot/
    cp ${DEPLOY_DIR_IMAGE}/initrd.img ${IMAGE_ROOTFS}/boot/
    cp ${DEPLOY_DIR_IMAGE}/phytium-image-lxqt-${MACHINE}.tar.gz ${IMAGE_ROOTFS}/copy/
}

rootfs_copy_core_image_ubuntu() {
    mkdir -p ${IMAGE_ROOTFS}/boot
    mkdir -p ${IMAGE_ROOTFS}/copy
    cp ${DEPLOY_DIR_IMAGE}/Image ${IMAGE_ROOTFS}/boot/
    cp ${DEPLOY_DIR_IMAGE}/${DEF_DEVICETREE} ${IMAGE_ROOTFS}/boot/
    cp ${DEPLOY_DIR_IMAGE}/rootfs-ubuntu-desktop.tar.gz ${IMAGE_ROOTFS}/copy/
}

rootfs_ln_initrd() {
    cd ${DEPLOY_DIR_IMAGE}
    ln -sf  initrd.img.rootfs.cpio.gz  initramfs.img
}

rootfs_copy_initrd() {
    cd ${DEPLOY_DIR_IMAGE}
    mkdir -p ${IMAGE_ROOTFS}/boot
    cp ${DEPLOY_DIR_IMAGE}/initrd.img.rootfs.cpio.gz ${IMAGE_ROOTFS}/boot/initramfs.img
    cd ${IMAGE_ROOTFS}/boot/
    ln -sf initramfs.img initrd
}

rootfs_copy_initramfs_target() {
    rm -rf ${IMAGE_ROOTFS}/lib/systemd/system/default.target
    rm -rf ${IMAGE_ROOTFS}/lib/systemd/system/basic.target
    rm -rf ${IMAGE_ROOTFS}/lib/systemd/system/tmp.mount
    rm -rf ${IMAGE_ROOTFS}/etc/systemd/system/*
    cp ${IMAGE_ROOTFS}/lib/systemd/system/initrd.target ${IMAGE_ROOTFS}/lib/systemd/system/default.target
    cp ${IMAGE_ROOTFS}/etc/basic.targetconf ${IMAGE_ROOTFS}/lib/systemd/system/basic.target
}

rootfs_ln_xorg() {
   echo "export LD_LIBRARY_PATH=\${LD_LIBRARY_PATH}:/usr/local/lib" >> ${IMAGE_ROOTFS}/etc/profile
   rm -rf ${IMAGE_ROOTFS}/usr/bin/Xorg
   cd ${IMAGE_ROOTFS}/usr/local/bin
   ln -s /usr/local/bin/Xorg ${IMAGE_ROOTFS}/usr/bin/Xorg
}

rootfs_ln_xorg1() {
   echo "export LD_LIBRARY_PATH=\${LD_LIBRARY_PATH}:/usr/local/lib" >> ${IMAGE_ROOTFS}/etc/profile
   echo "DefaultEnvironment=\"LD_LIBRARY_PATH=\${LD_LIBRARY_PATH}:/usr/local/lib\"" >> ${IMAGE_ROOTFS}/etc/systemd/system.conf
}

rootfs_ln_wayland() {
   echo "export LD_LIBRARY_PATH=\${LD_LIBRARY_PATH}:/usr/local/lib" >> ${IMAGE_ROOTFS}/etc/profile
   rm -rf ${IMAGE_ROOTFS}/usr/bin/weston
   cd ${IMAGE_ROOTFS}/usr/local/bin
   ln -s /usr/local/bin/weston ${IMAGE_ROOTFS}/usr/bin/weston
}

rootfs_copy_xen_image() {
    mkdir -p ${IMAGE_ROOTFS}/boot
    cp ${DEPLOY_DIR_IMAGE}/xen-guest-image-minimal-${MACHINE}.cpio.gz ${IMAGE_ROOTFS}/boot/
    cp ${DEPLOY_DIR_IMAGE}/Image ${IMAGE_ROOTFS}/boot/
}

