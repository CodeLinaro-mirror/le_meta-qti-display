SUMMARY = "Weston, a Wayland compositor"
DESCRIPTION = "Weston is the reference implementation of a Wayland compositor"
HOMEPAGE = "https://git.codelinaro.org/"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=d79ee9e66bb0f95d3386a7acae780b70"

FILESEXTRAPATHS:prepend := "${WORKSPACE}/display/:"
FILESEXTRAPATHS:prepend := "${THISDIR}/weston-launch:"
FILESPATH =+ "${WORKSPACE}:"
SRC_URI   = " file://weston-kalama.ini \
              file://weston-pineapple.ini \
              file://weston-sun.ini \
              file://display/vendor/qcom/opensource/display/weston/"

S = "${WORKDIR}/display/vendor/qcom/opensource/display/weston"

inherit meson pkgconfig useradd distro_features_check
DEPENDS = "libxkbcommon gdk-pixbuf pixman cairo glib-2.0 binder"
DEPENDS += "wayland wayland-protocols libinput adreno gbm pango wayland-native"
DEPENDS += "libsync display-hal-linux display-commonsys"

EXTRA_OEMESON += "-Ddeprecated-wl-shell=true"
EXTRA_OEMESON += "-Dbackend-default=auto -Dbackend-rdp=false -Dpipewire=false"

RRECOMMENDS:${PN} = "weston-launch liberation-fonts"

REQUIRED_DISTRO_FEATURES:remove = "opengl"
REQUIRED_DISTRO_FEATURES:remove = "pam"

PACKAGECONFIG ??= ""
# Weston on SDM
PACKAGECONFIG[sdm] = "-Dbackend-sdm=true,-Dbackend-sdm=false"
# Weston with disabling display power key
PACKAGECONFIG[disablepowerkey] = "-Ddisable-power-key=true,-Ddisable-power-key=false"

LDFLAGS  += "-lcutils -ldrmutils -ldisplaydebug -lglib-2.0 -lgbmutils -lutils -lbinder"

#meson script's CPP flags
CXXFLAGS += "-I${STAGING_INCDIR}/sdm"
CXXFLAGS += "-I${STAGING_INCDIR}/display/display"
CFLAGS:append:sun += "-Wno-error=incompatible-pointer-types \
                      -Wno-error=implicit-function-declaration \
                      -Wno-error=int-conversion \
                      -Wno-error=return-mismatch"

# select compositor, enable simple and demo clients and enable EGL
PACKAGECONFIG:append:kalama = "sdm clients egl shell-desktop disablepowerkey screenshare \
                               shell-fullscreen shell-ivi image-jpeg"

do_install:append:kalama() {
    install -m 0644 ${WORKDIR}/weston-kalama.ini -D ${D}${sysconfdir}/xdg/weston/weston.ini
}

PACKAGECONFIG:append:pineapple = "sdm clients shell-desktop disablepowerkey screenshare \
                                  shell-fullscreen shell-ivi image-jpeg"

do_install:append:pineapple() {
    install -m 0644 ${WORKDIR}/weston-pineapple.ini -D ${D}${sysconfdir}/xdg/weston/weston.ini
}

PACKAGECONFIG:append:sun = "kms egl clients shell-desktop disablepowerkey screenshare \
                                  shell-fullscreen shell-ivi image-jpeg"

do_install:append:sun() {
    install -m 0644 ${WORKDIR}/weston-sun.ini -D ${D}${sysconfdir}/xdg/weston/weston.ini
}

LDFLAGS:remove:sun += "-ldrmutils -ldisplaydebug"
DEPENDS:remove:sun += "display-hal-linux display-commonsys"

FILES:${PN} += "${bindir}/*"
FILES:${PN} += " ${libdir}/*.so"
FILES:${PN} += "${sysconfdir}/xdg/weston/weston.ini"
