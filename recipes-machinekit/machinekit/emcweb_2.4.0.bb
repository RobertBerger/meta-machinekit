MAINTAINER = "Robert Berger <robert.berger@reliableembeddedsystems.com>"
HOMEPAGE = "http://www.machinekit.io/"
SUMMARY = "Machinekit is a platform for machine control applications."
DESCRIPTION = "Machinekit is portable across a wide range of hardware platforms and real-time environments, and delivers excellent performance at low cost. It is based on the HAL component architecture, an intuitive and easy to use circuit model that includes over 150 building blocks for digital logic, motion, control loops, signal processing, and hardware drivers. Machinekit supports local and networked UI options, including ubiquitous platforms like phones or tablets."
PROVIDES = "emcweb"
DEPENDS += "python-doctest boost tk bwidget tcl"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://machinekit/COPYING;md5=5ad41ed7aac91d2ffb194c9fc1d45ed8"

S = "${WORKDIR}"

SRCREV = "${AUTOREV}"

SRC_URI = " \
file://machinekit-prebuilt.tar.bz2;md5=8b43cd3b0bcdfef45c3bbca10f4d558a \
file://machinekit.mumu;md5=8b43cd3b0bcdfef45c3bbca10f4d558a \
"
inherit useradd

USERADD_PACKAGES = "${PN}"
USERADD_PARAM_${PN} =" -m -r -s /bin/bash -d /home/machinekit machinekit"

do_compile() {
         :
}

do_install() {
        install -d  ${D}/home/root/machinekit/
        install -p -m 644 ${WORKDIR}/machinekit.mumu ${D}/home/root/machinekit
}

FILES_${PN} = "${D}/home/root/machinekit/*"

#INHIBIT_PACKAGE_SPLIT = "1"
#INHIBIT_PACKAGE_STRIP = "1"
#INSANE_SKIP_${PN} = "installed-vs-shipped "

