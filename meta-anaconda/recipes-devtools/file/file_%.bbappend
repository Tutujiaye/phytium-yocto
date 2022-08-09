FILESEXTRAPATHS_prepend_anaconda := "${THISDIR}/files:"
SRC_URI_append_class-native_anaconda = " \
    file://0001-Makefile.am-remove-tests.patch \
"

