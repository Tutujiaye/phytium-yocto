SUMMARY = "Gst-Shark Tracers"
DESCRIPTION = "Benchmarks and profiling tools for GStreamer"
HOMEPAGE = "https://developer.ridgerun.com/wiki/index.php?title=GstShark"
SECTION = "multimedia"
LICENSE = "GPLv2+"

LIC_FILES_CHKSUM = "file://CMakeLists.txt;md5=092ff0b0126eaf5bcf07f6ae835803c0"

#SRCBRANCH ?= "master"

#SRC_URI = "git://gitlab.phytium.com.cn/embedded/vpu.git;branch=${SRCBRANCH};protocol=http"

SRCBRANCH ?= "main"

SRC_URI = "git://git@gitlab.phytium.com.cn:12022/embedded/vpu.git;branch=${SRCBRANCH};protocol=ssh"


SRCREV = "${AUTOREV}"

DEPENDS = "gstreamer1.0 gstreamer1.0-plugins-base gstreamer1.0-plugins-bad "
DEPENDS += "virtual/kernel"

inherit module cmake pkgconfig

S = "${WORKDIR}/git"

OECMAKE_GENERATOR = "Unix Makefiles"

EXTRA_OECMAKE = "-DPLATFORM=img_fpga -DENABLE_3_9_CONFIG_ALL=TRUE -DLINUX_KERNEL_BUILD_DIR=${STAGING_KERNEL_DIR} -DBUILD_DECODER=ON  -DPVDEC_REG_FW_UPLOAD=ON -DOMX_CLIENT=ON  -DCMAKE_BUILD_TYPE=Debug -DDMABUF_BUFFERS=ON -DENABLE_COMPLETE_LOGGING=ON -DOMX_CLIENT=ON -DLOG_MBOX_MESSAGES=1"

do_compile_prepend () {
    cd ${S}/linux
    #EXTRA_OEMAKE += 'KERNEL_SRC="${STAGING_KERNEL_DIR}"'
    export KERNEL_SRC="${STAGING_KERNEL_DIR}"
}

do_install_append () {
        mkdir -p ${D}/usr/lib
        mkdir -p ${D}/usr/lib/gstreamer-1.0
        mkdir -p ${D}/lib/firmware/
        mkdir -p ${D}${sysconfdir}/xdg
        cp  ${WORKDIR}/build/imgvideo/examples/linux/gst-omx/libGST-OMX.so ${D}/usr/lib/gstreamer-1.0/libgstomx.so
        install -m 0544   ${S}/imgvideo/examples/linux/gst-omx/gstomx.conf ${D}${sysconfdir}/xdg/
        cp  ${WORKDIR}/build/debug/*.so  ${D}/usr/lib
        cp  ${S}/decoder/vdec/firmware/bin/pvdec_full_bin.fw  ${D}/lib/firmware/
}

FILES_${PN} += " /usr/lib/* /usr/km/* /lib/firmware/* /etc/xdg/*"
INSANE_SKIP_${PN}-dev += "dev-elf"
