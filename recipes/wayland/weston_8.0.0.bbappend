FILESEXTRAPATHS_prepend := "${WORKSPACE}/display/:"

FILESEXTRAPATHS_prepend := "${THISDIR}/qti-patches:"
FILESPATH =+ "${WORKSPACE}:"
SRC_URI   = "file://display/weston"
S = "${WORKDIR}/display/weston"

DEPENDS_append += "drm virtual/libgles2 adreno200 virtual/libgles1"

PACKAGECONFIG ??= ""

CFLAGS += "-idirafter ${STAGING_KERNEL_DIR}/include/"
#CPPFLAGS += "-I${STAGING_INCDIR}/sdm"
#CPPFLAGS += "-I${STAGING_INCDIR}/sdm/core"
#CPPFLAGS += "-I${WORKSPACE}/display/display-hal/gpu_tonemapper"
CPPFLAGS += "-D__GBM__"
LDFLAGS  += "-lcutils"

# select compositor, enable simple and demo clients and enable EGL
PACKAGECONFIG_append = "kms clients egl"

# Weston on Wayland (nested Weston)
FILES_${PN} += "${bindir}/*"
FILES_${PN} += " ${libdir}/*.so"
INSANE_SKIP_weston += "dev-deps"
