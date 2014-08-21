MAINTAINER = "Robert Berger <robert.berger@reliableembeddedsystems.com>"
HOMEPAGE = "http://www.machinekit.io/"
SUMMARY = "Machinekit is a platform for machine control applications."
DESCRIPTION = "Machinekit is portable across a wide range of hardware platforms and real-time environments, and delivers excellent performance at low cost."
PROVIDES = "emcweb"
DEPENDS += "python-doctest boost tk bwidget tcl"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=5ad41ed7aac91d2ffb194c9fc1d45ed8"
PR = "r1"

SRC_URI = " \
file://COPYING;md5=5ad41ed7aac91d2ffb194c9fc1d45ed8 \
"

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
USERADD_PARAM_${PN} =" -m -r -s /bin/bash -d /home/machinekit machinekit"
#USERADD_PARAM_${PN} = "-u 1200 -d /home/machinekit -r -s /bin/bash machinekit"

do_install () {
        install -d -m 755 ${D}${datadir}/machinekit

#        install -p -m 644 machinekit.tar.bz2 ${D}${datadir}/machinekit/
        install -p -m 644 COPYING ${D}${datadir}/machinekit/

        # The new users and groups are created before the do_install
        # step, so you are now free to make use of them:
        chown -R machinekit ${D}${datadir}/machinekit
}


do_compile() {
         :
}


FILES_${PN} = "${datadir}/machinekit/*"  

# Prevents do_package failures with:
# debugsources.list: No such file or directory:
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"

#INHIBIT_PACKAGE_SPLIT = "1"
#INHIBIT_PACKAGE_STRIP = "1"
# for now:
#INSANE_SKIP_${PN} = "installed-vs-shipped "

