# DISTRO_FEATURES:remove = " x11"

IMAGE_INSTALL:append =" packagegroup-qt5-demos \
     packagegroup-qt5-phytium \
     packagegroup-phy-gstreamer1.0 \
     packagegroup-phy-gstreamer1.0-full \
     gstreamer1.0-plugins-base-meta \
     gstreamer1.0-plugins-good-meta \
     gstreamer1.0-plugins-bad-meta  \
     alsa-utils-alsactl alsa-utils-alsamixer \
     alsa-utils-amixer alsa-utils-aplay \
     systemd \
"
