SUMMARY = "Xenomai is a real-time development framework cooperating with the Linux \
	    kernel, in order to provide a pervasive, interface-agnostic, hard \
	    real-time support to user-space applications, seamlessly integrated \
	    into the GNU/Linux environment."

HOMEPAGE = "http://www.xenomai.org"
SECTION = "tools/realtime"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=4d92cd373abda3937c2bc47fbc49d690 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

PROVIDES = "xenomai-userland"

PR = "r0"

SRC_URI = "http://download.gna.org/xenomai/stable/xenomai-${PV}.tar.bz2 \
           file://rtpipe.rules \
	   "
SRC_URI[md5sum] = "9f83c39cfb10535df6bf51702714e716"
SRC_URI[sha256sum] = "4d0d09431f0340cf4c9e2745177f77f15b7b124f89982035d1d3586519d7afe9"

inherit autotools

INSANE_SKIP_${PN}-dev += " libdir "
INSANE_SKIP_${PN}-dbg += " libdir "
INSANE_SKIP_${PN} += " libdir "

XENOMAI_SRC_PATH = "/usr/src/xenomai"

TARGET_CC_ARCH += "${LDFLAGS}"

prefix_x = "${prefix}/xenomai"

FILES_${PN} += "${prefix_x}/bin/* ${prefix_x}/sbin/* \
		 ${prefix_x}/bin/regression/native/* \
		 ${prefix_x}/bin/regression/native+posix/* \
		 ${prefix_x}/bin/regression/posix/* \
		 ${prefix_x}/lib/*.so.* ${prefix_x}/lib/pkgconfig ${prefix_x}/lib/posix.wrappers \
                 ${sysconfdir}/udev/rules.d/* \
                 "
#                ${sysconfdir}/* "

FILES_${PN}-doc += "${prefix_x}/share/*"
FILES_${PN}-dev += "${prefix_x}/include/* ${prefix_x}/lib/*.la ${prefix_x}/lib/*.so ${XENOMAI_SRC_PATH}"
FILES_${PN}-staticdev += "${prefix_x}/include/* ${prefix_x}/lib/*.a"
FILES_${PN}-dbg += "${prefix_x}/bin/.debug/* ${prefix_x}/sbin/.debug/* \
		${prefix_x}/bin/regression/native/.debug/* \
		 ${prefix_x}/bin/regression/native+posix/.debug/* \
		 ${prefix_x}/bin/regression/posix/.debug/* \
		 ${prefix_x}/lib/.debug/* \
		"

S = "${WORKDIR}/xenomai-${PV}"

do_configure() {
        ./configure --host=${HOST_SYS} 
}

do_compile () {
	make
}

do_install () {
	cd ${S}
	make DESTDIR=${D} install
	
	# remove /dev entry - it will be created later in image
	rm -fR ${D}/dev

        install -d ${D}${sysconfdir}/udev/rules.d

        # udev rules for rtheap - does not work
        # install -m 0644 ${WORKDIR}/rtheap.rules   ${D}${sysconfdir}/udev/rules.d/rtheap.rules
     
        # udev rule for rtpipes, so xenomai group can access rtpipes
        install -m 0644 ${WORKDIR}/rtpipe.rules     ${D}${sysconfdir}/udev/rules.d/rtpipe.rules
}

# this is a hack, but hey seems to work
# we want to delay the script execution so 
# that it runs on the target itself
pkg_postinst_${PN} () {
#!/bin/bash -e
if [ x"$D" = "x" ]; then
   # execute commands on target
   # /dev/rtheap permissions, group
   chmod 0660 /dev/rtheap
   chgrp xenomai /dev/rtheap
   # /dev/mem permissions, group
   chmod 0660 /dev/mem
   chgrp xenomai /dev/mem
   # non standard xenomai library path
   echo /usr/xenomai/lib > /etc/ld.so.conf 
   ldconfig
else
   exit 1
fi
}

RDEPENDS_${PN} = "udev"

