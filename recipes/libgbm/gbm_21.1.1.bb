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
SRC_URI = "file://display/vendor/qcom/opensource/display/libgbm/"
SRC_DIR = "${WORKSPACE}/display/vendor/qcom/opensource/display/libgbm/"

SRC_URI:append = " file://pkgconfig/gbm.pc"
SRCREV = "${AUTOREV}"
S = "${WORKDIR}/display/vendor/qcom/opensource/display/libgbm"

inherit autotools-brokensep qprebuilt pkgconfig

PREBUILT = "1"

EXTRA_OECONF += "--with-sanitized-headers=${STAGING_INCDIR}/linux-msm/usr/include"
EXTRA_OECONF += " \
    --enable-compilewithdrm \
"

CFLAGS += "-I${STAGING_INCDIR}/glib-2.0/ -I${STAGING_LIBDIR}/glib-2.0/include"
CFLAGS += "-DUSE_GLIB"
LDFLAGS += "-lglib-2.0"


PACKAGE_ARCH ?= "${MACHINE_ARCH}"

# The headers for GBM are contained in a completely separate package. Force
# that subsidiary package to be installed anytime that gbm-dev is.
RPROVIDES:${PN} += "libgbm"

SOLIBS = ".so"
FILES_SOLIBSDEV = ""

PACKAGES = "${PN}-dbg ${PN}"
FILES:${PN}-dbg  = "${libdir}/.debug/* ${bindir}/.debug/* /usr/lib/.debug/*"
FILES:${PN}      = "${libdir}/* /usr/lib/* ${bindir}/* ${includedir}/*"
