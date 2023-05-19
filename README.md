# Phytium Linux user guide

Download Yocto Layer

To make sure the build host is prepared for Yocto running and build, please follow below guide to prepare the build environment. 

https://docs.yoctoproject.org/4.0.9/ref-manual/index.html

1. Get the Yocto layers from repo manifest:

   The following is the step of how to use repo utility to download all Yocto layers according to the repo manifest
```
   Install the repo utility:
   $: mkdir ~/bin

   $: curl https://mirrors.tuna.tsinghua.edu.cn/git/git-repo > ~/bin/repo

   $:  export REPO_URL='https://mirrors.tuna.tsinghua.edu.cn/git/git-repo'

   $:  chmod a+x ~/bin/repo

   $: PATH=~/bin:${PATH}

   Download the Yocto layers:

   $: export PATH=~/bin:${PATH}

   $: mkdir <release>

   $: cd <release>

   $: repo init -u ssh://git@gitee.com:22/phytium_embedded/phytium-linux-yocto.git  -m default.xml

   $: repo sync --force-sync
```

# Supported boards

 e2000

# Building images

 Take e2000 as an example:

 $: . ./setup-env -m e2000
 
 $: bitbake core-image-minimal

 or:

 $: bitbake core-image-xfce

 Images will be found under tmp/deploy/images/e2000/.


# Booting the images for supported boards

 Prerequisites:
 1. A serial cable connected from your PC to UART1

 Booting with core-image-minimal rootfs:

  1. Power up or reset the board and press a key on the terminal when prompted
     to get to the U-Boot command line.

  2. Set up the environment in U-Boot:

     For e2000:

     => setenv bootargs console=ttyAMA1,115200  audit=0 earlycon=pl011,0x2800d000 root=/dev/sda2 rw
     
     => ext4load scsi 0:2 0x90100000 Image; ext4load scsi 0:2 0x90000000 <target>.dtb

     => booti 0x90100000 - 0x90000000


# Building with Multilib support

  Yocto Project is able to build libraries for different target optimizations, combing those in one system image,
  allowing the user to run both 32-bit and 64-bit applications.
  Here is an example to add multilib support (lib32).

  In local.conf
  - Define multilib targets
  
    require conf/multilib.conf

    MULTILIBS = "multilib:lib32"

    DEFAULTTUNE_virtclass-multilib-lib32 = "armv7athf-neon"
  
  - 32-bit libraries to be added into the image
  
    IMAGE_INSTALL_append = " lib32-glibc lib32-libgcc lib32-libstdc++"


# FAQ
 1. How do I build linux rt in the Yocto Project?

    Set PREFERRED_PROVIDER_virtual/kernel = "linux-phytium-rt" in your local.conf.
 
 2. How do I build linux 4.19 version in the Yocto Project ?

    Set PREFERRED_VERSION_linux-phytium = "4.19" in your local.conf.



# Contribute
  Please submit any patches to guochunrong@phytium.com.cn

# Maintainers

  GuoChunRong `<guochunrong@phytium.com.cn>`
