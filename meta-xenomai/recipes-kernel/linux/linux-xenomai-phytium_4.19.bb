require linux-phytium.inc

KERNEL_BRANCH ?= "4.19.209-cip59"
SRC_URI = "git://gitee.com/phytium_embedded/linux-kernel-xenomai.git;protocol=https;branch=${KERNEL_BRANCH} "
SRC_URI:append = " file://0001-perf-bench-Share-some-global-variables-to-fix-build-.patch \
                   file://0001-perf-tests-bp_account-Make-global-variable-static.patch \ 
                   file://0001-libtraceevent-Fix-build-with-binutils-2.35.patch \
"
SRCREV = "24f65f47f91d78893b2162b12536b04c5404e9aa"

LINUX_VERSION_EXTENSION = "-xeno"

XENOMAI_SRC = "xenomai-v3.1.3"

# Xenomai source (prepare_kernel.sh script)
SRC_URI += "https://source.denx.de/Xenomai/xenomai/-/archive/v3.1.3/${XENOMAI_SRC}.tar.bz2;name=xeno"

SRC_URI[xeno.md5sum] = "38ba82b70180c2c7a95cdae1767c6de2"


