inherit autotools pkgconfig

DESCRIPTION = "display Library"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=3775480a712fc46a69647678acb234cb"

PR = "r8"

FILESPATH   =+ "${WORKSPACE}:"
SRC_URI     =  "file://hardware/qcom/display"

S = "${WORKDIR}/hardware/qcom/display"

DEPENDS += " libhardware virtual/kernel libdrm drm binder displaydlkm"

LDFLAGS += "-llog -lhardware -lutils -lcutils"

PACKAGECONFIG[drm] = "--enable-sdmhaldrm, --disable-sdmhaldrm, libdrm, libdrm"

CPPFLAGS += "-I${S}/libdrmutils"
CPPFLAGS += "-I${S}/sdm/include"
CPPFLAGS += "-I${S}/include"
CPPFLAGS += "-I${WORKSPACE}/system/core/include"
CPPFLAGS += "-I${WORKSPACE}/system/core/libsync/include"
CPPFLAGS += "-I${WORKSPACE}/system/core/libion/include"
CPPFLAGS += "-I${S}/libqdutils"
CPPFLAGS += "-I${S}/libqservice"
CPPFLAGS += "-I${WORKSPACE}/vendor/qcom/opensource/commonsys-intf/display/include"
CPPFLAGS += "-I${S}/libdebug"
CPPFLAGS += "-I${STAGING_INCDIR}/libdrm"
CPPFLAGS += "-I${STAGING_KERNEL_BUILDDIR}/usr/include"
CPPFLAGS += "-DTRUSTED_VM"

do_install_append () {
    cp -fR ${S}/include/* ${STAGING_INCDIR}/
}
do_configure[depends] += "virtual/kernel:do_shared_workdir"

SOLIBS = ".so"
FILES_SOLIBSDEV = ""
