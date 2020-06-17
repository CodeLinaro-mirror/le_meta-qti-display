FILESEXTRAPATHS_prepend := "${WORKSPACE}/display/:"

FILESEXTRAPATHS_prepend := "${THISDIR}/qti-patches:"
FILESPATH =+ "${WORKSPACE}:"
SRC_URI   = "file://display/weston"
S = "${WORKDIR}/display/weston"
SRC_URI_append_sdmsteppe = " file://0001-glibc-2.28-include-sysmacros.h-for-major-and-minor-c.patch"
SRC_URI_append_sdm845 = " file://0001-glibc-2.28-include-sysmacros.h-for-major-and-minor-c.patch"

FILESEXTRAPATHS_append := ":${THISDIR}/${PN}"

DEPENDS = "libxkbcommon gdk-pixbuf pixman cairo glib-2.0"
DEPENDS += "wayland libinput pango wayland-native"
DEPENDS_append_sdmsteppe += " libion libsync"
DEPENDS_append_apq8098 = "jpeg virtual/egl"

do_configure[depends] += "virtual/kernel:do_shared_workdir"

EXTRA_OECONF += " --with-sanitized-headers=${STAGING_KERNEL_BUILDDIR}/usr/include"

PACKAGECONFIG ?= " \
                 ${@bb.utils.contains('COMBINED_FEATURES', 'drm', 'drm', '', d)} \
                 ${@bb.utils.contains('COMBINED_FEATURES', 'fbdev', 'fbdev', '', d)} \
                 gbm \
                 sdm \
                 "

PACKAGECONFIG[gbm] = "--with-gbm, --without-gbm, gbm, gbm"
PACKAGECONFIG[sdm] = " \
                     --with-sdm=${STAGING_INCDIR}/sdm, --without-sdm, \
                     display-hal-linux display-noship-linux, \
                     display-hal-linux display-noship-linux \
                     "

# Weston on drm
PACKAGECONFIG[drm] = "--enable-drm-compositor, --disable-drm-compositor, libdrm"

# Weston on KMS - Update extra arguments, as they were conflicting with drm arguments
PACKAGECONFIG[kms] = "--with-kms"

# Weston on Wayland (nested Weston)
PACKAGECONFIG[wayland] = "--enable-wayland-compositor,--disable-wayland-compositor,gbm"

EXTRA_OECONF_append_apq8098 = "\
        --enable-simple-egl-clients \
	"
EXTRA_OECONF_append_sdmsteppe = "\
        --enable-simple-egl-clients \
	"

EXTRA_OECONF_append += "--with-wayland-scanner-path=${STAGING_BINDIR_NATIVE}/wayland-scanner"

CPPFLAGS += "-I${WORKSPACE}/display/display-hal/gpu_tonemapper"
LDFLAGS  += "-lcutils"

FILES_${PN} += "${bindir}/weston-fullscreen ${bindir}/weston-flower ${bindir}/weston-simple-egl"
FILES_${PN} += " ${libdir}/*.so"
INSANE_SKIP_weston += "dev-deps"

do_install_append_apq8098() {
	install -d ${STAGING_DIR_HOST}${datadir}/wayland-protocols/stable/gbm-buffer-backend/
	cp ${S}/protocol/gbm-buffer-backend.xml ${STAGING_DIR_HOST}${datadir}/wayland-protocols/stable/gbm-buffer-backend
	install -d                                                                 ${D}${libdir}/
	install -m 0755 ${B}/.libs/gbm-buffer-backend.so                           ${D}${libdir}/
}

FILES_${PN}-dbg    += "${libdir}/.debug/libgbm-buffer-backend-protocol.*"
FILES_${PN}        += "${libdir}/libgbm-buffer-backend-protocol.so.*"
FILES_${PN}-dev    += "${libdir}/libgbm-buffer-backend-protocol.so ${libdir}/libgbm-buffer-backend-protocol.la"
