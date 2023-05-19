# DISTRO_FEATURES:remove = " x11"

IMAGE_INSTALL_GPU =" wayland-rogue-umlibs wayland-rogue-umlibs-dev"
IMAGE_INSTALL_VPU =" vpu-firmware vpu-lib vpu-lib-dev"

IMAGE_INSTALL:append =" packagegroup-qt5-demos \
     packagegroup-qt5-phytium \
     packagegroup-phy-gstreamer1.0 \
     packagegroup-phy-gstreamer1.0-full \
     gstreamer1.0-plugins-base-meta \
     gstreamer1.0-plugins-good-meta \
     gstreamer1.0-plugins-bad-meta  \
     alsa-utils-alsactl alsa-utils-alsamixer \
     alsa-utils-amixer alsa-utils-aplay \
     ${@bb.utils.contains('MACHINE_FEATURES', 'gpu', '${IMAGE_INSTALL_GPU}', '', d)} \
     ${IMAGE_INSTALL_VPU} \
     systemd \
"
inherit phy-utils

ROOTFS_POSTPROCESS_COMMAND += "${@bb.utils.contains('MACHINE_FEATURES', 'gpu', 'rootfs_ln_wayland;', '', d)}"
