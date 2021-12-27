FILESEXTRAPATHS_prepend := "${WORKSPACE}/display/:"

FILESEXTRAPATHS_prepend := "${THISDIR}/qti-patches:"
FILESPATH =+ "${WORKSPACE}:"
SRC_URI   = "file://display/weston"
S = "${WORKDIR}/display/weston"

DEPENDS_append += "display-hal-linux drm virtual/libgles2 adreno200 \
		   display-noship-linux virtual/libgles1"
DEPENDS_remove_qrbx210 = "display-hal-linux display-noship-linux"

PACKAGECONFIG ??= ""

CFLAGS += "-idirafter ${STAGING_KERNEL_DIR}/include/"
#CPPFLAGS += "-I${STAGING_INCDIR}/sdm"
#CPPFLAGS += "-I${STAGING_INCDIR}/sdm/core"
#CPPFLAGS += "-I${WORKSPACE}/display/display-hal/gpu_tonemapper"
CPPFLAGS += "-D__GBM__"
LDFLAGS  += "-lcutils -lGLESv2_adreno -lEGL_adreno \
	     -lsdmutils -lsdmcore -ldrmutils -ldisplaydebug"
LDFLAGS_remove_qrbx210 = "-lsdmutils -lsdmcore -ldrmutils -ldisplaydebug"

#meson script's CPP flags
CXXFLAGS += "-I${WORKSPACE}/hardware/qcom/display/include"
CXXFLAGS += "-I${STAGING_INCDIR}/sdm"
CXXFLAGS += "-D__GBM__ "

# select compositor, enable simple and demo clients and enable EGL
PACKAGECONFIG_append_qrb5165 = "sdm clients egl"
PACKAGECONFIG_append_qrbx210 = "kms clients egl"
PACKAGECONFIG_append_sxr2130-mtp = "sdm clients egl multidisplay"

# Weston on Wayland (nested Weston)
FILES_${PN} += "${bindir}/*"
FILES_${PN} += " ${libdir}/*.so"
INSANE_SKIP_weston += "dev-deps"
