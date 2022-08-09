do_configure:append () {
    if [ "${TCMODE}" = "external-arm" ] ; then
        sed -i -e "s# /lib64# /lib#g" ${B}/include/builddefs
        sed -i -e "s# /usr/lib64# /usr/lib#g" ${B}/include/builddefs
    fi
}

