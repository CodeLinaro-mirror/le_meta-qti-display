LIC_FILES_CHKSUM = "file://COPYING;md5=d79ee9e66bb0f95d3386a7acae780b70"
FILESEXTRAPATHS:prepend := "${WORKSPACE}/display/:"

PACKAGE_ARCH = "${MACHINE_ARCH}"
FILESPATH =+ "${WORKSPACE}:"
SRC_URI   = "file://display/weston"
S = "${WORKDIR}/display/weston"

inherit meson pkgconfig useradd distro_features_check
DEPENDS:append += "display-hal-linux virtual/libgles2 virtual/libgles1 gbm"

PACKAGECONFIG ??= ""
EXTRA_OEMESON += "-Dbackend-default=auto"
EXTRA_OEMESON += "-Ddeprecated-wl-shell=true"

LDFLAGS  += "-lcutils -lGLESv2_adreno -lEGL_adreno \
	     -ldrmutils -ldisplaydebug -lm"


# select compositor, enable simple and demo clients and enable EGL
PACKAGECONFIG = "kms clients egl multidisplay \
                 screenshare shell-desktop shell-fullscreen \
                 shell-ivi image-jpeg"

# Weston on Wayland (nested Weston)
FILES:${PN} += "${bindir}/*"
FILES:${PN} += " ${libdir}/*.so"
INSANE_SKIP:weston += "dev-deps"

