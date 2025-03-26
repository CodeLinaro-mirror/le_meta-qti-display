inherit module deploy

DESCRIPTION = "QTI Display drivers"
LICENSE = "GPL-2.0-only & MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/GPL-2.0-only;md5=801f80980d171dd6425610833a22dbe6 \
		    file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

do_configure[depends] += "virtual/kernel:do_shared_workdir"

FILESEXTRAPATHS:prepend := "${WORKSPACE}:"
SRC_URI     =  "file://display/vendor/qcom/opensource/display-drivers/"
SRC_URI    +=  "file://display_load.conf"

RPROVIDES:${PN} += "kernel-module-msm-drm-${KERNEL_VERSION}"

S = "${WORKDIR}/display/vendor/qcom/opensource/display-drivers"
EXTRA_OEMAKE += "TARGET_SUPPORT=${BASEMACHINE}"
EXTRA_OEMAKE += "M=${S}"
KERNEL_MODULES = "msm_drm.ko"

# Disable parallel make
PARALLEL_MAKE = ""

# Disable parallel make
PARALLEL_MAKE = "-j1"

KERNEL_CC = "${STAGING_BINDIR_NATIVE}/clang/bin/clang -target ${TARGET_ARCH}${TARGET_VENDOR}-${TARGET_OS}"

do_install() {
	install -m 0755 ${WORKDIR}/display/vendor/qcom/opensource/display-drivers/msm/msm_drm.ko -D ${D}${base_libdir}/modules/msm_drm.ko
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
