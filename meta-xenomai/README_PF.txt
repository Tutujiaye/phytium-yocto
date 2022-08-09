9/2/2018
========

- Ajout xenomai_3.0.6.bb

- Test sur Pyro ->

LES_${PN} += "/dev/*" -> LES_${PN} += "/dev"

- Test sur Rocko

* erreur bitbake-layers

$ bitbake-layers add-layer ../meta-openembedded/meta-oe/
NOTE: Starting bitbake server...
Loading cache: 100% |############################################| Time: 0:00:00
Loaded 6 entries from dependency cache.
ERROR: ParseError at /media/pierre/PF_WD_1TO/Yocto/rocko/meta-raspberrypi/recipes-devtools/python/rpi-gpio_0.6.3.bb:8: Could not inherit file classes/pypi.bbclass

Summary: There was 1 ERROR message shown, returning a non-zero exit code.
ERROR: Parse failure with the specified layer added

-> OK en ajoutant les layers directement dans bblayers.conf

pypi.bbclass défini dans meta-openembedded/meta-python/classes.

10/2/2018
=========

- Erreur compilation glibc

/bin/bash ../scripts/rellns-sh /media/pierre/PF_WD_1TO/Yocto/rocko/rpi3-build/tmp/work/cortexa7hf-neon-vfpv4-poky-linux-gnueabi/glibc/2.26-r0/build-arm-poky-linux-gnueabi/elf/ld.so /media/pierre/PF_WD_1TO/Yocto/rocko/rpi3-build/tmp/work/cortexa7hf-neon-vfpv4-poky-linux-gnueabi/glibc/2.26-r0/build-arm-poky-linux-gnueabi/elf/ld-linux-armhf.so.3.new
/media/pierre/PF_WD_1TO/Yocto/rocko/rpi3-build/tmp/work/cortexa7hf-neon-vfpv4-poky-linux-gnueabi/glibc/2.26-r0/recipe-sysroot-native/usr/bin/arm-poky-linux-gnueabi.gcc-cross-initial-arm/../../libexec/arm-poky-linux-gnueabi.gcc-cross-initial-arm/gcc/arm-poky-linux-gnueabi/7.3.0/ld:/media/pierre/PF_WD_1TO/Yocto/rocko/rpi3-build/tmp/work/cortexa7hf-neon-vfpv4-poky-linux-gnueabi/glibc/2.26-r0/build-arm-poky-linux-gnueabi/shlib.lds:156: syntax error
collect2: error: ld returned 1 exit status
make[2]: *** [/media/pierre/PF_WD_1TO/Yocto/rocko/rpi3-build/tmp/work/cortexa7hf-neon-vfpv4-poky-linux-gnueabi/glibc/2.26-r0/build-arm-poky-linux-gnueabi/libc.so] Error 1
make[2]: *** Waiting for unfinished jobs....

=> OK sur 14.04 !

*** /home/pierre/Bureau/shlib.lds	2018-02-11 01:28:25.640343636 +0100
--- /home/pierre/Bureau/shlib.lds.16_04	2018-02-11 01:34:58.991119520 +0100
***************
*** 153,159 ****
    }
    .jcr            : { KEEP (*(.jcr)) } 
  	 PROVIDE(__start___libc_subfreeres = .);
! 	 __libc_subfreeres : { *(__libc_subfreeres) }
  	 PROVIDE(__stop___libc_subfreeres = .);
  	 PROVIDE(__start___libc_atexit = .);
  	 __libc_atexit : { *(__libc_atexit) }
--- 153,159 ----
    }
    .jcr            : { KEEP (*(.jcr)) } 
  	 PROVIDE(__start___libc_subfreeres = .);
! 	 __libc_subfreE(__st { *(__libc_subfreeres) }
  	 PROVIDE(__stop___libc_subfreeres = .);
  	 PROVIDE(__start___libc_atexit = .);
  	 __libc_atexit : { *(__libc_atexit) }

-> pb avec make 3.81 (à cause Android) => OK avec 3.82 + 4.x

1/2/2018
=========

- Test Rocko -> OK


