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
RPROVIDES:${PN} += "virtual/libgbm"

COLOR_METADATA_DIR = "${WORKSPACE}/vendor/qcom/opensource/commonsys-intf/display"
S = "${WORKDIR}/display/libgbm/"

DEPENDS += "virtual/kernel wayland glib-2.0 linux-msm-headers displaydlkm wayland libdmabufheap libvmmem"

do_configure[depends] += "virtual/kernel:do_shared_workdir"

PACKAGECONFIG ??= "glib \
                   ${@bb.utils.contains('COMBINED_FEATURES', 'drm', 'drm', '', d)} \
                  "

PACKAGECONFIG[glib] = "--with-glib, --without-glib, glib-2.0"
PACKAGECONFIG[drm] = "--enable-compilewithdrm, --disable-compilewithdrm"

EXTRA_OECONF += " --with-sanitized-headers=${STAGING_KERNEL_BUILDDIR}/usr/include"
EXTRA_OECONF += " ${@oe.utils.conditional('BASEMACHINE', 'qrbx210', '--enable-target-qrbx210=yes', '', d)}"
INSANE_SKIP:gbm += "dev-deps"

CPPFLAGS += "-I${STAGING_INCDIR}/linux-msm/usr/include/"
CPPFLAGS += "-I${STAGING_INCDIR}/"
do_install:append () {
  install -d                                               ${D}${includedir}
  install -d                                               ${D}${libdir}
  cp -rf ${S}/inc/gbm.h                                    ${D}${includedir}
  cp -rf ${S}/inc/gbm_priv.h                               ${D}${includedir}
  cp -rf ${COLOR_METADATA_DIR}/include/color_metadata.h    ${D}${includedir}
  install -m 0755 ${B}.libs/libgbm.so ${D}${libdir}/
  ln -sf libgbm.so ${D}${libdir}/libgbm.so.1
}
PACKAGES = "${PN}-dbg ${PN}"
FILES:${PN}-dbg  = "${libdir}/.debug/* ${bindir}/.debug/* /usr/lib/.debug/*"
FILES:${PN}      = "${libdir}/* /usr/lib/* ${bindir}/* ${includedir}/*"

