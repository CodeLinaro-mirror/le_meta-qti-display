inherit autotools pkgconfig

PACKAGE_ARCH = "${MACHINE_ARCH}"
DESCRIPTION = "display Library"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=3775480a712fc46a69647678acb234cb"

PR = "r8"

FILESPATH   =+ "${WORKSPACE}:"
SRC_URI     =  "file://display/hardware/qcom/display"

S = "${WORKDIR}/display/hardware/qcom/display/"

EXTRA_OECONF += " --with-sanitized-headers=${STAGING_INCDIR}/linux-msm/usr/include"

PACKAGECONFIG ?= " \
                 ${@bb.utils.contains('COMBINED_FEATURES', 'drm', 'drm', '', d)} \
                 "

PACKAGECONFIG[drm] = "--enable-sdmhaldrm, --disable-sdmhaldrm, libdrm, libdrm"

DEPENDS += "libhardware linux-msm-headers displaydlkm display-commonsys dbus"

TOOLCHAIN = "sdllvm"
SOLIBS = ".so"
FILES_SOLIBSDEV = ""
