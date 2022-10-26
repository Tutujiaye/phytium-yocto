KERNEL_BRANCH ?= "master"
SRC_URI = "git://gitee.com/phytium_embedded/linux-kernel-xenomai.git;protocol=https;branch=${KERNEL_BRANCH} "
SRCREV = "${AUTOREV}"
