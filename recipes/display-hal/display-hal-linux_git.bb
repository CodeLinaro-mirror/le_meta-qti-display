inherit autotools pkgconfig

DESCRIPTION = "display Library"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

PR = "r8"

PACKAGES = "${PN}"

FILESPATH   =+ "${WORKSPACE}:"
SRC_URI     =  "file://hardware/qcom/display"

S = "${WORKDIR}/hardware/qcom/display/"

DEPENDS += "virtual/kernel libdrm drm binder displaydlkm"

LDFLAGS += "-llog -lutils -lcutils"

PACKAGECONFIG[gbm] = "--with-gbm, --without-gbm, gbm, gbm"
PACKAGECONFIG[fbdev] = "--enable-sdmhalfb, --disable-sdmhalfb"
PACKAGECONFIG[drm] = "--enable-sdmhaldrm, --disable-sdmhaldrm, libdrm, libdrm"
PACKAGECONFIG[adreno] = "--enable-adreno, --disable-adreno, adreno, adreno"

LDFLAGS += "-llog -lhardware -lutils -lcutils"

CPPFLAGS_append_apq8098 += "-DCOMPILE_DRM"
CPPFLAGS_append_qcs605 += "-DCOMPILE_DRM"
CPPFLAGS_append_sdm845 += "-DCOMPILE_DRM"
CPPFLAGS_append_sdmsteppe += "-DCOMPILE_DRM"
CPPFLAGS_append_qrb5165 += "-DCOMPILE_DRM"
CPPFLAGS_append_qrbx210 += "-DCOMPILE_DRM"
CPPFLAGS_append_qcs610 += "-DCOMPILE_DRM"
CPPFLAGS_append_sxr2130-mtp += "-DCOMPILE_DRM"

CFLAGS += "-I${STAGING_KERNEL_BUILDDIR}/usr/include"
CPPFLAGS += "-I${STAGING_KERNEL_BUILDDIR}/usr/include"
CPPFLAGS += "-DVENUS_COLOR_FORMAT"
CPPFLAGS += "-DPAGE_SIZE=4096"
CPPFLAGS += "-D__GBM__"
CPPFLAGS_append_apq8098 += "-I${WORKSPACE}/display/display-hal/libdrmutils"
CPPFLAGS_append_qcs605 += "-I${WORKSPACE}/display/display-hal/libdrmutils"
CPPFLAGS_append_sdm845 += "-I${WORKSPACE}/display/display-hal/libdrmutils"
CPPFLAGS_append_sdmsteppe += "-I${WORKSPACE}/display/display-hal/libdrmutils"
CPPFLAGS_append_qrb5165 += "-I${S}/libdebug"
CPPFLAGS_append_qrb5165 += "-I${S}/libdrmutils"
CPPFLAGS_append_qrbx210 += "-I${S}/libdebug"
CPPFLAGS_append_qrbx210 += "-I${S}/libdrmutils"
CPPFLAGS_append_qcs610 += "-I${S}/libdebug"
CPPFLAGS_append_qcs610 += "-I${S}/libdrmutils"
CPPFLAGS_append_sxr2130-mtp += "-I${S}/libdebug"
CPPFLAGS_append_sxr2130-mtp += "-I${S}/libdrmutils"

CPPFLAGS += "-I${S}gpu_tonemapper"
CPPFLAGS += "-I${S}sdm/include"
CPPFLAGS += "-I${S}include"
CPPFLAGS += "-I${WORKSPACE}/system/core/include"
CPPFLAGS_append_robot-som += "-I${S}libqservice"
CPPFLAGS_append_robot-som += "-I${S}libgralloc"
CPPFLAGS_append_robot-som += "-I${S}libqdutils"
CPPFLAGS_append_apq8098 += "-I${STAGING_INCDIR}/libdrm"
CPPFLAGS_append_apq8098 += "-I${STAGING_INCDIR}/gbm"
CPPFLAGS_append_qcs605 += "-I${STAGING_INCDIR}/libdrm"
CPPFLAGS_append_qcs605 += "-I${STAGING_INCDIR}/gbm"
CPPFLAGS_append_sdm845 += "-I${STAGING_INCDIR}/libdrm"
CPPFLAGS_append_sdm845 += "-I${STAGING_INCDIR}/gbm"
CPPFLAGS_append_sdmsteppe += "-I${STAGING_INCDIR}/libdrm"
CPPFLAGS_append_sdmsteppe += "-I${STAGING_INCDIR}/gbm"
CPPFLAGS_append_apq8098 += "-I${STAGING_INCDIR}/adreno"
CPPFLAGS_append_qrb5165 += "-I${STAGING_INCDIR}/libdrm"
CPPFLAGS_append_qrb5165 += "-I${STAGING_INCDIR}/gbm"
CPPFLAGS_append_qrbx210 += "-I${STAGING_INCDIR}/libdrm"
CPPFLAGS_append_qrbx210 += "-I${STAGING_INCDIR}/gbm"
CPPFLAGS_append_qcs610 += "-I${STAGING_INCDIR}/libdrm"
CPPFLAGS_append_qcs610 += "-I${STAGING_INCDIR}/gbm"
CPPFLAGS_append_sxr2130-mtp += "-I${STAGING_INCDIR}/libdrm"
CPPFLAGS_append_sxr2130-mtp += "-I${STAGING_INCDIR}/gbm"

do_configure[depends] += "virtual/kernel:do_shared_workdir"

do_install:append () {
    cp -fR ${S}/include/* ${STAGING_INCDIR}/
}

FILES_${PN} += "${libdir}/* ${includedir}"
FILES_${PN} += "${libdir}/hw/gralloc.default.so"
INSANE_SKIP_${PN} = "dev-so"
