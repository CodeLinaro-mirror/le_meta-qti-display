FILESEXTRAPATHS_prepend := "${WORKSPACE}/display/:"

FILESEXTRAPATHS_prepend := "${THISDIR}/qti-patches:"
FILESPATH =+ "${WORKSPACE}:"
SRC_URI   = "file://display/weston"
S = "${WORKDIR}/display/weston"

DEPENDS_sdmsteppe = "libxkbcommon gdk-pixbuf pixman cairo glib-2.0 libsync"
DEPENDS_sdmsteppe += "wayland libdrm libinput pango wayland-native wayland-protocols virtual/egl virtual/libgles2 adreno200 virtual/libgles1"

PACKAGECONFIG ??= ""


EXTRA_OECONF_append += "--with-wayland-scanner-path=${STAGING_BINDIR_NATIVE}/wayland-scanner"


#EXTRA_OECONF_append_sdmsteppe = "\
#	--enable-simple-egl-clients \
#	"
#  

CFLAGS += "-idirafter ${STAGING_KERNEL_DIR}/include/"
#CPPFLAGS += "-I${STAGING_INCDIR}/sdm"
#CPPFLAGS += "-I${STAGING_INCDIR}/sdm/core"
#CPPFLAGS += "-I${WORKSPACE}/display/display-hal/gpu_tonemapper"
CPPFLAGS += "-D__GBM__"
LDFLAGS  += "-lcutils"
#
# Compositor choices
#


PACKAGECONFIG_append_sdmsteppe = " kms"
PACKAGECONFIG_append_sdmsteppe = " clients"
PACKAGECONFIG_append_sdmsteppe = " egl"

# Weston on Wayland (nested Weston)
#FILES_${PN} += "${bindir}/weston-fullscreen ${bindir}/weston-flower ${bindir}/weston-simple-egl"
FILES_${PN} += " ${libdir}/*.so"
INSANE_SKIP_weston += "dev-deps"


