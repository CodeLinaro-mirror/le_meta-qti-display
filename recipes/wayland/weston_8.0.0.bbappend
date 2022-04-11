FILESEXTRAPATHS_prepend := "${WORKSPACE}/display/:"

FILESEXTRAPATHS_prepend := "${THISDIR}/qti-patches:"
FILESPATH =+ "${WORKSPACE}:"
SRC_URI   = "file://display/weston"
S = "${WORKDIR}/display/weston"

DEPENDS_append += "display-hal-linux drm virtual/libgles2 adreno200 \
		   display-noship-linux virtual/libgles1"

PACKAGECONFIG ??= ""

CFLAGS += "-idirafter ${STAGING_KERNEL_DIR}/include/"
CPPFLAGS += "-D__GBM__"
LDFLAGS  += "-lcutils -lGLESv2_adreno -lEGL_adreno \
	     -lsdmutils -lsdmcore -ldrmutils -ldisplaydebug"

#meson script's CPP flags
CXXFLAGS += "-I${WORKSPACE}/hardware/qcom/display/include"
CXXFLAGS += "-I${STAGING_INCDIR}/sdm"
CXXFLAGS += "-D__GBM__ "
CXXFLAGS += "-I${WORKSPACE}/vendor/qcom/opensource/commonsys-intf/display/include"

# select compositor, enable simple and demo clients and enable EGL
PACKAGECONFIG = "sdm clients egl"

# Weston on Wayland (nested Weston)
FILES_${PN} += "${bindir}/*"
FILES_${PN} += " ${libdir}/*.so"
INSANE_SKIP_weston += "dev-deps"
