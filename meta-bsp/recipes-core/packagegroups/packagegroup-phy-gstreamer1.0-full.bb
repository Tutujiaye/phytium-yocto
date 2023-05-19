DESCRIPTION = "Package group used by Phytium Community to provide all GStreamer plugins from the \
base, good, and bad packages, as well as the ugly and libav ones if commercial packages \
are whitelisted, and plugins for the required hardware acceleration (if supported by the SoC)."
SUMMARY = "Phytium Community package group - full set of all GStreamer 1.0 plugins"

inherit packagegroup

RDEPENDS:${PN} = " \
    packagegroup-phy-gstreamer1.0 \
    ${@bb.utils.contains('LICENSE_FLAGS_WHITELIST', 'commercial', 'packagegroup-phy-gstreamer1.0-commercial', '', d)} \
"
