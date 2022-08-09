SECTION = "kernel"
LICENCE = "GPLv2"

require recipes-kernel/linux/linux-yocto.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

LINUX_VERSION_EXTENSION = "-xeno"

PV = "${LINUX_VERSION}+git${SRCPV}"

# Xenomai source (prepare_kernel.sh script)
SRC_URI += "http://xenomai.org/downloads/xenomai/stable/${XENOMAI_SRC}.tar.bz2;name=xeno"
SRC_URI += "file://defconfig"
SRC_URI += "file://${IPIPE_PATCH};apply=0"

INITRAMFS_IMAGE = "core-image-minimal-initramfs"
INITRAMFS_IMAGE_BUNDLE = "1"

do_prepare_kernel () {
    # Set linux kernel source directory
    linux_src="${S}"

    # Set xenomai source directory 

    # Set ipipe patch (adapted for Pi 3)

    # Prepare kernel
    ${WORKDIR}/${XENOMAI_SRC}/scripts/prepare-kernel.sh --arch=${ARCH} --linux=${linux_src} --ipipe="${WORKDIR}/${IPIPE_PATCH}" --default
}

addtask prepare_kernel after do_patch before do_configure
