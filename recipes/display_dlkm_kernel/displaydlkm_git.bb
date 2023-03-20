DESCRIPTION = "QTI Display drivers"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=801f80980d171dd6425610833a22dbe6"

inherit linux-kernel-base deploy

PACKAGE_ARCH = "${MACHINE_ARCH}"

PR = "r0"

DEPENDS += "virtual/kernel displaydlkm-headers"
DEPENDS:append += "mmdlkm mmrm-kernel"

do_configure[depends] += "virtual/kernel:do_shared_workdir"

FILESEXTRAPATHS:prepend := "${WORKSPACE}:"
SRC_URI    =  "file://display/vendor/qcom/opensource/display-drivers/"
SRC_URI    +=  "file://start_display_le"
SRC_URI    +=  "file://display@.service"
SRC_URI    +=  "file://display_load.conf"
KERNEL_VERSION = "${@get_kernelversion_file("${STAGING_KERNEL_BUILDDIR}")}"

S = "${WORKDIR}/display/vendor/qcom/opensource/display-drivers"

EXT_MODULES = "${@os.path.relpath("${S}","${KERNEL_PLATFORM_PATH}")}"
EXTRA_OEMAKE += "TARGET_SUPPORT=${BASEMACHINE}"

# Disable parallel make
PARALLEL_MAKE = ""

# Disable parallel make
PARALLEL_MAKE = "-j1"

do_configure() {
	cp -f ${B}/Makefile.am ${B}/Makefile
}

do_compile() {
    cd ${KERNEL_PLATFORM_PATH}
    BUILD_CONFIG=msm-kernel/${KERNEL_CONFIG} \
    KERNEL_KIT=${KERNEL_PREBUILT_PATH} \
    OUT_DIR=${WORKDIR}/out/${KERNEL_DEFCONFIG} \
    EXT_MODULES=${EXT_MODULES} \
    ROOTDIR=${WORKDIR}/ \
    MODULE_DRM_MSM=m \
    MODULE_DRM_LT9611UXC=m \
    INPLACE_COMPILE=y \
    MODULE_OUT=${WORKDIR}/display/vendor/qcom/opensource/display-drivers \
    KERNEL_UAPI_HEADERS_DIR=${STAGING_KERNEL_BUILDDIR} \
    ./build/build_module.sh \
    KBUILD_EXTRA_SYMBOLS=${STAGING_DIR_HOST}/lib/modules/${KERNEL_VERSION}/mm-drivers/Module.symvers \
    KBUILD_EXTRA_SYMBOLS+=${STAGING_DIR_HOST}/lib/modules/${KERNEL_VERSION}/Module.symvers
}

do_install() {
	install -d ${D}${sysconfdir}/initscripts
	install -m 755 ${WORKDIR}/start_display_le ${D}${sysconfdir}/initscripts
	install -d ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}
	install -m 0755 ${B}/msm/msm_drm.ko -D ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}
	install -m 0755 ${B}/bridge-drivers/lt9611uxc.ko -D ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}
	install -m 0644 ${WORKDIR}/display@.service -D ${D}${systemd_unitdir}/system/display@.service
	install -m 0755 ${WORKDIR}/display_load.conf -D ${D}${sysconfdir}/modules-load.d/display_load.conf
}

do_deploy() {
        install -d ${DEPLOYDIR}/kernel_modules
        cp -rp ${B}/msm/msm_drm.ko ${DEPLOYDIR}/kernel_modules
        cp -rp ${B}/bridge-drivers/lt9611uxc.ko ${DEPLOYDIR}/kernel_modules
}

addtask do_deploy after do_install

FILES:${PN} += "${sysconfdir}/*"
FILES:${PN} += "/etc/initscripts/start_display_le"
FILES:${PN} += "${systemd_unitdir}/system/display@.service"
FILES:${PN} += "${nonarch_base_libdir}/modules/${KERNEL_VERSION}/*"
