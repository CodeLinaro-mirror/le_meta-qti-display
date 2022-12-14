inherit autotools pkgconfig systemd

PACKAGE_ARCH = "${MACHINE_ARCH}"
DESCRIPTION = "display Library"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=3775480a712fc46a69647678acb234cb"

PR = "r8"

FILESPATH   =+ "${WORKSPACE}:"
SRC_URI     =  "file://display/hardware/qcom/display \
                file://display_hal.service \
               "

S = "${WORKDIR}/display/hardware/qcom/display/"

EXTRA_OECONF += " --with-sanitized-headers=${STAGING_INCDIR}/linux-msm/usr/include"

PACKAGECONFIG ?= " \
                 ${@bb.utils.contains('COMBINED_FEATURES', 'drm', 'drm', '', d)} \
                 "

PACKAGECONFIG[drm] = "--enable-sdmhaldrm, --disable-sdmhaldrm, libdrm, libdrm"

DEPENDS += "libhardware linux-msm-headers displaydlkm display-commonsys dbus lz4"

do_install_append() {
  install -d -m 0755 ${D}${bindir}/
  install -d ${D}${systemd_unitdir}/system/
  install -d ${D}${systemd_unitdir}/system/multi-user.target.wants
  install -m 0644 ${WORKDIR}/display_hal.service \
                  -D ${D}${systemd_unitdir}/system/display_hal.service
}

FILES_${PN} += "${sysconfdir}/*"
FILES_${PN} += "${systemd_unitdir}/system/display_hal.service"
FILES_${PN} += "${systemd_unitdir}/system/multi-user.target.wants/display_hal.service"
SYSTEMD_SERVICE_${PN} = "display_hal.service"
TOOLCHAIN = "sdllvm"
SOLIBS = ".so"
FILES_SOLIBSDEV = ""
