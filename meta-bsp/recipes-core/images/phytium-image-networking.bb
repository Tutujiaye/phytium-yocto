require recipes-core/images/core-image-minimal.bb

PACKAGE_ARCH = "${MACHINE_ARCH}"

CORE_IMAGE_EXTRA_INSTALL += "grub-efi lsb-release"

IMAGE_FSTYPES = "tar.gz ext4"

SUMMARY = "Small image to be used for evaluating the Phytium SOC"
DESCRIPTION = "Small image which includes some helpful tools and \
packages."

LICENSE = "MIT"

IMAGE_INSTALL:append = " \
    packagegroup-core-ssh-openssh \
    packagegroup-phy-tools-core \
    packagegroup-phy-benchmark-core \
    packagegroup-phy-benchmark-extended \
    packagegroup-phy-networking-core \
    packagegroup-core-tools-testapps \
    packagegroup-core-tools-debug \
    packagegroup-core-full-cmdline \
    stress-ng \
"
