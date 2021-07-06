inherit autotools-brokensep pkgconfig qprebuilt

HOMEPAGE         = "http://support.cdmatech.com"
LICENSE          = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

DESCRIPTION = "libgbm Library"
PR = "r2"

SRC_URI     =  "file://display/libgbm"
FILESPATH   =+ "${WORKSPACE}:"

PROVIDES        += "virtual/libgbm"
RPROVIDES_${PN} += "virtual/libgbm"

S = "${WORKDIR}/display/libgbm/"

DEPENDS += "virtual/kernel wayland glib-2.0 display-commonsys"

do_configure[depends] += "virtual/kernel:do_shared_workdir"

PACKAGECONFIG ??= "glib \
                   ${@bb.utils.contains('COMBINED_FEATURES', 'drm', 'drm', '', d)} \
                  "

PACKAGECONFIG[glib] = "--with-glib, --without-glib, glib-2.0"
PACKAGECONFIG[drm] = "--enable-compilewithdrm, --disable-compilewithdrm"

PACKAGECONFIG_append_sxr2130-mtp = " glib drm "

EXTRA_OECONF += " --with-sanitized-headers=${STAGING_KERNEL_BUILDDIR}/usr/include"
INSANE_SKIP_gbm += "dev-deps"
do_install_append () {
  install -d                                               ${D}${includedir}
  cp -rf ${S}/inc/gbm.h                                    ${D}${includedir}
  cp -rf ${S}/inc/gbm_priv.h                               ${D}${includedir}
}
PACKAGES = "${PN}-dbg ${PN}"
FILES_${PN}-dbg  = "${libdir}/.debug/* ${bindir}/.debug/* /usr/lib/.debug/*"
FILES_${PN}      = "${libdir}/* /usr/lib/* ${bindir}/* ${includedir}/*"
