FILESEXTRAPATHS_prepend := "${WORKSPACE}/display/:"

FILESPATH =+ "${WORKSPACE}:"
SRC_URI   = "file://display/weston"
S = "${WORKDIR}/weston"

FILESEXTRAPATHS_append := ":${THISDIR}/${PN}"

DEPENDS_apq8098 = "libxkbcommon gdk-pixbuf pixman cairo glib-2.0 jpeg"
DEPENDS_apq8098 += "wayland libinput virtual/egl pango"
DEPENDS_apq8098 += "display-hal-linux display-noship-linux display-ship-linux"

EXTRA_OECONF_append = "\
	--enable-drm-compositor \
	"
CFLAGS += "-idirafter ${STAGING_KERNEL_DIR}/include/"
CPPFLAGS += "-I${STAGING_INCDIR}/"
CPPFLAGS += "-I${STAGING_INCDIR}/sdm/"
CPPFLAGS += "-I${STAGING_INCDIR}/sdm/core/"

#
# Compositor choices
#
# Weston on KMS
PACKAGECONFIG[kms] = "--enable-drm-compositor"
# Weston on Wayland (nested Weston)
PACKAGECONFIG[wayland] = "--enable-wayland-compositor,--disable-wayland-compositor,gbm"
FILES_${PN} += "${bindir}/weston-fullscreen ${bindir}/weston-flower ${bindir}/weston-simple-egl"
FILES_${PN} += " ${libdir}/*.so"
INSANE_SKIP_weston += "dev-deps"

do_install_append_apq8098() {
	install -d ${STAGING_DIR_HOST}${datadir}/wayland-protocols/stable/gbm-buffer-backend/
	cp ${S}/protocol/gbm-buffer-backend.xml ${STAGING_DIR_HOST}${datadir}/wayland-protocols/stable/gbm-buffer-backend
	install -d                                                                 ${D}${libdir}/
	install -m 0755 ${B}/.libs/gbm-buffer-backend.so                           ${D}${libdir}/
}
