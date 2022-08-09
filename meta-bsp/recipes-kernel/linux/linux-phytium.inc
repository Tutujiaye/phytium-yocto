KERNEL_BRANCH ?= "linux-4.19"
SRC_URI = "git://git@gitee.com:22/phytium_embedded/phytium-linux-kernel.git;protocol=ssh;branch=${KERNEL_BRANCH} "

SRCREV = "${AUTOREV}"
