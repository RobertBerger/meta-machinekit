DESCRIPTION = "A suite of megawidgets for Tk"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=af21afb4e406f3d8e15b91dd3fa0a978"

DEPENDS = "tcl"
BBCLASSEXTEND = "native"

SRC_URI = "http://sourceforge.net/projects/tcllib/files/BWidget/${PV}/${PN}-${PV}.tar.gz"

SRC_URI[md5sum] = "2ec9a66ce2e0a7c4e741fc30a9d46b2b"
SRC_URI[sha256sum] = "313229892075fecbe57eef63525a901f384822e67de67b06749a2ab7a5d45ada"

S = "${WORKDIR}/${PN}-${PV}"

do_install() {
        install -d ${D}${libdir}/tcl8.6/${PN}-${PV}
        install -m 0644 ${S}/*.tcl ${D}${libdir}/tcl8.6/${PN}-${PV}

	install -d ${D}${libdir}/tcl8.6/images
	install -m 0644 ${S}/images/* ${D}${libdir}/tcl8.6/images
}

FILES_${PN} = "${libdir}/tcl8.6/"
