DESCRIPTION = "A XFCE phytium demo image."

require phytium-image-networking.bb

IMAGE_INSTALL = "packagegroup-core-boot \
    packagegroup-core-x11 \
    packagegroup-xfce-base \
    kernel-modules \
"

IMAGE_FEATURES += "package-management \
"

inherit features_check
REQUIRED_DISTRO_FEATURES = "x11"

SYSTEMD_DEFAULT_TARGET = "graphical.target"

VIRTUAL-RUNTIME_graphical_init_manager = "xserver-xfce-init"

IMAGE_LINGUAS = "en-gb en-gb.iso-8859-1 en-us en-us.iso-8859-1 zh-cn"

LICENSE = "MIT"

export IMAGE_BASENAME = "core-image-xfce"

inherit core-image

inherit phy-utils

ROOTFS_POSTPROCESS_COMMAND += "${@bb.utils.contains('MACHINE_FEATURES', 'gpu', 'rootfs_ln_xorg;', '', d)}"

ROOTFS_POSTPROCESS_COMMAND += "rootfs_copy_core_image;rootfs_copy_initrd;"

do_rootfs[depends] += "phytium-image-initramfs:do_image_complete"

PACKAGE_ARCH = "${MACHINE_ARCH}"

IMAGE_FSTYPES = "tar.gz ext2.gz ext4"

IMAGE_INSTALL_GPU =" kmscube libdrm-tests xorg-rogue-umlibs xorg-rogue-umlibs-dev systemd-gpuconfig"
IMAGE_INSTALL_VPU =" vpu-lib vpu-lib-dev"

IMAGE_INSTALL += "packagegroup-phy-virtualization \
    packagegroup-core-tools-testapps \
    glmark2 \
    xserver-xfce-init \
    xfce-phytium-settings \
    packagegroup-qt5-demos \
    openssh \
    packagegroup-phy-gstreamer1.0 \
    packagegroup-phy-gstreamer1.0-full \
    gstreamer1.0-plugins-base-meta \
    gstreamer1.0-plugins-good-meta \
    gstreamer1.0-plugins-bad-meta  \
    alsa-utils-alsactl alsa-utils-alsamixer \
    udev-extra-rules \
    gtk+3 default-locale \
    qedit evince xdg-user-dirs \
    lmsensors-sensors \
    binutils make cmake gcc-symlinks g++-symlinks cpp-symlinks pkgconfig \
    libomxil htop lsscsi lshw alsa-tools bison flex \
    cpufrequtils sysbench libkcapi libgpiod libgpiod-tools linuxptp vim git ntpdate ffmpeg \
    packagegroup-gui-base \
    ${@bb.utils.contains('MACHINE_FEATURES', 'gpu', '${IMAGE_INSTALL_GPU}', '', d)} \
    ${IMAGE_INSTALL_VPU} \
"
