inherit phy-utils

ROOTFS_POSTPROCESS_COMMAND += "rootfs_copy_core_image;rootfs_copy_initrd;"
  
do_rootfs[depends] += "phytium-image-initramfs:do_image_complete"

