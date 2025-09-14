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
DEPENDS += "libdmabufheap gbm adreno display-hal-linux display-commonsys"

EXTRA_OEMESON += "-Dbackend-default=auto -Dbackend-rdp=false -Dpipewire=false -Dbackend-headless=true"

RRECOMMENDS:${PN} = "weston-launch liberation-fonts"

REQUIRED_DISTRO_FEATURES:remove = "pam"

# Weston on SDM
PACKAGECONFIG[sdm] = "-Dbackend-sdm=true,-Dbackend-sdm=false"
PACKAGECONFIG[rdp] = "-Dbackend-rdp=true,-Dbackend-rdp=false,freerdp"
PACKAGECONFIG[screenshare] = "-Dscreenshare=true,-Dscreenshare=false"
# Weston with disabling display power key
PACKAGECONFIG[disablepowerkey] = "-Ddisable-power-key=true,-Ddisable-power-key=false"

LDFLAGS  += "-lcutils -ldrmutils -ldisplaydebug -lglib-2.0 -lgbmutils -lutils"

#meson script's CPP flags
CXXFLAGS += "-I${STAGING_INCDIR}/sdm"
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
PACKAGECONFIG:append:sun = "sdm headless disablepowerkey"

do_install:append:sun() {
    install -m 0644 ${WORKDIR}/weston-sun.ini -D ${D}${sysconfdir}/xdg/weston/weston.ini
}

PACKAGECONFIG:append:kera = "sdm disablepowerkey"

do_install:append:kera() {
    install -m 0644 ${WORKDIR}/weston-kera.ini -D ${D}${sysconfdir}/xdg/weston/weston.ini
}

FILES:${PN} += "${bindir}/*"
FILES:${PN} += " ${libdir}/*.so"
FILES:${PN} += " ${libdir}/libweston-13/*.so"
FILES:${PN} += "${sysconfdir}/xdg/weston/weston.ini"
