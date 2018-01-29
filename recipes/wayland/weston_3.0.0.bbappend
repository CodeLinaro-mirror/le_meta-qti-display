FILESPATH =+ "${WORKSPACE}:"

SRC_URI = "file://display/weston \
           file://weston.png \
           file://weston.desktop \
           file://0001-make-error-portable.patch \           
           file://xwayland.weston-start \
	   file://weston-gl-renderer-Set-pitch-correctly-for-subsampled-textures.patch \
	   file://fix-missing-header.patch \
"
SRCREV = "${AUTOREV}"
S      = "${WORKDIR}/weston"


DEPENDS += "gbm"

INSANE_SKIP_weston += "dev-deps"
INSANE_SKIP_libweston-3 += "dev-deps"

EXTRA_OECONF_append = "\
   --enable-fbdev-compositor \
"

EXTRA_OECONF_append = "\
		WESTON_NATIVE_BACKEND=fbdev-backend.so \
		"

CFLAGS += "-idirafter ${STAGING_KERNEL_DIR}/include/"
CPPFLAGS += "-I${STAGING_INCDIR}/sdm"
CPPFLAGS += "-I${STAGING_INCDIR}/sdm/core"

#
# Compositor choices
#
# Adding fbdev package
PACKAGECONFIG_remove = "kms"
PACKAGECONFIG_append = " fbdev"
# Weston on Wayland (nested Weston)
PACKAGECONFIG[wayland] = "--enable-wayland-compositor,--disable-wayland-compositor,libgbm"
PACKAGECONFIG_append = " clients"
