PACKAGECONFIG = "avdevice avfilter avcodec avformat swresample swscale postproc avresample \
                   alsa bzlib lzma pic pthreads shared theora zlib sdl2 \
                   ${@bb.utils.contains('AVAILTUNES', 'mips32r2', 'mips32r2', '', d)} \
                   ${@bb.utils.contains('DISTRO_FEATURES', 'x11', 'xv xcb', '', d)}"
