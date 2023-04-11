SRC_URI = "git://git@gitee.com:22/phytium_embedded/phytium-linux-kernel.git;protocol=ssh;branch=${KERNEL_BRANCH} "

inherit kernel siteinfo
inherit phy-kernel-localversion

SUMMARY = "Linux Kernel for Phytium platform"
SECTION = "kernel"
LICENSE = "GPLv2"

S = "${WORKDIR}/git"

DEPENDS_append = " libgcc u-boot-tools-native"
# Images shouldn't be placed in the '/boot' directory in the rootfs, install kernel-image if needed
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
DELTA_KERNEL_DEFCONFIG_prepend = "e2000_defconfig"

do_merge_delta_config[dirs] = "${B}"

do_merge_delta_config() {
    # create config by executing the command 'make config
    oe_runmake  -C ${S} O=${B} ${KERNEL_DEFCONFIG}

    # check if bigendian is enabled
    if [ "${SITEINFO_ENDIANNESS}" = "be" ]; then
        echo "CONFIG_CPU_BIG_ENDIAN=y" >> .config
        echo "CONFIG_MTD_CFI_BE_BYTE_SWAP=y" >> .config
    fi

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

