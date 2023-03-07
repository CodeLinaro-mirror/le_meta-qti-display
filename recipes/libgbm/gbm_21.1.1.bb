SUMMARY = "gbm"
DESCRIPTION = "Provide QC contributed GBM (Generic Buffer Management) \
library."
HOMEPAGE = "https://git.codelinaro.org/"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

DEPENDS = "glib-2.0 linux-msm-headers displaydlkm wayland libdmabufheap libvmmem display-commonsys"
PROVIDES += "virtual/libgbm libgbm"

FILESPATH   =+ "${WORKSPACE}:"
SRC_URI = "file://display/libgbm/"
SRC_DIR = "${WORKSPACE}/display/libgbm/"

SRC_URI:append = " file://pkgconfig/gbm.pc"
SRCREV = "${AUTOREV}"
S = "${WORKDIR}/display/libgbm"

inherit autotools-brokensep qprebuilt pkgconfig

PREBUILT = "1"

EXTRA_OECONF += "--with-sanitized-headers=${STAGING_KERNEL_BUILDDIR}/usr/include"
EXTRA_OECONF += " \
    --enable-compilewithdrm \
"

CFLAGS += "-I${STAGING_INCDIR}/glib-2.0/ -I${STAGING_LIBDIR}/glib-2.0/include"
CFLAGS += "-I${STAGING_INCDIR}/disp-commonsys-intf/display"
CFLAGS += "-I${STAGING_INCDIR}/libdrm/"
CFLAGS += "-DUSE_GLIB"


# add display techpack headers
CPPFLAGS += "-I${STAGING_INCDIR}/linux-msm/usr/include/"
CPPFLAGS += "-I${STAGING_INCDIR}/"

LDFLAGS += "-lglib-2.0"

do_install(){
    # gbm - Libs
    install -d ${D}${libdir}/
    install -m 0644 ${S}/.libs/libgbm.so ${D}${libdir}/
    install -d ${D}${includedir}/
    install -m 066 ${S}/inc/gbm.h ${D}${includedir}/
    install -m 066 ${S}/inc/gbm_priv.h ${D}${includedir}/
    install -m 0644 ${S}/inc/*.h ${D}${includedir}

    install -d ${D}${libdir}/pkgconfig/
    install -m 0664 ${WORKDIR}/pkgconfig/* ${D}${libdir}/pkgconfig
    sed -i  \
      -e 's:OEPREFIX:${prefix}:g' \
      -e 's:OELIBDIR:${libdir}:g' \
      -e 's:OEINCDIR:${includedir}:g' \
      -e 's:OEEXECPREFIX:${exec_prefix}:g' \
      ${D}${libdir}/pkgconfig/gbm.pc
}

PACKAGE_ARCH ?= "${MACHINE_ARCH}"

# The headers for GBM are contained in a completely separate package. Force
# that subsidiary package to be installed anytime that gbm-dev is.
RPROVIDES:${PN} += "libgbm"

SOLIBS = ".so"
FILES_SOLIBSDEV = ""

PACKAGES = "${PN}-dbg ${PN}"
FILES:${PN}-dbg  = "${libdir}/.debug/* ${bindir}/.debug/* /usr/lib/.debug/*"
FILES:${PN}      = "${libdir}/* /usr/lib/* ${bindir}/* ${includedir}/*"
