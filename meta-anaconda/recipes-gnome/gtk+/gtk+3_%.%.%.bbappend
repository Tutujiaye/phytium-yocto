FILESEXTRAPATHS_prepend_anaconda := "${THISDIR}/files:"
SRC_URI_append_anaconda = " file://workaround-for-anaconda-installer-while-loading-libA.patch \
"
