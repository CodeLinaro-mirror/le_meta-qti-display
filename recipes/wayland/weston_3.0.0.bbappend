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

DEPENDS += "libion libsync libdrm"

EXTRA_OECONF += " --with-sanitized-headers=${STAGING_KERNEL_BUILDDIR}/usr/include"

PACKAGECONFIG ?= " \
                 ${@bb.utils.contains('COMBINED_FEATURES', 'fbdev', 'fbdev', '', d)} \
                 gbm \
                 clients \
                 sdm \
                 "

PACKAGECONFIG[gbm] = "--with-gbm, --without-gbm, gbm, gbm"
PACKAGECONFIG[sdm] = "--with-sdm=${STAGING_INCDIR}/sdm, --without-sdm, display-hal-linux, display-hal-linux"

INSANE_SKIP_weston += "dev-deps"
INSANE_SKIP_libweston-3 += "dev-deps"

EXTRA_OECONF_append = "\
		WESTON_NATIVE_BACKEND=fbdev-backend.so \
		"

# CFLAGS += "-idirafter ${STAGING_KERNEL_DIR}/include/"

# Weston on Wayland (nested Weston)
PACKAGECONFIG[wayland] = "--enable-wayland-compositor,--disable-wayland-compositor,gbm"
FILES_${PN} += "${bindir}/weston-simple-egl"
