SRC_URI = "git://gitee.com/mirrors/dnf.git;branch=master;protocol=https \
           file://0001-Corretly-install-tmpfiles.d-configuration.patch \
           file://0001-Do-not-hardcode-etc-and-systemd-unit-directories.patch \
           file://0005-Do-not-prepend-installroot-to-logdir.patch \
           file://0029-Do-not-set-PYTHON_INSTALL_DIR-by-running-python.patch \
           file://0030-Run-python-scripts-using-env.patch \
           file://0001-set-python-path-for-completion_helper.patch \
           file://0001-dnf-write-the-log-lock-to-root.patch \
           "
