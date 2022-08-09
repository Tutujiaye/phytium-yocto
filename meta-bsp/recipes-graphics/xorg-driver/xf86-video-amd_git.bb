SUMMARY = "X.Org X server -- AMD graphics chipsets driver"

DESCRIPTION = "xf86-video-amd is an Xorg driver for AMD integrated	\
graphics chipsets. The driver supports depths 8, 15, 16 and 24. On	\
some chipsets, the driver supports hardware accelerated 3D via the	\
Direct Rendering Infrastructure (DRI)."

require recipes-graphics/xorg-driver/xorg-driver-video.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=aabff1606551f9461ccf567739af63dc"

DEPENDS += "virtual/libx11 drm xorgproto \
	    virtual/libgl libpciaccess \
"

PACKAGECONFIG[udev] = "--enable-udev,--disable-udev,udev"
PACKAGECONFIG[glamor] = "--enable-glamor,--disable-glamor"

SRC_URI = "git://anongit.freedesktop.org/xorg/driver/xf86-video-amdgpu"
SRCREV = "0d68a91dce88eeacd15bf1159ddc6200a01b1f2e"
PV  = "amdgpu-20.0.0"
PACKAGECONFIG_append  = " udev glamor"

PV = "git${SRCPV}"

S = "${WORKDIR}/git"

PACKAGES += "${PN}-conf"
FILES_${PN}-conf = "${datadir}/X11"
