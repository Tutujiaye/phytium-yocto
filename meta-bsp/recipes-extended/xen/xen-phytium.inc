FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

DEPENDS += "dtc-native virtual/kernel"

do_deploy[depends] += "virtual/kernel:do_deploy"

BLOB_LOADADDRESS ?= "0x8000"
DTB_NAME:d2000 = "d2000-devboard-dsk.dtb"
DTB_NAME:ft2004-evm = "ft2004-devboard-d4-dsk.dtb"
do_deploy:append() {
       if [ -f ${DEPLOYDIR}/xen-${MACHINE} ]; then
          dtc -I dtb -O dts ${DEPLOY_DIR_IMAGE}/${DTB_NAME} >  ${DEPLOY_DIR_IMAGE}/xen.dts
	  dtc -I dts -O dtb -S ${BLOB_LOADADDRESS} ${DEPLOY_DIR_IMAGE}/xen.dts > ${DEPLOY_DIR_IMAGE}/xen.dtb
       fi
}
