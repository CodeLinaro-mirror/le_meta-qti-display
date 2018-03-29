FILESEXTRAPATHS_prepend := "${WORKSPACE}/display/:"

FILESPATH =+ "${WORKSPACE}:"
SRC_URI   = "file://display/weston"
S = "${WORKDIR}/display/weston"

FILESEXTRAPATHS_append := ":${THISDIR}/${PN}"

DEPENDS_apq8098 = "libxkbcommon gdk-pixbuf pixman cairo glib-2.0 jpeg"
DEPENDS_apq8098 += "wayland libdrm gbm display-hal-linux libinput virtual/egl pango wayland-native"
DEPENDS_apq8098 += "display-noship-linux"

EXTRA_OECONF_append = "\
	--enable-drm-compositor \
	"

EXTRA_OECONF_append += "--with-wayland-scanner-path=${STAGING_BINDIR_NATIVE}/wayland-scanner"
CFLAGS += "-idirafter ${STAGING_KERNEL_DIR}/include/"
CPPFLAGS += "-I${STAGING_INCDIR}/sdm"
CPPFLAGS += "-I${STAGING_INCDIR}/sdm/core"
CPPFLAGS += "-I${WORKSPACE}/display/display-hal/gpu_tonemapper"
#
# Compositor choices
#
# Weston on KMS
PACKAGECONFIG[kms] = "--enable-drm-compositor"
# Weston on Wayland (nested Weston)
PACKAGECONFIG[wayland] = "--enable-wayland-compositor,--disable-wayland-compositor,gbm"
FILES_${PN} += "${bindir}/weston-fullscreen ${bindir}/weston-flower"
INSANE_SKIP_weston += "dev-deps"

do_install_append_apq8098() {
	install -d ${STAGING_DIR_HOST}${datadir}/wayland-protocols/stable/gbm-buffer-backend/
	cp ${S}/protocol/gbm-buffer-backend.xml ${STAGING_DIR_HOST}${datadir}/wayland-protocols/stable/gbm-buffer-backend
}
