inherit autotools pkgconfig

PACKAGE_ARCH = "${MACHINE_ARCH}"
DESCRIPTION = "display kernel headers"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

PR = "r0"

FILESEXTRAPATHS:prepend := "${WORKSPACE}:"
SRC_URI     =  "file://display/vendor/qcom/opensource/display-drivers/include"

S = "${WORKDIR}/display/vendor/qcom/opensource/display-drivers/include"

TOOLCHAIN = "sdllvm"
ALLOW_EMPTY:${PN} = "1"
INSANE_SKIP:${PN} = "dev-so"
