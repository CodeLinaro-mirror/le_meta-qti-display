FILESEXTRAPATHS_prepend := "${WORKSPACE}/display/:"

FILESEXTRAPATHS_prepend := "${THISDIR}/qti-patches:"
FILESPATH =+ "${WORKSPACE}:"
SRC_URI   = "file://display/weston"
S = "${WORKDIR}/display/weston"

inherit meson pkgconfig useradd distro_features_check
DEPENDS_append += "display-hal-linux drm virtual/libgles2 adreno200 \
		   display-noship-linux virtual/libgles1"

PACKAGECONFIG ??= ""

# Weston with SDM backend
PACKAGECONFIG[sdm] = "-Dbackend-sdm=true,-Dbackend-sdm=false"
# Weston with multi display support
PACKAGECONFIG[multidisplay] = "-Dmulti-display=true,-Dmulti-display=false"

LDFLAGS  += "-lcutils -lGLESv2_adreno -lEGL_adreno \
	     -lsdmutils -lsdmcore -ldrmutils -ldisplaydebug"

#meson script's CPP flags
CXXFLAGS += "-I${WORKSPACE}/hardware/qcom/display/include"
CXXFLAGS += "-I${STAGING_INCDIR}/sdm"
CXXFLAGS += "-D__GBM__ "

# select compositor, enable simple and demo clients and enable EGL
PACKAGECONFIG = "sdm clients egl multidisplay \
                 screenshare shell-desktop shell-fullscreen \
                 shell-ivi image-jpeg"

# Weston on Wayland (nested Weston)
FILES_${PN} += "${bindir}/*"
FILES_${PN} += " ${libdir}/*.so"
INSANE_SKIP_weston += "dev-deps"
