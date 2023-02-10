SUMMARY = "Weston, a Wayland compositor"
DESCRIPTION = "Weston is the reference implementation of a Wayland compositor"
HOMEPAGE = "https://git.codelinaro.org/"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=d79ee9e66bb0f95d3386a7acae780b70"

FILESEXTRAPATHS:prepend := "${WORKSPACE}/display/:"
FILESEXTRAPATHS:prepend := "${THISDIR}/weston-launch:"
FILESPATH =+ "${WORKSPACE}:"
SRC_URI   = " file://weston-kalama.ini \
              file://display/vendor/qcom/opensource/display/weston/"

S = "${WORKDIR}/display/vendor/qcom/opensource/display/weston"

DEPENDS = "libxkbcommon gdk-pixbuf pixman cairo glib-2.0"
DEPENDS += "wayland wayland-protocols libinput adreno gbm pango wayland-native"
DEPENDS += "libsync display-hal-linux drm display-commonsys display-noship-linux"

EXTRA_OEMESON += "-Ddeprecated-wl-shell=true"
EXTRA_OEMESON += "-Dbackend-default=auto -Dbackend-rdp=false -Dpipewire=false"

RRECOMMENDS:${PN} = "weston-launch liberation-fonts"

REQUIRED_DISTRO_FEATURES:remove = "opengl"
REQUIRED_DISTRO_FEATURES:remove = "pam"

PACKAGECONFIG ??= ""
# Weston on SDM
PACKAGECONFIG[sdm] = "-Dbackend-sdm=true,-Dbackend-sdm=false"

CPPFLAGS += "-D__GBM__"

LDFLAGS  += "-lcutils -ldrmutils -ldisplaydebug -lglib-2.0"

#meson script's CPP flags
CXXFLAGS += "-I${WORKSPACE}/display/hardware/qcom/display/include"
CXXFLAGS += "-I${STAGING_INCDIR}/sdm"
CXXFLAGS += "-D__GBM__ "
CXXFLAGS += "-I${WORKSPACE}/display/vendor/qcom/opensource/commonsys-intf/display/include"
# select compositor, enable simple and demo clients and enable EGL
PACKAGECONFIG:append:kalama = "sdm clients egl shell-desktop"

do_install:append:kalama() {
    install -m 0644 ${WORKDIR}/weston-kalama.ini -D ${D}${sysconfdir}/xdg/weston/weston.ini
}

FILES:${PN} += "${bindir}/*"
FILES:${PN} += " ${libdir}/*.so"
FILES:${PN} += "${sysconfdir}/xdg/weston/weston.ini"
