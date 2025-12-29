DESCRIPTION = "QTI mm drivers"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=801f80980d171dd6425610833a22dbe6"

inherit linux-kernel-base deploy

PACKAGE_ARCH = "${MACHINE_ARCH}"

PR = "r0"

# Add for DDK
DDK_BUILD ?= "false"
DEPENDS += "${@bb.utils.contains('DDK_BUILD', 'false', \
           'virtual/kernel', '', d)}"
OVERRIDES:append = "${@':ddk_build:ddk_install' if d.getVar('DDK_BUILD') == 'true' else ''}"

SRCREV = "${AUTOREV}"

do_compile[depends] += "virtual/kernel:do_shared_workdir"
do_compile[cleandirs] += "${WORKDIR}/out/${KERNEL_DEFCONFIG}"

FILESEXTRAPATHS:prepend := "${WORKSPACE}:"
SRC_URI     =  "file://display/vendor/qcom/opensource/mm-drivers/"
KERNEL_VERSION = "${@get_kernelversion_file("${STAGING_KERNEL_BUILDDIR}")}"

S = "${WORKDIR}/display/vendor/qcom/opensource/mm-drivers"

EXTRA_OEMAKE += "TARGET_SUPPORT=${BASEMACHINE}"

do_configure[noexec] = "1"

# Map of module subdirectory to .ko name
MM_MODULES = "hw_fence:msm_hw_fence sync_fence:sync_fence msm_ext_display:msm_ext_display hfi_core:msm_hfi_core"

# Common build runner; expects EXT_MODULES to be set by  caller
run_build_module() {
    cd ${KERNEL_PLATFORM_PATH}
    TARGET_BOARD_PLATFORM=${TARGET_BOARD_PLATFORM} \
    ENABLE_DDK_BUILD=${DDK_BUILD} \
    BUILD_CONFIG=${KERNEL_BUILD_CONFIG} \
    VARIANT=${KERNEL_DEFCONFIG_VARIANT} \
    KERNEL_KIT=${KERNEL_PREBUILT_PATH} \
    OUT_DIR=${INTERMEDIATE_KERNEL_PATH} \
    EXT_MODULES=${EXT_MODULES} \
    ROOTDIR=${WORKDIR}/ \
    INPLACE_COMPILE=y \
    MODULE_OUT=${MODULE_OUT} \
    KERNEL_UAPI_HEADERS_DIR=${STAGING_KERNEL_BUILDDIR} \
    ./build/build_module.sh
}

do_compile_hw_fence() {
    EXT_MODULES="${@os.path.relpath("${S}/hw_fence","${KERNEL_PLATFORM_PATH}")}"
    MODULE_OUT="${S}/hw_fence"
    run_build_module
}

do_compile_sync_fence() {
    EXT_MODULES="${@os.path.relpath("${S}/sync_fence","${KERNEL_PLATFORM_PATH}")}"
    MODULE_OUT="${S}/sync_fence"
    run_build_module
}

do_compile_msm_ext_display() {
    EXT_MODULES="${@os.path.relpath("${S}/msm_ext_display","${KERNEL_PLATFORM_PATH}")}"
    MODULE_OUT="${S}/msm_ext_display"
    run_build_module
}

do_compile_hfi_core() {
    EXT_MODULES="${@os.path.relpath("${S}/hfi_core","${KERNEL_PLATFORM_PATH}")}"
    MODULE_OUT="${S}/hfi_core"
    run_build_module
}

do_compile:ddk_build() {
    if [ ! -L "${KERNEL_PLATFORM_PATH}/vendor" ]; then
        ln -sf ${WORKSPACE}/vendor ${KERNEL_PLATFORM_PATH}/vendor
    fi
    if [ ! -L "${KERNEL_PLATFORM_PATH}/vendor/qcom/opensource/mm-drivers" ]; then
        ln -sf ${WORKSPACE}/display/vendor/qcom/opensource/mm-drivers  ${WORKSPACE}/vendor/qcom/opensource/mm-drivers
    fi
    do_compile_hw_fence
    do_compile_sync_fence
    do_compile_msm_ext_display
    do_compile_hfi_core
}

do_compile() {
    cd ${KERNEL_PLATFORM_PATH}
    BUILD_CONFIG=msm-kernel/${KERNEL_CONFIG} \
    KERNEL_KIT=${KERNEL_PREBUILT_PATH} \
    OUT_DIR=${WORKDIR}/out/${KERNEL_DEFCONFIG} \
    EXT_MODULES="${@os.path.relpath("${S}","${KERNEL_PLATFORM_PATH}")}" \
    ROOTDIR=${WORKDIR}/ \
    INPLACE_COMPILE=y \
    MODULE_OUT=${WORKDIR}/display/vendor/qcom/opensource/mm-drivers \
    KERNEL_UAPI_HEADERS_DIR=${STAGING_KERNEL_BUILDDIR} \
    ./build/build_module.sh
}

do_install:ddk_install() {
    install -d ${D}/${nonarch_base_libdir}/modules/${KERNEL_VERSION}
    for pair in ${MM_MODULES}; do
        subdir=${pair%%:*}
        modname=${pair##*:}
        if [ -f "${S}/${subdir}/${modname}.ko" ]; then
            install -m 0755 "${S}/${subdir}/${modname}.ko" -D "${D}/${nonarch_base_libdir}/modules/${KERNEL_VERSION}/${modname}.ko"
            install -m 0755 "${S}/${subdir}/Module.symvers" -D ${D}${base_libdir}/modules/${KERNEL_VERSION}/${subdir}/Module.symvers
        fi
    done
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
