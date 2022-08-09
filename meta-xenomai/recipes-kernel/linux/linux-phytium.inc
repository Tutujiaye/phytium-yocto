KERNEL_BRANCH ?= ""
#SRC_URI = "git://git@gitee.com:22/phytium_embedded/linux-4.19.git;protocol=ssh;nobranch=1 "
SRC_URI = "git://git@gitlab.phytium.com.cn:12022/embedded/linux/linux-4.19.git;protocol=ssh;nobranch=1 "

SRC_URI_append_ft2002-evm = " file://patch-ft2000ahk-poweroff.patch"
SRCREV = "${AUTOREV}"


