SUMMARY = "Weston, a Wayland compositor"
DESCRIPTION = "Weston is the reference implementation of a Wayland compositor"
HOMEPAGE = "https://git.codelinaro.org/"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=d79ee9e66bb0f95d3386a7acae780b70"

FILESEXTRAPATHS:prepend := "${WORKSPACE}/display/:"
FILESEXTRAPATHS:prepend := "${THISDIR}/weston-launch:"
FILESPATH =+ "${WORKSPACE}:"
SRC_URI   = " file://weston.ini \
              file://display/weston/"


S = "${WORKDIR}/display/weston"

DEPENDS = "libxkbcommon gdk-pixbuf pixman cairo glib-2.0"
DEPENDS += "wayland wayland-protocols libinput adreno gbm pango wayland-native"
DEPENDS += "libsync display-hal-linux drm"

EXTRA_OEMESON += "-Ddeprecated-wl-shell=true"

RRECOMMENDS:${PN} = "weston-launch liberation-fonts"

REQUIRED_DISTRO_FEATURES:remove = "opengl"
REQUIRED_DISTRO_FEATURES:remove = "pam"

PACKAGECONFIG ??= ""

CFLAGS += "-idirafter ${STAGING_KERNEL_DIR}/include/"
CPPFLAGS += "-D__GBM__"

LDFLAGS  += "-lcutils -ldrmutils -ldisplaydebug -lglib-2.0"

# select compositor, enable simple and demo clients and enable EGL
PACKAGECONFIG:append:qrb5165 = "kms clients egl shell-desktop"

do_install:append:qrb5165() {
    install -m 0644 ${WORKDIR}/weston.ini -D ${D}${sysconfdir}/xdg/weston/weston.ini
}

FILES:${PN} += "${bindir}/*"
FILES:${PN} += " ${libdir}/*.so"
FILES:${PN} += "${sysconfdir}/xdg/weston/weston.ini"
