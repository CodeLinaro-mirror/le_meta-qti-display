inherit autotools pkgconfig

DESCRIPTION = "display Library"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

PR = "r8"

FILESPATH   =+ "${WORKSPACE}:"
SRC_URI     =  "file://hardware/qcom/display"

S = "${WORKDIR}/hardware/qcom/display"

DEPENDS += "virtual/kernel binder libcutils displaydlkm"

LDFLAGS += "-llog -lutils -lcutils"

PACKAGECONFIG[drm] = ",, libdrm, libdrm"
PACKAGECONFIG[hdr] = "--enable-hdr, --disable-hdr"
PACKAGECONFIG[gbm] = ",, gbm, gbm"
PACKAGECONFIG[adreno] = ",, adreno, adreno"

PACKAGECONFIG ?= "gbm \
                 adreno \
                 hdr \
                 ${@bb.utils.contains('COMBINED_FEATURES', 'drm', 'drm', '', d)} \
                 "


do_configure:append(){
        if [ ${@bb.utils.contains('ARMPKGARCH', 'armv7a','true','', d)} ]; then
                mkdir -p "${PKG_CONFIG_SYSROOT_DIR}/usr/include/display/"
                cp -r "${GIT_CEILING_DIRECTORIES}/recipe-sysroot/usr/include/display/drm"  "${PKG_CONFIG_SYSROOT_DIR}/usr/include/display/"
                cp -r "${GIT_CEILING_DIRECTORIES}/recipe-sysroot/usr/include/display/media"  "${PKG_CONFIG_SYSROOT_DIR}/usr/include/display/"
                cp -r "${GIT_CEILING_DIRECTORIES}/recipe-sysroot/usr/include/display/hdcp"  "${PKG_CONFIG_SYSROOT_DIR}/usr/include/display/"
        fi
}



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
CPPFLAGS += "-I${STAGING_INCDIR}/display/drm"
CPPFLAGS += "-I${STAGING_KERNEL_BUILDDIR}/usr/include"
CPPFLAGS += "-DTRUSTED_VM"
CPPFLAGS += "-fno-operator-names"
CFLAGS:append = " -D_FILE_OFFSET_BITS=64"
CPPFLAGS:append = " -D_FILE_OFFSET_BITS=64"

do_install:append () {
    cp -fR ${S}/include/* ${STAGING_INCDIR}/
}
do_configure[depends] += "virtual/kernel:do_shared_workdir"

SOLIBS = ".so"
FILES_SOLIBSDEV = ""

