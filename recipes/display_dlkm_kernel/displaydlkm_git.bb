DESCRIPTION = "QTI Display drivers"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=801f80980d171dd6425610833a22dbe6"

inherit linux-kernel-base deploy

PACKAGE_ARCH = "${MACHINE_ARCH}"

PR = "r0"

do_compile[network] = "1"

# Add for DDK
DDK_BUILD ?= "false"
DEPENDS += "${@bb.utils.contains('DDK_BUILD', 'false', \
           'virtual/kernel displaydlkm-headers mmdlkm mmrm-kernel synx-kernel synx-kernel-header', 'mmdlkm', d)}"
OVERRIDES:append = "${@':ddk_build' if d.getVar('DDK_BUILD') == 'true' else ''}"

do_configure[depends] += "virtual/kernel:do_shared_workdir"

FILESEXTRAPATHS:prepend := "${WORKSPACE}:"
SRC_URI    =  "file://display/vendor/qcom/opensource/display-drivers/"
SRC_URI    +=  "file://start_display_le"
SRC_URI    +=  "file://display@.service"
SRC_URI    +=  "file://display_load.conf"
SRC_URI    +=  "file://display/vendor/qcom/opensource/mm-drivers/hw_fence/include"
SRC_URI    +=  "file://display/vendor/qcom/opensource/mm-drivers/msm_ext_display/include"
SRC_URI    +=  "file://display/vendor/qcom/opensource/mm-drivers/sync_fence/include"
SRC_URI    +=  "file://display/vendor/qcom/opensource/mm-drivers/hfi_core/inc"
KERNEL_VERSION = "${@get_kernelversion_file("${STAGING_KERNEL_BUILDDIR}")}"

S = "${WORKDIR}/display/vendor/qcom/opensource/display-drivers"

EXT_MODULES = "${@os.path.relpath("${S}","${KERNEL_PLATFORM_PATH}")}"
EXTRA_OEMAKE += "TARGET_SUPPORT=${BASEMACHINE}"

do_configure() {
	cp -f ${B}/Makefile.am ${B}/Makefile
	if ${@bb.utils.contains_any("BASEMACHINE", ["sun"], "true", "false", d)}; then
		sed -i '/CONFIG_HDCP_QSEECOM/d' ${B}/config/gki_sundisp.conf
		sed -i '/CONFIG_SMMU_PROXY/d' ${B}/config/gki_sundisp.conf
		sed -i '/CONFIG_DRM_MSM_HDMI/d' ${B}/config/gki_sundisp.conf
		sed -i '/CONFIG_HDCP_QSEECOM/d' ${B}/config/gki_sundispconf.h
		sed -i '/CONFIG_SMMU_PROXY/d' ${B}/config/gki_sundispconf.h
		sed -i '/CONFIG_DRM_MSM_HDMI/d' ${B}/config/gki_sundispconf.h
	fi

	if ${@bb.utils.contains_any("BASEMACHINE", ["kera"], "true", "false", d)}; then
		sed -i '/CONFIG_HDCP_QSEECOM/d' ${B}/config/gki_sundisp.conf
		sed -i '/CONFIG_SMMU_PROXY/d' ${B}/config/gki_sundisp.conf
		sed -i '/CONFIG_HDCP_QSEECOM/d' ${B}/config/gki_sundispconf.h
		sed -i '/CONFIG_SMMU_PROXY/d' ${B}/config/gki_sundispconf.h
	fi

        if ${@bb.utils.contains("BASEMACHINE", "alor", "true", "false", d)}; then
                sed -i '/CONFIG_HDCP_QSEECOM/d' ${B}/targets/canoe.bzl
        fi
}

do_compile() {
    cd ${KERNEL_PLATFORM_PATH}
    ENABLE_BUILD_PATH=y \
    BUILD_CONFIG=msm-kernel/${KERNEL_CONFIG} \
    KERNEL_KIT=${KERNEL_PREBUILT_PATH} \
    OUT_DIR=${WORKDIR}/out/${KERNEL_DEFCONFIG} \
    EXT_MODULES=${EXT_MODULES} \
    ROOTDIR=${WORKDIR}/ \
    MODULE_DRM_MSM=m \
    MODULE_DRM_LT9611UXC=m \
    MODULE_SYNX=y \
    INPLACE_COMPILE=y \
    MODULE_OUT=${WORKDIR}/display/vendor/qcom/opensource/display-drivers \
    KERNEL_UAPI_HEADERS_DIR=${STAGING_KERNEL_BUILDDIR} \
    LE_EXTRA_CFLAGS="-I${STAGING_DIR_HOST}/usr/include -I${STAGING_DIR_HOST}/usr/include/linux -I${WORKSPACE}/vendor/qcom/opensource/securemsm-kernel" \
    ./build/build_module.sh \
    KBUILD_EXTRA_SYMBOLS=${STAGING_DIR_HOST}/${nonarch_base_libdir}/modules/${KERNEL_VERSION}/mm-drivers/Module.symvers \
    KBUILD_EXTRA_SYMBOLS+=${STAGING_DIR_HOST}/${nonarch_base_libdir}/modules/${KERNEL_VERSION}/mmrm-kernel/Module.symvers \
    KBUILD_EXTRA_SYMBOLS+=${STAGING_DIR_HOST}/${nonarch_base_libdir}/modules/${KERNEL_VERSION}/synx-kernel/Module.symvers
}

#####Add for DDK
do_compile:ddk_build() {
    cd ${KERNEL_PLATFORM_PATH}
    ENABLE_DDK_BUILD=${DDK_BUILD} \
    TARGET_BOARD_PLATFORM=${TARGET_BOARD_PLATFORM} \
    VARIANT=${KERNEL_DEFCONFIG_VARIANT} \
    BUILD_CONFIG=${KERNEL_BUILD_CONFIG} \
    EXT_MODULES=${EXT_MODULES} \
    KERNEL_KIT=${KERNEL_PREBUILT_PATH} \
    OUT_DIR=${INTERMEDIAT_KERNEL_PATH} \
    MODULE_OUT=${WORKDIR}/display/vendor/qcom/opensource/display-drivers/msm \
    ./build/build_module.sh
}

do_install() {
    install -d ${D}${sysconfdir}/initscripts
    install -m 755 ${WORKDIR}/start_display_le ${D}${sysconfdir}/initscripts
    install -d ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}
    install -m 0755 ${B}/msm/msm_drm.ko -D ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}
    install -m 0644 ${WORKDIR}/display@.service -D ${D}${systemd_unitdir}/system/display@.service
    install -m 0755 ${WORKDIR}/display_load.conf -D ${D}${sysconfdir}/modules-load.d/display_load.conf
}

do_deploy() {
        install -d ${DEPLOYDIR}/kernel_modules
        cp -rp ${B}/msm/msm_drm.ko ${DEPLOYDIR}/kernel_modules
}

addtask do_deploy after do_install

FILES:${PN} += "${sysconfdir}/*"
FILES:${PN} += "/etc/initscripts/start_display_le"
FILES:${PN} += "${systemd_unitdir}/system/display@.service"
FILES:${PN} += "${nonarch_base_libdir}/modules/${KERNEL_VERSION}/*"
