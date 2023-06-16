DESCRIPTION = "QTI mm drivers"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=801f80980d171dd6425610833a22dbe6"

inherit linux-kernel-base deploy

PACKAGE_ARCH = "${MACHINE_ARCH}"

PR = "r0"

DEPENDS += "virtual/kernel"

SRCREV = "${AUTOREV}"

do_compile[depends] += "virtual/kernel:do_shared_workdir"
do_compile[cleandirs] += "${WORKDIR}/out/${KERNEL_DEFCONFIG}"

FILESEXTRAPATHS:prepend := "${WORKSPACE}:"
SRC_URI     =  "file://display/vendor/qcom/opensource/mm-drivers/"
KERNEL_VERSION = "${@get_kernelversion_file("${STAGING_KERNEL_BUILDDIR}")}"

S = "${WORKDIR}/display/vendor/qcom/opensource/mm-drivers"


EXT_MODULES = "${@os.path.relpath("${S}","${KERNEL_PLATFORM_PATH}")}"
EXTRA_OEMAKE += "TARGET_SUPPORT=${BASEMACHINE}"

# Disable parallel make
PARALLEL_MAKE = ""

# Disable parallel make
PARALLEL_MAKE = "-j1"

do_configure[noexec] = "1"

do_compile() {
    cd ${KERNEL_PLATFORM_PATH}
    BUILD_CONFIG=msm-kernel/${KERNEL_CONFIG} \
    KERNEL_KIT=${KERNEL_PREBUILT_PATH} \
    OUT_DIR=${WORKDIR}/out/${KERNEL_DEFCONFIG} \
    EXT_MODULES=${EXT_MODULES} \
    ROOTDIR=${WORKDIR}/ \
    INPLACE_COMPILE=y \
    MODULE_OUT=${WORKDIR}/display/vendor/qcom/opensource/mm-drivers \
    KERNEL_UAPI_HEADERS_DIR=${STAGING_KERNEL_BUILDDIR} \
    ./build/build_module.sh
}

do_install() {
    install -d ${D}/${nonarch_base_libdir}/modules/${KERNEL_VERSION}
    install -m 0755 ${B}/hw_fence/msm_hw_fence.ko -D ${D}/${nonarch_base_libdir}/modules/${KERNEL_VERSION}
    install -m 0755 ${B}/sync_fence/sync_fence.ko -D ${D}/${nonarch_base_libdir}/modules/${KERNEL_VERSION}
    install -m 0755 ${B}/msm_ext_display/msm_ext_display.ko -D ${D}/${nonarch_base_libdir}/modules/${KERNEL_VERSION}
    install -m 0755 ${WORKDIR}/display/vendor/qcom/opensource/mm-drivers/Module.symvers -D ${D}${base_libdir}/modules/${KERNEL_VERSION}/mm-drivers/Module.symvers
}

do_deploy() {
# Deploy unstripped kernel modules into ${DEPLOYDIR}/kernel_modules for debugging purposes
    install -d ${DEPLOYDIR}/kernel_modules
    for kmod in $(find ${D} -name "*.ko") ; do
        install -m 0644 $kmod ${DEPLOYDIR}/kernel_modules
    done
}

#addtask do_deploy after do_install

FILES:${PN} += "${sysconfdir}/*"
FILES:${PN} += "${nonarch_base_libdir}/modules/${KERNEL_VERSION}/*"
