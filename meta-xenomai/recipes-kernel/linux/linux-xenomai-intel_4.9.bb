DESCRIPTION = "Linux/Xenomai kernel for Intel"

XENOMAI_SRC = "xenomai-3.0.7"
IPIPE_PATCH = "ipipe-core-4.9.90-x86-6.patch"

LINUX_VERSION ?= "4.9.90"

SRC_URI = "git://git.kernel.org/pub/scm/linux/kernel/git/stable/linux.git;protocol=https;branch=linux-4.9.y"

include recipes-kernel/linux/linux-xenomai.inc

SRCREV = "v4.9.90"

SRC_URI[xeno.md5sum] = "6d7760cd065359666b16bfdaf0f1f932"

COMPATIBLE_MACHINE = "intel"
