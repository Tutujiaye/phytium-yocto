SUMMARY = "Phytium Embedded Linux Demo with LXQT"
SUMMARY_append_apalis-tk1-mainline = " (Mainline)"
DESCRIPTION = "Image with the LXQT desktop environment"

LICENSE = "MIT"

require phytium-image-networking.bb

inherit core-image

inherit phy-utils

ROOTFS_POSTPROCESS_COMMAND += "rootfs_copy_core_image;rootfs_copy_initrd;"

do_rootfs[depends] += "phytium-image-initramfs:do_image_complete"



#start of the resulting deployable tarball name
#MACHINE_NAME ?= "${MACHINE}"
#IMAGE_NAME = "${MACHINE_NAME}_${IMAGE_BASENAME}"

SYSTEMD_DEFAULT_TARGET = "graphical.target"

VIRTUAL-RUNTIME_graphical_init_manager = "sddm"

IMAGE_FEATURES += " x11 "

inherit populate_sdk_qt5

IMAGE_LINGUAS = "en-us"
#IMAGE_LINGUAS = "de-de fr-fr en-gb en-us pt-br es-es kn-in ml-in ta-in"
#ROOTFS_POSTPROCESS_COMMAND += 'install_linguas; '

ROOTFS_PKGMANAGE_PKGS ?= '${@oe.utils.conditional("ONLINE_PACKAGE_MANAGEMENT", "none", "", "${ROOTFS_PKGMANAGE}", d)}'

CONMANPKGS ?= "connman connman-client lxqt-connman-applet"

IMAGE_BROWSER = "falkon"

# this would pull in a large amount of gst-plugins, we only add a selected few
#    gstreamer1.0-plugins-base-meta
#    gstreamer1.0-plugins-good-meta
#    gstreamer1.0-plugins-bad-meta
#    gst-ffmpeg
GSTREAMER = " \
    gstreamer1.0 \
    gstreamer1.0-plugins-base \
    gstreamer1.0-plugins-base-alsa \
    gstreamer1.0-plugins-base-audioconvert \
    gstreamer1.0-plugins-base-audioresample \
    gstreamer1.0-plugins-base-audiotestsrc \
    gstreamer1.0-plugins-base-typefindfunctions \
    gstreamer1.0-plugins-base-ogg \
    gstreamer1.0-plugins-base-theora \
    gstreamer1.0-plugins-base-videotestsrc \
    gstreamer1.0-plugins-base-vorbis \
    gstreamer1.0-plugins-good-audioparsers \
    gstreamer1.0-plugins-good-autodetect \
    gstreamer1.0-plugins-good-avi \
    gstreamer1.0-plugins-good-deinterlace \
    gstreamer1.0-plugins-good-id3demux \
    gstreamer1.0-plugins-good-isomp4 \
    gstreamer1.0-plugins-good-matroska \
    gstreamer1.0-plugins-good-multifile \
    gstreamer1.0-plugins-good-rtp \
    gstreamer1.0-plugins-good-rtpmanager \
    gstreamer1.0-plugins-good-udp \
    gstreamer1.0-plugins-good-video4linux2 \
    gstreamer1.0-plugins-good-wavenc \
    gstreamer1.0-plugins-good-wavparse \
"

# check the licensing at http://doc.qt.io/qt-5/licensing.html
QT5_LIBS ?= " \
    qt3d \
    qt5-plugin-generic-vboxtouch \
    qtconnectivity \
    qtdeclarative \
    qtgraphicaleffects \
    qtimageformats \
    qtlocation \
    qtmultimedia \
    qtquickcontrols2 \
    qtquickcontrols \
    qtscript \
    qtsensors \
    qtserialport \
    qtsvg \
    qtsystems \
    qttools \
    qttranslations \
    qtwebchannel \
    qtwebkit \
    qtwebsockets \
    qtxmlpatterns \
"
QT5_LIBS_GPLv3 ?= " \
    qtcharts \
    qtdatavis3d \
    qtvirtualkeyboard \
"


IMAGE_INSTALL += " \
    eject \
    xdg-utils \
    \
    libgsf \
    libxres \
    makedevs \
    xcursor-transparent-theme \
    zeroconf \
    packagegroup-boot \
    udev-extra-rules \
    ${CONMANPKGS} \
    ${ROOTFS_PKGMANAGE_PKGS} \
    xserver-common \
    xauth \
    xhost \
    xset \
    setxkbmap \
    \
    xrdb \
    xorg-minimal-fonts xserver-xorg-utils \
    scrot \
    libxdamage libxvmc libxinerama \
    libxcursor \
    \
    bash \
    \
    ${GSTREAMER} \
    v4l-utils \
    libpcre \
    libpcreposix \
    libxcomposite \
    alsa-states \
    ${QT5_LIBS} \
    ${QT5_LIBS_GPLv3} \
    packagegroup-lxqt-base \
    unpriv-user \
    qedit \
    liberation-fonts \
    qt5-creator \
    sddm \
    packagegroup-phy-benchmark-extended \
    alsa-utils-alsactl \
    alsa-utils-alsamixer \
    linux-firmware \
    linux-firmware-radeon \
"

IMAGE_DEV_MANAGER   = "udev"
IMAGE_INIT_MANAGER  = "systemd"
IMAGE_INITSCRIPTS   = " "
IMAGE_LOGIN_MANAGER = "busybox shadow"

IMAGE_BOOT ?= "${IMAGE_INITSCRIPTS} \
               ${IMAGE_DEV_MANAGER} \
               ${IMAGE_INIT_MANAGER} \
               ${IMAGE_LOGIN_MANAGER}"

#IMAGE_INSTALL += "${IMAGE_BOOT}"
