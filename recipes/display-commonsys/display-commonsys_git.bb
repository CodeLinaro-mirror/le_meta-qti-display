inherit autotools pkgconfig

PACKAGE_ARCH = "${MACHINE_ARCH}"
DESCRIPTION = "display Library"

LICENSE = "BSD-3-Clause & BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
BSD-3-Clause;md5=550794465ba0ec5312d6919e203a55f9"
LIC_FILES_CHKSUM += "file://${COREBASE}/meta/files/common-licenses/\
BSD-3-Clause-Clear;md5=7a434440b651f4a472ca93716d01033a"

PR = "r0"

FILESPATH   =+ "${WORKSPACE}:"
SRC_URI     =  "file://display/vendor/qcom/opensource/commonsys-intf/display"

S = "${WORKDIR}/display/vendor/qcom/opensource/commonsys-intf/display/"

TOOLCHAIN = "sdllvm"
ALLOW_EMPTY:${PN} = "1"
INSANE_SKIP:${PN} = "dev-so"
