do_deploy_append() {
    # fixes rpi3 ttyAMA0 serial console
    echo "dtoverlay=pi3-miniuart-bt" >> ${DEPLOYDIR}/bcm2835-bootfiles/config.txt
}
