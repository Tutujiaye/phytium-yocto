SUMMARY = "Add user wrluser for lxqt Desktop Environment"
DESCRIPTION = "Since sddm not support root login, we add this user to resolve it"
LICENSE = "MIT"
inherit useradd

USERADD_PACKAGES = "${PN}"
USERADD_PARAM_${PN} = "--create-home \
                       --user-group -P 'user' user"
ALLOW_EMPTY_${PN} = "1"
