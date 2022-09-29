KERNEL_BRANCH ?= "main"
SRC_URI = "git://git@gitlab.phytium.com.cn:12022/embedded/linux/linux-kernel-xenomai.git;protocol=ssh;branch=${KERNEL_BRANCH} "
SRCREV = "${AUTOREV}"
