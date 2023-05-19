inherit phy-utils

ROOTFS_POSTPROCESS_COMMAND += "rootfs_copy_xen_image;"

do_rootfs[depends] += "xen-guest-image-minimal:do_image_complete"

IMAGE_INSTALL:remove = "${XEN_KERNEL_MODULES}"
