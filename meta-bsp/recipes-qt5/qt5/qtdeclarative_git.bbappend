SRC_URI = "https://mirrors.tuna.tsinghua.edu.cn/qt/official_releases/qt/5.15/5.15.3/submodules/qtdeclarative-everywhere-opensource-src-5.15.3.tar.xz"

SRC_URI += " \
    file://0001-Use-OE_QMAKE_PATH_EXTERNAL_HOST_BINS-to-locate-qmlca.patch \
    file://0001-yarr-Include-limits-for-numeric_limits.patch \
    file://0001-qmldebug-Include-limits-header.patch \
"

SRC_URI[sha256sum] = "33f15a5caa451bddf8298466442ccf7ca65e4cf90453928ddbb95216c4374062"

S = "${WORKDIR}/qtdeclarative-everywhere-src-${PV}"

PV = "5.15.3"
