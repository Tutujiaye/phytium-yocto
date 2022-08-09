do_install_prepend_qemux86-64_anaconda () {
    if test -s ${WORKDIR}/xorg.conf; then
        sed -i 's/Modes *"640x480"/Modes    "1024x768"/g' ${WORKDIR}/xorg.conf
    fi
}

do_install_prepend_qemux86_anaconda () {
    if test -s ${WORKDIR}/xorg.conf; then
        sed -i 's/Modes *"640x480"/Modes    "1024x768"/g' ${WORKDIR}/xorg.conf
    fi
}

