require linux-xenomai.inc
LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"
KERNEL_BRANCH ?= "4.19.209-cip59"
SRC_URI:append = " file://0001-perf-bench-Share-some-global-variables-to-fix-build-.patch \
                   file://0001-perf-tests-bp_account-Make-global-variable-static.patch \
                   file://0001-libtraceevent-Fix-build-with-binutils-2.35.patch \
"
SRCREV = "94d20013f323c68ee898ef2c0d1f64fd617a0f64"
