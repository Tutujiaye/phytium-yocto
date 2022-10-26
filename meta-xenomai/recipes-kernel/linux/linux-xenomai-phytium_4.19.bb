require linux-phytium.inc
inherit kernel siteinfo
inherit phy-kernel-localversion

SUMMARY = "Linux Kernel for Phytium platforms"
SECTION = "kernel"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"

LINUX_VERSION_EXTENSION = "-xeno"

XENOMAI_SRC = "xenomai-v3.1.3"

# Xenomai source (prepare_kernel.sh script)
SRC_URI += "https://source.denx.de/Xenomai/xenomai/-/archive/v3.1.3/${XENOMAI_SRC}.tar.bz2;name=xeno"

SRC_URI[xeno.md5sum] = "38ba82b70180c2c7a95cdae1767c6de2"

do_prepare_kernel () {
    # Set linux kernel source directory
    linux_src="${S}"

    # Set xenomai source directory

    # Set ipipe patch (adapted for Pi 3)

    # Prepare kernel
    ${WORKDIR}/${XENOMAI_SRC}/scripts/prepare-kernel.sh --arch=${ARCH} --linux=${linux_src} --ipipe="${WORKDIR}/${IPIPE_PATCH}" --default
}

addtask prepare_kernel after do_patch before do_configure


S = "${WORKDIR}/git"

DEPENDS_append = " libgcc u-boot-tools-native"
# not put Images into /boot of rootfs, install kernel-image if needed
RDEPENDS_${KERNEL_PACKAGE_NAME}-base = ""

KERNEL_CC_append = " ${TOOLCHAIN_OPTIONS}"
KERNEL_LD_append = " ${TOOLCHAIN_OPTIONS}"
KERNEL_EXTRA_ARGS += "LOADADDR=${UBOOT_ENTRYPOINT}"

FILES_${KERNEL_PACKAGE_NAME}-base += " ${nonarch_base_libdir}/modules/${KERNEL_VERSION}/modules.builtin.modinfo "

KERNEL_CONFIG_COMMAND = "oe_runmake_call -C ${S} CC="${KERNEL_CC}" O=${B} olddefconfig || oe_runmake -C ${S} O=${B} CC="${KERNEL_CC}" oldnoconfig"

ZIMAGE_BASE_NAME = "zImage-${PKGE}-${PKGV}-${PKGR}-${MACHINE}-${DATETIME}"
ZIMAGE_BASE_NAME[vardepsexclude] = "DATETIME"

SCMVERSION ?= "y"
LOCALVERSION = ""
DELTA_KERNEL_DEFCONFIG ?= ""
DELTA_KERNEL_DEFCONFIG_prepend = "cobalt.config"

do_merge_delta_config[dirs] = "${B}"

do_merge_delta_config() {
    # create config with make config
    oe_runmake  -C ${S} O=${B} ${KERNEL_DEFCONFIG}
    # add config fragments
    for deltacfg in ${DELTA_KERNEL_DEFCONFIG}; do
        if [ -f ${S}/arch/${ARCH}/configs/${deltacfg} ]; then
            oe_runmake  -C ${S} O=${B} ${deltacfg}
        elif [ -f "${WORKDIR}/${deltacfg}" ]; then
            ${S}/scripts/kconfig/merge_config.sh -m .config ${WORKDIR}/${deltacfg}
        elif [ -f "${deltacfg}" ]; then
            ${S}/scripts/kconfig/merge_config.sh -m .config ${deltacfg}
        fi
    done
    cp .config ${WORKDIR}/defconfig
}

do_uboot_mkimage() {
   uboot-mkimage -A arm64 -O linux -T kernel -C none -a 0x80080000 -e 0x80080000  -n "4.19" -d ${DEPLOY_DIR_IMAGE}/Image ${DEPLOY_DIR_IMAGE}/uImage
}
addtask merge_delta_config before do_preconfigure after do_patch do_prepare_recipe_sysroot
addtask uboot_mkimage before do_package_write_rpm after do_deploy 

FILES_${KERNEL_PACKAGE_NAME}-image += "/boot/zImage*"
