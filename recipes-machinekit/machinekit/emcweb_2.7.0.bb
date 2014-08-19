MAINTAINER = "Robert Berger <robert.berger@reliableembeddedsystems.com>"
HOMEPAGE = "http://www.machinekit.io/"
SUMMARY = "Machinekit is a platform for machine control applications."
DESCRIPTION = "Machinekit is portable across a wide range of hardware platforms and real-time environments, and delivers excellent performance at low cost. It is based on the HAL component architecture, an intuitive and easy to use circuit model that includes over 150 building blocks for digital logic, motion, control loops, signal processing, and hardware drivers. Machinekit supports local and networked UI options, including ubiquitous platforms like phones or tablets."
PROVIDES = "emcweb"
DEPENDS += "python-doctest boost tk bwidget tcl"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://../COPYING;md5=5ad41ed7aac91d2ffb194c9fc1d45ed8"

SRCREV = "${AUTOREV}"
SRC_URI = " \
git://github.com/machinekit/machinekit.git \
file://0001-configure-bwidget.patch \
file://0002-config-libgl1-mesa-dri-workaround.patch \
file://0005-makefile-inc-tcl-h.ppatch \
file://0006-dso-missing.ppatch \
file://0007-missing-argument-to-l.ppatch \
file://0008-rtlib-cross-compile.ppatch \
file://0009-no-libglade.ppatch \
"

S = "${WORKDIR}/git/src/"

inherit useradd

USERADD_PACKAGES = "${PN}"
USERADD_PARAM_${PN} = "-u 1200 -d /home/machinekit -r -s /bin/bash machinekit"
GROUPADD_PARAM_${PN} = "-g 1200 machinekit_group"

EXTRA_OECONF = "--disable-python "" \
                --with-posix "" \
                --enable-emcweb "" \
                --disable-gtk "" \
                --with-platform-beaglebone "" \
                --disable-option-checking "" \
                --with-boost-serialization=boost_serialization-mt "" \
                --with-tclConfig=/usr/lib "" \
                --with-tkConfig=/usr/lib"

do_configure() {
        ./autogen.sh
        oe_runconf
}

do_configure_append() {
      patch -p1 < ${WORKDIR}/0005-makefile-inc-tcl-h.ppatch
      patch -p1 < ${WORKDIR}/0006-dso-missing.ppatch
      patch -p1 < ${WORKDIR}/0007-missing-argument-to-l.ppatch
      patch -p1 < ${WORKDIR}/0008-rtlib-cross-compile.ppatch
      patch -p1 < ${WORKDIR}/0009-no-libglade.ppatch
}

do_compile() {
        ( export CPLUS_INCLUDE_PATH=${STAGING_INCDIR}/python2.7 
        oe_runmake VV=1 )
}

do_install() {
        install -d -m 755 ${D}${datadir}/machinekit
        chown -R machinekit ${D}${datadir}/machinekit
        chgrp -R machinekit_group ${D}${datadir}/machinekit

        oe_runmake install DESTDIR=${D}
}

inherit autotools gettext
INSANE_SKIP_${PN} = "installed-vs-shipped debug-files "
