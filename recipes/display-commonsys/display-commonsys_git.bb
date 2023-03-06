inherit autotools pkgconfig

PACKAGE_ARCH = "${MACHINE_ARCH}"
DESCRIPTION = "display Library"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

PR = "r0"

FILESPATH   =+ "${WORKSPACE}:"
SRC_URI     =  "file://vendor/qcom/opensource/commonsys-intf/display"

S = "${WORKDIR}/vendor/qcom/opensource/commonsys-intf/display/"

TOOLCHAIN = "sdllvm"
ALLOW_EMPTY:${PN} = "1"
INSANE_SKIP:${PN} = "dev-so"
