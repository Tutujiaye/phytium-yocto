#add the below env in local.conf to build eglfs
#DISTRO_FEATURES_remove = " x11 wayland"
#PREFERRED_PROVIDER_virtual/egl      ?= "nullwm-rogue-umlibs"
#PREFERRED_PROVIDER_virtual/libglapi  ?= "nullwm-rogue-umlibs"
#PREFERRED_PROVIDER_virtual/libgl  ?= "nullwm-rogue-umlibs"
#PREFERRED_PROVIDER_virtual/libgles1  ?= "nullwm-rogue-umlibs"
#PREFERRED_PROVIDER_virtual/libgles2  ?= "nullwm-rogue-umlibs"
#PREFERRED_PROVIDER_virtual/libgbm  ?= "nullwm-rogue-umlibs"
#PREFERRED_PROVIDER_virtual/libdrm  = "nullwm-rogue-umlibs"

require phytium-image-networking.bb

PACKAGE_ARCH = "${MACHINE_ARCH}"

IMAGE_FEATURES += "${@bb.utils.contains('DISTRO_FEATURES', 'x11', \
        'splash package-management x11-base x11-sato','', d)} \
"

IMAGE_FSTYPES = "tar.gz ext2.gz ext2.gz.u-boot"

inherit core-image

SUMMARY = "Small qt5 image to be used for evaluating the Phytium SOC"
DESCRIPTION = "Small image which includes some helpful tools and \
packages."

LICENSE = "MIT"

IMAGE_INSTALL_GPU =" nullwm-rogue-umlibs"

IMAGE_INSTALL_append = " \
    packagegroup-qt5-phytium \
    alsa-utils-alsactl \
    alsa-utils-alsamixer \
    packagegroup-core-tools-debug \
    packagegroup-core-full-cmdline \
    packagegroup-core-tools-testapps \
    packagegroup-qt5-demos \
    ${@bb.utils.contains('MACHINE_FEATURES', 'gpu', '${IMAGE_INSTALL_GPU}', '', d)} \
"
IMAGE_INSTALL_GPU =" nullwm-rogue-umlibs"

RDEPENDS_${PN} = "${@bb.utils.contains('DISTRO_FEATURES', 'x11', 'glmark2 qt5-creator xserver-nodm-init ', '', d)} "

