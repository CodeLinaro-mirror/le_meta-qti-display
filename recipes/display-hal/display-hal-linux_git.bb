inherit autotools pkgconfig

PACKAGE_ARCH = "${MACHINE_ARCH}"

DESCRIPTION = "display Library"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

PR = "r8"

FILESPATH   =+ "${WORKSPACE}:"
SRC_URI = "file://display/vendor/qcom/opensource/display-core"

S = "${WORKDIR}/display/vendor/qcom/opensource/display-core"

DEPENDS += "virtual/kernel libdrm binder displaydlkm"
DEPENDS += "linux-msm-headers display-commonsys libcutils"

LDFLAGS += "-llog -lutils -lcutils"

PACKAGECONFIG[drm] = "--enable-sdmhaldrm, --disable-sdmhaldrm, libdrm, libdrm"
EXTRA_OECONF += " --with-sanitized-headers=${STAGING_INCDIR}/linux-msm/usr/include"



CPPFLAGS += "-I${S}/libdebug"
CPPFLAGS += "-I${S}/libdrmutils"
CPPFLAGS += "-I${S}/sdm/include"
CPPFLAGS += "-I${S}/include"
CPPFLAGS += "-I${STAGING_INCDIR}/libdrm"
CPPFLAGS += "-DTRUSTED_VM"
CPPFLAGS += "-fno-operator-names"


SOLIBS = ".so"
FILES_SOLIBSDEV = ""
