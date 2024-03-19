inherit autotools pkgconfig systemd

PACKAGE_ARCH = "${MACHINE_ARCH}"
DESCRIPTION = "display Library"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

PR = "r8"

FILESEXTRAPATHS:prepend := "${WORKSPACE}:"
SRC_URI     =  "file://display/hardware/qcom/display \
                file://display_hal.service \
               "

S = "${WORKDIR}/display/hardware/qcom/display/"

EXTRA_OECONF += " --with-sanitized-headers=${STAGING_INCDIR}/linux-msm/usr/include"

PACKAGECONFIG ?= " \
                 ${@bb.utils.contains('COMBINED_FEATURES', 'drm', 'drm', '', d)} \
                 "

PACKAGECONFIG[drm] = "--enable-sdmhaldrm, --disable-sdmhaldrm, libdrm, libdrm"

DEPENDS += "linux-msm-headers \
            displaydlkm-headers \
            display-commonsys \
            dbus \
            binder \
            lz4 \
            gbm \
            libsync"

do_install:append() {
  install -d -m 0755 ${D}${bindir}/
  install -d ${D}${systemd_unitdir}/system/
  install -d ${D}${systemd_unitdir}/system/multi-user.target.wants
  install -m 0644 ${WORKDIR}/display_hal.service \
                  -D ${D}${systemd_unitdir}/system/display_hal.service
}

INSANE_SKIP:${PN} += "installed-vs-shipped"
FILES:${PN} += "${sysconfdir}/*"
FILES:${PN} += "${systemd_unitdir}/system/display_hal.service"
FILES:${PN} += "${systemd_unitdir}/system/multi-user.target.wants/display_hal.service"
SYSTEMD_SERVICE:${PN} = "display_hal.service"
TOOLCHAIN = "sdllvm"
SOLIBS = ".so"
FILES_SOLIBSDEV = ""
