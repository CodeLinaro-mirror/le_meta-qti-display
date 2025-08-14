SUMMARY = "Weston, a Wayland compositor"
DESCRIPTION = "Weston is the reference implementation of a Wayland compositor"
HOMEPAGE = "https://git.codelinaro.org/"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=d79ee9e66bb0f95d3386a7acae780b70"

FILESEXTRAPATHS:prepend := "${THISDIR}/weston-launch:"

FILESPATH =+ "${WORKSPACE}:"
SRC_URI = " file://display/vendor/qcom/opensource/display/weston/ \
            file://systemd-notify.weston-start \
            file://weston-sun.ini \
            file://weston-kera.ini \
           "

S = "${WORKDIR}/display/vendor/qcom/opensource/display/weston"

inherit meson pkgconfig useradd features_check
DEPENDS += "libdmabufheap gbm adreno"

EXTRA_OEMESON += "-Dbackend-default=auto -Dbackend-rdp=false -Dpipewire=false"

RRECOMMENDS:${PN} = "weston-launch liberation-fonts"

REQUIRED_DISTRO_FEATURES:remove = "opengl"
REQUIRED_DISTRO_FEATURES:remove = "pam"

LDFLAGS  += "-lcutils -lglib-2.0 -lutils"

#meson script's CPP flags
CXXFLAGS += "-I${STAGING_INCDIR}/display/display"
CFLAGS:append:sun += "-Wno-error=incompatible-pointer-types \
                      -Wno-error=implicit-function-declaration \
                      -Wno-error=int-conversion"

CFLAGS:append:kera += "-Wno-error=incompatible-pointer-types \
                      -Wno-error=implicit-function-declaration \
                      -Wno-error=int-conversion"

PACKAGECONFIG: = " \
                 egl \
                 clients \
                 shell-desktop \
                 screenshare \
                 shell-fullscreen \
                 shell-ivi \
                 image-jpeg \
                 ${@bb.utils.contains('DISTRO_FEATURES', 'x11', 'x11 xwayland', '', d)} \
                 "
PACKAGECONFIG:append:sun = "kms"

do_install:append:sun() {
    install -m 0644 ${WORKDIR}/weston-sun.ini -D ${D}${sysconfdir}/xdg/weston/weston.ini
}

PACKAGECONFIG:append:kera = "kms"

do_install:append:kera() {
    install -m 0644 ${WORKDIR}/weston-kera.ini -D ${D}${sysconfdir}/xdg/weston/weston.ini
}

FILES:${PN} += "${bindir}/*"
FILES:${PN} += " ${libdir}/*.so"
FILES:${PN} += " ${libdir}/libweston-13/*.so"
FILES:${PN} += "${sysconfdir}/xdg/weston/weston.ini"
