FILESEXTRAPATHS_prepend := "${WORKSPACE}/display/:"

FILESPATH =+ "${WORKSPACE}:"
SRC_URI   = "file://display/weston"
S = "${WORKDIR}/display/weston"

FILESEXTRAPATHS_append := ":${THISDIR}/${PN}"

DEPENDS_apq8098 = "libxkbcommon gdk-pixbuf pixman cairo glib-2.0 jpeg"
DEPENDS_apq8098 += "wayland libdrm gbm display-hal-linux libinput virtual/egl pango wayland-native"
DEPENDS_apq8098 += "display-noship-linux"


DEPENDS_qcs605 = "libxkbcommon gdk-pixbuf pixman cairo glib-2.0"
DEPENDS_qcs605 += "wayland libdrm gbm display-hal-linux libinput  pango wayland-native"
DEPENDS_qcs605 += "display-noship-linux"

DEPENDS_qcs40x = "libxkbcommon gdk-pixbuf pixman cairo glib-2.0 jpeg libion libsync"
DEPENDS_qcs40x += "wayland gbm display-hal-linux libinput virtual/egl pango wayland-native"

CFLAGS_append_qcs40x += "-I${STAGING_KERNEL_BUILDDIR}/usr/include"

EXTRA_OECONF_append_apq8098 = "\
	--enable-drm-compositor \
	"
EXTRA_OECONF_append_qcs605 = "\
	--enable-drm-compositor \
	"
EXTRA_OECONF_append += "--with-wayland-scanner-path=${STAGING_BINDIR_NATIVE}/wayland-scanner"

EXTRA_OECONF_append_qcs40x = "\
	--enable-fbdev-compositor \
	"

DEPENDS_apq8017 = "libxkbcommon gdk-pixbuf pixman cairo glib-2.0 jpeg"
DEPENDS_apq8017 += "wayland libinput virtual/egl pango"

EXTRA_OECONF_append_apq8017 = "\
		WESTON_NATIVE_BACKEND=fbdev-backend.so \
		"

EXTRA_OECONF_append_qcs40x = "\
		WESTON_NATIVE_BACKEND=fbdev-backend.so \
		"
EXTRA_OECONF_append_qcs40x = "\
	--enable-simple-egl-clients \
		"

CFLAGS += "-idirafter ${STAGING_KERNEL_DIR}/include/"
CPPFLAGS += "-I${STAGING_INCDIR}/sdm"
CPPFLAGS += "-I${STAGING_INCDIR}/sdm/core"
CPPFLAGS += "-I${WORKSPACE}/display/display-hal/gpu_tonemapper"
CPPFLAGS += "-D__GBM__"
#
# Compositor choices
#
# Adding fbdev package for 8017 target
PACKAGECONFIG_remove_apq8017 = "kms"
PACKAGECONFIG_append_apq8017 = " fbdev"

PACKAGECONFIG_remove_qcs40x = "kms"
PACKAGECONFIG_append_qcs40x = " fbdev"

# Weston on Wayland (nested Weston)
PACKAGECONFIG[wayland] = "--enable-wayland-compositor,--disable-wayland-compositor,gbm"
FILES_${PN} += "${bindir}/weston-fullscreen ${bindir}/weston-flower ${bindir}/weston-simple-egl"
INSANE_SKIP_weston += "dev-deps"

do_install_append_apq8098() {
	install -d ${STAGING_DIR_HOST}${datadir}/wayland-protocols/stable/gbm-buffer-backend/
	cp ${S}/protocol/gbm-buffer-backend.xml ${STAGING_DIR_HOST}${datadir}/wayland-protocols/stable/gbm-buffer-backend
}
