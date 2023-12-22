DESCRIPTION = "QTI Display drivers"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta-qti-bsp/files/common-licenses/\
${LICENSE};md5=801f80980d171dd6425610833a22dbe6"

inherit linux-kernel-base deploy

PR = "r0"

DEPENDS += "rsync-native"

do_configure[depends] += "virtual/kernel:do_shared_workdir"

# TODO: Remove this local definition once available via machine.conf
KERNEL_DEFCONFIG ?= "neo_le-defconfig"
KERNEL_DEFCONFIG_qti-distro-debug ?= "neo_le-debug_defconfig"

FILESEXTRAPATHS:prepend := "${WORKSPACE}:"
SRC_URI     =  "file://display/vendor/qcom/opensource/display-drivers/"
SRC_URI    +=  "file://kernel-5.10/kernel_platform"
SRC_URI    +=  "file://kernel-5.10/out/${KERNEL_DEFCONFIG}"
SRC_URI    +=  "file://display_load.conf"

S = "${WORKDIR}/display/vendor/qcom/opensource/display-drivers"
KERNEL_VERSION = "${@get_kernelversion_file("${STAGING_KERNEL_BUILDDIR}")}"
EXTRA_OEMAKE += "TARGET_SUPPORT=${BASEMACHINE}"

# Disable parallel make
PARALLEL_MAKE = ""

# Disable parallel make
PARALLEL_MAKE = "-j1"

do_configure[noexec] = "1"

do_compile() {
    PATH=${STAGING_BINDIR_NATIVE}:$PATH \
    # Ensure right make file is in use
    cp -f ${S}/Makefile.am ${S}/Makefile
    cd ${WORKDIR}/kernel-5.10/kernel_platform  && \
    BUILD_CONFIG=msm-kernel/${KERNEL_CONFIG} \
    EXT_MODULES=../../display/vendor/qcom/opensource/display-drivers \
    ROOTDIR=${WORKDIR}/display/ \
    MODULE_DRM_MSM=m \
    MODULE_OUT=${S} \
    OUT_DIR=${WORKDIR}/kernel-5.10/out/${KERNEL_DEFCONFIG} \
    KERNEL_UAPI_HEADERS_DIR=${STAGING_KERNEL_BUILDDIR} \
    INSTALL_MODULE_HEADERS=1 \
    ./build/build_module.sh
}

do_install() {
	install -m 0755 ${S}/msm/msm_drm.ko -D ${D}${base_libdir}/modules/${KERNEL_VERSION}/msm_drm.ko
	install -m 0755 ${WORKDIR}/display_load.conf -D ${D}${sysconfdir}/modules-load.d/display_load.conf
}

do_deploy() {
# Deploy unstripped kernel modules into ${DEPLOYDIR}/kernel_modules for debugging purposes
    install -d ${DEPLOYDIR}/kernel_modules
    for kmod in $(find ${D} -name "*.ko") ; do
        install -m 0644 $kmod ${DEPLOYDIR}/kernel_modules
    done
}

addtask deploy after do_install before do_package

FILES:${PN} += "${sysconfdir}/*"
FILES:${PN} += "${base_libdir}/modules/*"
