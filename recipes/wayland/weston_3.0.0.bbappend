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
PKG_weston = "weston"
PKGV_weston = "3.0.0"
PKGR_weston = "1"

PKG_libweston-3 = "libweston-3-0"
PKGV_libweston-3 = "3.0.0"
PKGR_libweston-3 = "1"

DEPENDS += "libion libsync libdrm"

do_configure[depends] += "virtual/kernel:do_shared_workdir"
export PKG_CONFIG_PATH = "${PKG_CONFIG_DIR}:${STAGING_DATADIR}/pkgconfig:${STAGING_DIR_HOST}${libdir}/aarch64-linux-gnu/pkgconfig"

EXTRA_OECONF += " --with-sanitized-headers=${STAGING_KERNEL_BUILDDIR}/usr/include"
CFLAGS += "-I${STAGING_KERNEL_BUILDDIR}/usr/include/drm"
CPPFLAGS += "-I${STAGING_KERNEL_BUILDDIR}/usr/include/drm"
PACKAGECONFIG ?= " \
                 kms \
                 gbm \
                 clients \
                 egl \
                 "
PACKAGECONFIG[gbm] = "--with-gbm, --without-gbm, gbm, gbm"
PACKAGECONFIG[sdm] = "--with-sdm=${STAGING_INCDIR}/sdm, --without-sdm, display-hal-linux, display-hal-linux"
# Weston with kms backend
PACKAGECONFIG[kms] = "--enable-drm-compositor,--disable-drm-compositor,drm udev mtdev"
INSANE_SKIP_weston += "dev-deps"
INSANE_SKIP_libweston-3 += "dev-deps"

EXTRA_OECONF_append = "\
		WESTON_NATIVE_BACKEND=drm-backend.so \
		"

# CFLAGS += "-idirafter ${STAGING_KERNEL_DIR}/include/"

do_install_append () {
  echo 0 > ${WORKDIR}/gbm_dbg_cfg.txt
  install -m 0666 ${WORKDIR}/gbm_dbg_cfg.txt -D ${D}/data/misc/display/gbm_dbg_cfg.txt
}

# Weston on Wayland (nested Weston)
PACKAGECONFIG[wayland] = "--enable-wayland-compositor,--disable-wayland-compositor,gbm"
FILES_${PN} += "${bindir}/*"
FILES_${PN} += "/data/misc/display/*"
do_package_qa[noexec] = "1"
