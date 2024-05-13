inherit autotools pkgconfig

DESCRIPTION = "display Library"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

PR = "r8"

FILESPATH   =+ "${WORKSPACE}:"
SRC_URI     =  "file://display/vendor/qcom/opensource/display-core"

S = "${WORKDIR}/display/vendor/qcom/opensource/display-core"
CONFIG_PATH = "${WORKSPACE}/display/vendor/qcom/opensource/display-core/config"

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
  install -d ${D}/usr/data/display
  install -m 0644 ${CONFIG_PATH}/qdcm_calib_data_nt37801_amoled_cmd_mode_dsi_csot_panel_with_DSC_TVM.json \
-D ${D}/usr/data/display/qdcm_calib_data_nt37801_amoled_cmd_mode_dsi_csot_panel_with_DSC.json
  install -m 0644 ${CONFIG_PATH}/snapdragon_color_libs_config.xml \
-D ${D}/usr/data/display/snapdragon_color_libs_config.xml
}
do_configure[depends] += "virtual/kernel:do_shared_workdir"

SOLIBS = ".so"
FILES_SOLIBSDEV = ""

FILES:${PN} += "/usr/data/display/qdcm_calib_data_nt37801_amoled_cmd_mode_dsi_csot_panel_with_DSC.json"
FILES:${PN} += "/usr/data/display/snapdragon_color_libs_config.xml"
