PACKAGECONFIG:remove = "xcomposite-egl xcomposite-glx"
PACKAGECONFIG:append = " ${PHY_EXA}"

PHY_EXA = "${@bb.utils.contains('DISTRO_FEATURES', 'x11', '', 'examples', d)}"
