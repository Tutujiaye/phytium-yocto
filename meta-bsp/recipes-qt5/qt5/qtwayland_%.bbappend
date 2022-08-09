PACKAGECONFIG_remove = "xcomposite-egl xcomposite-glx"
PACKAGECONFIG_append = " ${PHY_EXA}"

PHY_EXA = "${@bb.utils.contains('DISTRO_FEATURES', 'x11', '', 'examples', d)}"
