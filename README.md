# Phytium Linux user guide

Download Yocto Layer

To make sure the build host is prepared for Yocto running and build, please follow below guide to prepare the build environment. 

https://www.yoctoproject.org/docs/3.3/brief-yoctoprojectqs/brief-yoctoprojectqs.html

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

   $: repo init -u https://gitee.com/phytium_embedded/phytium-sdk.git -b master [ -m <release manifest>]

   $: repo sync --force-sync
```

# Supported boards

 ft2002-evm

 ft2004-evm

 d2000

# Building images

 Take ft2004-evm as an example:

 $: . ./setup-env -m ft2004-evm

 Note: Please contact phytium sales for linux-phytium kernel source codes

 you must add the below env to local.conf

 INHERIT += "externalsrc"

 EXTERNALSRC_pn-linux-phytium = "path-to-your-source-tree"

 $: bitbake core-image-minimal

 or:

 $: bitbake core-image-sato

 Images will be found under tmp/deploy/images/ft2004-evm/.


# Booting the images for supported boards

 Prerequisites:
 1. TFTP server being setup to hold the images.
 2. A serial cable connected from your PC to UART1
 3. Ethernet connected to the first ethernet port on the board.

 Booting with core-image-minimal rootfs:

  1. Power up or reset the board and press a key on the terminal when prompted
     to get to the U-Boot command line.

  2. Set up the environment in U-Boot:

     => setenv ipaddr \<board_ipaddr\>

     => setenv serverip <tftp_serverip>

     For ft2000:

     => setenv bootargs console=ttyAMA1,115200 earlycon=pl011,0x28001000 root=/dev/ram0 rw  ramdisk_size=0x2000000

     => tftpboot 0x90100000 ft2004-devboard-d4-dsk.dtb ; tftpboot 0x90200000 Image

     => tftpboot 0x93000000 core-image-minimal-ft2004-evm.ext2.gz.u-boot

     => booti 0x90200000 0x93000000 0x90100000

# Building  Xenomai
  
   Set the below env to local.conf

   PREFERRED_PROVIDER_virtual/kernel = "linux-xenomai-phytium"

   $: bitbake core-image-rt
    
   For test steps ,see the https://gitee.com/phytium_embedded/FT2004-Xenomai

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

 2. How do fix "linux-phytium-4.19-r0 do_preconfigure error" ?

    linux kernel source code must git init

    $ cd  \<linux-kernel-source\>

    $ git init

    $ git add .

    $ git commit -s -m "init"


# Contribute
  Please submit any patches to guochunrong@phytium.com.cn

# Maintainers

  GuoChunRong `<guochunrong@phytium.com.cn>`
