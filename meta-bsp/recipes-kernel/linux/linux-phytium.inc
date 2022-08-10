KERNEL_BRANCH ?= "linux-4.19"
SRC_URI = "git://gitee.com/phytium_embedded/phytium-linux-kernel.git;protocol=https;branch=${KERNEL_BRANCH} "

SRCREV = "${AUTOREV}"
