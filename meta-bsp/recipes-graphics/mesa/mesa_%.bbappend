PACKAGECONFIG_append = " r600 gallium gallium-llvm"
python () {
    d.setVar("GALLIUMDRIVERS", "swrast,r300,r600,radeonsi")
}
