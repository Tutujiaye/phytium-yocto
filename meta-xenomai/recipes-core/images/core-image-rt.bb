require recipes-core/images/core-image-minimal.bb

# Skip processing of this recipe if linux-intel-rt is not explicitly specified as the
# PREFERRED_PROVIDER for virtual/kernel. This avoids errors when trying
# to build multiple virtual/kernel providers.
#python () {
#    if not d.getVar("PREFERRED_PROVIDER_virtual/kernel").startswith("linux-xenomai"):
#        raise bb.parse.SkipPackage("Set PREFERRED_PROVIDER_virtual/kernel to linux-xenomai-* (as appropriate) to enable it")
#}

DESCRIPTION = "A small image just capable of allowing a device to boot plus a \
real-time test suite and tools appropriate for real-time use."

IMAGE_INSTALL += "grub-efi rt-tests hwlatdetect kernel-modules xenomai xenomai-demos stress-ng \
       packagegroup-core-ssh-dropbear \
       packagegroup-core-buildessential \
       xenomai-dev \
"

LICENSE = "MIT"

WKS_FILE_intel-corei7-64 = "core-image-rt.wks.in"
WKS_FILE_raspberrypi3 = "core-image-rt.wks.in"
WKS_FILE = "core-image-rt.wks.in"

do_rootfs[depends] += "core-image-minimal-initramfs:do_image_complete"
