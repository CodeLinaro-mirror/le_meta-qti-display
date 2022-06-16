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

COLOR_METADATA_DIR = "${WORKSPACE}/vendor/qcom/opensource/commonsys-intf/display"
S = "${WORKDIR}/display/libgbm/"

DEPENDS += "linux-msm-headers wayland glib-2.0"

CFLAGS += "-I${STAGING_INCDIR}/linux-msm/usr/include"
CPPFLAGS += "-I${STAGING_INCDIR}/linux-msm/usr/include"

PACKAGECONFIG ??= "glib \
                   ${@bb.utils.contains('COMBINED_FEATURES', 'drm', 'drm', '', d)} \
                  "

PACKAGECONFIG[glib] = "--with-glib, --without-glib, glib-2.0"
PACKAGECONFIG[drm] = "--enable-compilewithdrm, --disable-compilewithdrm"

EXTRA_OECONF += " --with-sanitized-headers=${STAGING_KERNEL_BUILDDIR}/usr/include"
EXTRA_OECONF += " ${@oe.utils.conditional('BASEMACHINE', 'sdmsteppe', '--enable-target-qcs610=yes', '', d)}"
INSANE_SKIP_gbm += "dev-deps"
do_install_append () {
  install -d                                               ${D}${includedir}
  cp -rf ${S}/inc/gbm.h                                    ${D}${includedir}
  cp -rf ${S}/inc/gbm_priv.h                               ${D}${includedir}
  cp -rf ${COLOR_METADATA_DIR}/include/*.h                 ${D}${includedir}
}
PACKAGES = "${PN}-dbg ${PN}"
FILES_${PN}-dbg  = "${libdir}/.debug/* ${bindir}/.debug/* /usr/lib/.debug/*"
FILES_${PN}      = "${libdir}/* /usr/lib/* ${bindir}/* ${includedir}/*"
