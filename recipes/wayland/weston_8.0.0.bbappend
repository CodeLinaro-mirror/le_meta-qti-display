FILESEXTRAPATHS_prepend := "${WORKSPACE}/display/:"

FILESEXTRAPATHS_prepend := "${THISDIR}/qti-patches:"
PACKAGE_ARCH = "${MACHINE_ARCH}"
FILESPATH =+ "${WORKSPACE}:"
SRC_URI   = "file://display/weston"
S = "${WORKDIR}/display/weston"

inherit meson pkgconfig useradd distro_features_check
DEPENDS_append += "display-hal-linux virtual/libgles2 virtual/libgles1 gbm"

PACKAGECONFIG ??= ""

# Weston with SDM backend
PACKAGECONFIG[sdm] = "-Dbackend-sdm=true,-Dbackend-sdm=false"
# Weston with multi display support
PACKAGECONFIG[multidisplay] = "-Dmulti-display=true,-Dmulti-display=false"

LDFLAGS  += "-lcutils -lGLESv2_adreno -lEGL_adreno \
	     -lsdmutils -lsdmcore -ldrmutils -ldisplaydebug"

#meson script's CPP flags
CXXFLAGS += "-I${STAGING_INCDIR}/sdm"

# select compositor, enable simple and demo clients and enable EGL
PACKAGECONFIG = "sdm clients egl multidisplay \
                 screenshare shell-desktop shell-fullscreen \
                 shell-ivi image-jpeg"

# Weston on Wayland (nested Weston)
FILES_${PN} += "${bindir}/*"
FILES_${PN} += " ${libdir}/*.so"
INSANE_SKIP_weston += "dev-deps"
