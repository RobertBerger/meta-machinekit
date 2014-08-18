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
"

S = "${WORKDIR}/git/src/"

EXTRA_OECONF = "--disable-python "" \
                --with-posix "" \
                --enable-emcweb "" \
                --disable-gtk "" \
                --with-platform-beaglebone "" \
                --disable-option-checking "" \
                --with-boost-serialization=boost_serialization-mt "" \
                --with-build-sysroot=${PKG_CONFIG_SYSROOT_DIR}"" \
                --with-native-system-header-dir=${PKG_CONFIG_SYSROOT_DIR}\usr\include"" \
                --with-tclConfig=${STAGING_LIBDIR} --with-tkConfig=${STAGING_LIBDIR}"

do_configure() {
        ./autogen.sh
        oe_runconf
}

do_configure_append() {
      patch -p1 < ${WORKDIR}/0005-makefile-inc-tcl-h.ppatch
      patch -p1 < ${WORKDIR}/0006-dso-missing.ppatch
      patch -p1 < ${WORKDIR}/0007-missing-argument-to-l.ppatch
      patch -p1 < ${WORKDIR}/0008-rtlib-cross-compile.ppatch
}

do_compile() {
        ( export CPLUS_INCLUDE_PATH=${STAGING_INCDIR}/python2.7 
        make VV=1 )
}

inherit autotools gettext

