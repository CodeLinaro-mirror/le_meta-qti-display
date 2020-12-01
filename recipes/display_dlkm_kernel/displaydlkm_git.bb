DESCRIPTION = "QTI Display drivers"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=3775480a712fc46a69647678acb234cb"

inherit linux-kernel-base

PR = "r0"

DEPENDS = "virtual/kernel rsync-native"

FILESPATH   =+ "${WORKSPACE}:"
SRC_URI     =  "file://vendor/qcom/opensource/display-drivers/"
SRC_URI    +=  "file://display_load.conf"

S = "${WORKDIR}/vendor/qcom/opensource/display-drivers"

KERNEL_VERSION = "${@get_kernelversion_headers('${STAGING_KERNEL_BUILDDIR}')}"

EXTRA_OEMAKE += "TARGET_SUPPORT=${BASEMACHINE}"

# Disable parallel make
PARALLEL_MAKE = ""

# Disable parallel make
PARALLEL_MAKE = "-j1"

do_configure() {
	cp -f ${WORKSPACE}/vendor/qcom/opensource/display-drivers/Makefile.am ${WORKSPACE}/vendor/qcom/opensource/display-drivers/Makefile
}

do_compile() {
    cd ${WORKSPACE}/kernel-${PREFERRED_VERSION_linux-msm}/kernel_platform  && \
    BUILD_CONFIG=common/build.config.msm.*.tuivm \
    EXT_MODULES=../../vendor/qcom/opensource/display-drivers \
    ROOTDIR=${WORKSPACE}/ \
    MODULE_DRM_MSM=m \
    MODULE_OUT=${WORKDIR}/vendor/qcom/opensource/display-drivers \
    OUT_DIR=${WORKSPACE}/kernel-${PREFERRED_VERSION_linux-msm}/out/msm-*-*-${KERNEL_VARIANT}defconfig/ \
    KERNEL_UAPI_HEADERS_DIR=${STAGING_KERNEL_BUILDDIR} \
    INSTALL_MODULE_HEADERS=1 \
    ./build/build_module.sh
}

do_install() {
	install -d ${D}/usr/lib/modules/${KERNEL_VERSION}/vendor/qcom/opensource/display-drivers/msm/
	install -m 0755 ${WORKDIR}/display_load.conf -D ${D}${sysconfdir}/modules-load.d/display_load.conf
	install -m 0755 ${WORKDIR}/vendor/qcom/opensource/display-drivers/msm/msm_drm.ko -D ${D}${libdir}/modules/${KERNEL_VERSION}/msm_drm.ko
	cp -r ${WORKDIR}/vendor/qcom/opensource/display-drivers/usr/include/display ${STAGING_KERNEL_BUILDDIR}/usr/include/display
}

# The inherit of module.bbclass will automatically name module packages with
# kernel-module-" prefix as required by the oe-core build environment. Also it
# replaces '_' with '-' in the module name.

RPROVIDES_${PN} += "${@'kernel-module-msm-drm-${KERNEL_VERSION}'.replace('_', '-')}"

FILES_${PN} += "${sysconfdir}/*"
FILES_${PN} += "${libdir}/modules/${KERNEL_VERSION}/*"
