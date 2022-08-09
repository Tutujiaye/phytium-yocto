FILESEXTRAPATHS_prepend := "${LAYER_PATH_meta-anaconda}/recipes-kernel/linux/files/:"
SRC_URI_append = " \
    file://dmthin.scc \
    file://crypt.scc \
"

KERNEL_FEATURES_append = " cfg/systemd.scc"

