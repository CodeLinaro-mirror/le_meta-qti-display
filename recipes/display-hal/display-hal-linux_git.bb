inherit autotools pkgconfig

DESCRIPTION = "display Library"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

PR = "r8"

FILESPATH   =+ "${WORKSPACE}:"
SRC_URI     =  "file://display/vendor/qcom/opensource/display-core"

S = "${WORKDIR}/display/vendor/qcom/opensource/display-core"

DEPENDS += " libhardware virtual/kernel libdrm drm binder displaydlkm"

LDFLAGS += "-llog -lhardware -lutils -lcutils"

PACKAGECONFIG[drm] = "--enable-sdmhaldrm, --disable-sdmhaldrm, libdrm, libdrm"

CPPFLAGS += "-I${S}/libdrmutils"
CPPFLAGS += "-I${S}/sdm/include"
CPPFLAGS += "-I${S}/include"
CPPFLAGS += "-I${WORKSPACE}/system/core/include"
CPPFLAGS += "-I${WORKSPACE}/system/core/libsync/include"
CPPFLAGS += "-I${WORKSPACE}/system/core/libion/include"
CPPFLAGS += "-I${WORKSPACE}/display/vendor/qcom/opensource/commonsys-intf/display/include"
CPPFLAGS += "-I${WORKSPACE}/display/vendor/qcom/opensource/display-intf/common"
CPPFLAGS += "-I${WORKSPACE}/display/vendor/qcom/opensource/display-intf/snapalloc/"
CPPFLAGS += "-I${S}/libdebug"
CPPFLAGS += "-I${STAGING_INCDIR}/libdrm"
CPPFLAGS += "-I${STAGING_KERNEL_BUILDDIR}/usr/include"
CPPFLAGS += "-DTRUSTED_VM"
CPPFLAGS += "-fno-operator-names"

do_install:append () {
    cp -fR ${S}/include/* ${STAGING_INCDIR}/
}
do_configure[depends] += "virtual/kernel:do_shared_workdir"

SOLIBS = ".so"
FILES_SOLIBSDEV = ""
