require recipes-core/images/core-image-minimal.bb

PACKAGE_ARCH = "${MACHINE_ARCH}"

IMAGE_FSTYPES = "tar.gz ext2.gz"

SUMMARY = "Small virtual image to be used for evaluating the Phytium SOC"
DESCRIPTION = "Small image which includes some helpful tools and \
packages."

LICENSE = "MIT"

IMAGE_INSTALL += "packagegroup-phy-virtualization \
    packagegroup-core-ssh-openssh \
"
