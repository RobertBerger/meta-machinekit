MAINTAINER = "Robert Berger <robert.berger@reliableembeddedsystems.com>"
HOMEPAGE = "http://www.machinekit.io/"
SUMMARY = "Machinekit is a platform for machine control applications."
DESCRIPTION = "Machinekit is portable across a wide range of hardware platforms and real-time environments, and delivers excellent performance at low cost."
PROVIDES = "emcweb"
DEPENDS += "python-doctest boost tk bwidget tcl python-argparse bc python-tkinter python-distutils"

PACKAGECONFIG_pn-boost = "python"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=5ad41ed7aac91d2ffb194c9fc1d45ed8"
PR = "r2"

SRC_URI = " \
file://COPYING;md5=5ad41ed7aac91d2ffb194c9fc1d45ed8 \
file://machinekit \
file://config-pin \
http://www.reliableembeddedsystems.com/machinekit/machinekit-precompiled;name=machinekit-precompiled \
"

SRC_URI[machinekit-precompiled.md5sum] = "412d5b37e64d8a4f912427f9c15edfe6"
SRC_URI[machinekit-precompiled.sha256sum] = "2089e09469d311ea516e1fe316534b2a20a92d866847b472488a32ed541524d5"

#SRC_URI = " \
#file://COPYING;md5=5ad41ed7aac91d2ffb194c9fc1d45ed8 \
#file://machinekit.tar.bz2;md5=8b43cd3b0bcdfef45c3bbca10f4d558a \
#"

#SRC_URI = " \
#file://machinekit-prebuilt.tar.bz2;md5=8b43cd3b0bcdfef45c3bbca10f4d558a \
#"
S = "${WORKDIR}"

inherit useradd

USERADD_PACKAGES = "${PN}"

# we need a special xenomai group
GROUPADD_PARAM_${PN} = "-g 880 xenomai"

# we need a non root user -> machinekit
# .who is member of the xenomai group (/dev/rtheap, /dev/rtpipe) and the kmem group (/dev/mem)
USERADD_PARAM_${PN} =" -m -r -s /bin/bash -G xenomai,kmem -d /home/machinekit machinekit"

do_install () {
        install -d -m 755 ${D}${datadir}/machinekit

#        install -p -m 644 machinekit.tar.bz2 ${D}${datadir}/machinekit/
        install -p -m 644 COPYING ${D}${datadir}/machinekit/
        install -p -m 644 machinekit-precompiled ${D}${datadir}/machinekit/

        # The new users and groups are created before the do_install
        # step, so you are now free to make use of them:
        chown -R machinekit ${D}${datadir}/machinekit

        # the machinekit user shall be a sudoer
        install -d ${D}${sysconfdir}/sudoers.d
        install -p -m 644 machinekit ${D}${sysconfdir}/sudoers.d/machinekit

        # config-pin needs to be installed in /usr/bin
        install -d ${D}${bindir}
        install -p -m 755 config-pin ${D}${bindir}/config-pin
}


do_compile() {
         :
}

# this is a hack, but hey seems to work
# we want to delay the script execution so
# that it runs on the target itself
pkg_postinst_${PN} () {
#!/bin/bash -e
if [ x"$D" = "x" ]; then
   # execute commands on target
   tar xf /usr/share/machinekit/machinekit-precompiled -C /usr/share
   echo /usr/xenomai/lib > /etc/ld.so.conf
   ldconfig
else
   exit 1
fi
}

FILES_${PN} = "${datadir}/machinekit/* ${sysconfdir}/sudoers.d/* ${bindir}/*"

# Prevents do_package failures with:
# debugsources.list: No such file or directory:
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"

#INHIBIT_PACKAGE_SPLIT = "1"
#INHIBIT_PACKAGE_STRIP = "1"
# for now:
#INSANE_SKIP_${PN} = "installed-vs-shipped "

