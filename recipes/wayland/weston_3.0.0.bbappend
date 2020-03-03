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
S      = "${WORKDIR}/display/weston"


DEPENDS += "gbm libion libsync libcutils"

CFLAGS_append += "-I${STAGING_KERNEL_BUILDDIR}/usr/include"
CPPFLAGS += "-D__GBM__"

INSANE_SKIP_weston += "dev-deps"
INSANE_SKIP_libweston-3 += "dev-deps"

EXTRA_OECONF_append = "\
   --enable-drm-compositor \
"

EXTRA_OECONF_append = "\
		WESTON_NATIVE_BACKEND=drm-backend.so \
		"

CFLAGS += "-idirafter ${STAGING_KERNEL_DIR}/include/"
CPPFLAGS += "-I${STAGING_INCDIR}/sdm"
CPPFLAGS += "-I${STAGING_INCDIR}/sdm/core"

#
# Compositor choices
#
# Adding kms package
PACKAGECONFIG_remove = "fbdev"
PACKAGECONFIG_append = "kms gbm clients egl"
# Weston on Wayland (nested Weston)
PACKAGECONFIG[wayland] = "--enable-wayland-compositor,--disable-wayland-compositor,libgbm"
FILES_${PN} += "${bindir}/weston-simple-egl"
PACKAGECONFIG_append = " clients"
