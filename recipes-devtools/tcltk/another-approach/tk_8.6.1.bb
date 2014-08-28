DESCRIPTION = "Tk graphical toolkit for the Tcl scripting language."
LICENSE = "BSD-3-Clause"
SECTION = "devel/tcltk"
HOMEPAGE = "http://tcl.sourceforge.net"
DEPENDS = "tcl virtual/libx11"

LIC_FILES_CHKSUM = "file://../license.terms;md5=c88f99decec11afa967ad33d314f87fe \
                    file://../compat/license.terms;md5=c88f99decec11afa967ad33d314f87fe \
                    file://../library/license.terms;md5=c88f99decec11afa967ad33d314f87fe \
                    file://../macosx/license.terms;md5=c88f99decec11afa967ad33d314f87fe \
                    file://../tests/license.terms;md5=c88f99decec11afa967ad33d314f87fe \
                    file://../win/license.terms;md5=c88f99decec11afa967ad33d314f87fe \
                    "

PR = "r0"

SRC_URI = "${SOURCEFORGE_MIRROR}/tcl/tk${PV}-src.tar.gz"
SRC_URI[md5sum] = "63f21c3a0e0cefbd854b4eb29b129ac6"
SRC_URI[sha256sum] = "b691a2e84907392918665fe03a0deb913663a026bed2162185b4a9a14898162c"

S = "${WORKDIR}/tk${PV}/unix"

inherit autotools

EXTRA_OECONF = "--enable-threads \
                --disable-rpath \
                --with-tcl=${STAGING_BINDIR_CROSS} \
                "

do_configure() {
    gnu-configize
    oe_runconf
}

do_install() {
    autotools_do_install
    oe_libinstall -so libtk8.6 ${STAGING_LIBDIR}
    sed -i "s+${WORKDIR}+${STAGING_INCDIR}+g" tkConfig.sh
    sed -i "s,-L${libdir},," tkConfig.sh
    install -d ${STAGING_BINDIR_CROSS}/
    install -m 0755 tkConfig.sh ${STAGING_BINDIR_CROSS}
    cd ..
    for dir in compat generic unix
    do
        install -d ${STAGING_INCDIR}/tk${PV}/$dir
        install -m 0644 $dir/*.h ${STAGING_INCDIR}/tk${PV}/$dir/
    done
}

PACKAGES =+ "${PN}-demos \
         ${PN}-lib \
         "
FILES_${PN}-lib   = "${libdir}/libtk8.6.so* \
                     "
FILES_${PN}-demos = "${libdir}/tk8.6/demos/* \
                     "
FILES_${PN}      += "${libdir}/tk8.6/ttk/* \
             ${libdir}/tk8.6/images/* \
             ${libdir}/tk8.6/msgs/* \
             ${libdir}/tk8.6/*.tcl \
             ${libdir}/tk8.6/tclIndex \
          "
FILES_${PN}-dev += "${libdir}/tkConfig.sh \
            ${libdir}/tk8.6/tkAppInit.c \
            "

RDEPENDS_${PN} += "tk-lib"
