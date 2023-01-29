SRC_URI = "\
        git://github.com/docker/docker-ce.git;protocol=http;branch=19.03;name=docker \
        git://github.com/moby/libnetwork.git;protocol=http;branch=bump_19.03;name=libnetwork;destsuffix=git/libnetwork \
        file://0001-libnetwork-use-GO-instead-of-go.patch \
        file://docker.init \
        file://0001-imporve-hardcoded-CC-on-cross-compile-docker-ce.patch \
        file://0001-dynbinary-use-go-cross-compiler.patch \
        file://0001-cli-use-go-cross-compiler.patch \
        "
