DESCRIPTION = "QTI Display drivers"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

inherit linux-kernel-base deploy

PR = "r0"

DEPENDS += "rsync-native"

do_configure[depends] += "virtual/kernel:do_shared_workdir"

# TODO: Remove this local definition once available via machine.conf
KERNEL_DEFCONFIG ?= "neo_le-defconfig"
KERNEL_DEFCONFIG_qti-distro-debug ?= "neo_le-debug_defconfig"

FILESPATH   =+ "${WORKSPACE}:"
SRC_URI     =  "file://vendor/qcom/opensource/display-drivers/"
SRC_URI    +=  "file://kernel-5.10/kernel_platform"
SRC_URI    +=  "file://kernel-5.10/out/${KERNEL_DEFCONFIG}"
SRC_URI    +=  "file://start_display_le"
SRC_URI    +=  "file://display.service"
SRC_URI    +=  "file://display_load.conf"

S = "${WORKDIR}/vendor/qcom/opensource/display-drivers"

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
    EXT_MODULES=../../vendor/qcom/opensource/display-drivers \
    ROOTDIR=${WORKDIR}/ \
    MODULE_DRM_MSM=m \
    MODULE_OUT=${S} \
    OUT_DIR=${WORKDIR}/kernel-5.10/out/${KERNEL_DEFCONFIG} \
    KERNEL_UAPI_HEADERS_DIR=${STAGING_KERNEL_BUILDDIR} \
    INSTALL_MODULE_HEADERS=1 \
    ./build/build_module.sh
}

do_install() {
	install -d ${D}${sysconfdir}/initscripts
	install -d ${D}${systemd_unitdir}/system/multi-user.target.wants/
	install -m 755 ${WORKDIR}/start_display_le ${D}${sysconfdir}/initscripts
	install -d ${D}/usr/lib/modules/
	install -m 0755 ${S}/msm/msm_drm.ko -D ${D}${libdir}/modules/msm_drm.ko
	install -d ${D}/usr/include/display/drm
	install -d ${D}/usr/include/display/hdcp
	install -d ${D}/usr/include/display/media
	install -m 0755 ${WORKDIR}/vendor/qcom/opensource/display-drivers/usr/include/display/drm/*.h -D ${D}${includedir}/display/drm/
	install -m 0755 ${WORKDIR}/vendor/qcom/opensource/display-drivers/usr/include/display/hdcp/*.h -D ${D}${includedir}/display/hdcp/
	install -m 0755 ${WORKDIR}/vendor/qcom/opensource/display-drivers/usr/include/display/media/*.h -D ${D}${includedir}/display/media/
	install -m 0644 ${WORKDIR}/display.service -D ${D}${systemd_unitdir}/system/display.service
	install -m 0755 ${WORKDIR}/display_load.conf -D ${D}${sysconfdir}/modules-load.d/display_load.conf
	ln -sf ${systemd_unitdir}/system/display.service ${D}${systemd_unitdir}/system/multi-user.target.wants/display.service
}

do_deploy() {
# Deploy unstripped kernel modules into ${DEPLOYDIR}/kernel_modules for debugging purposes
    install -d ${DEPLOYDIR}/kernel_modules
    for kmod in $(find ${D} -name "*.ko") ; do
        install -m 0644 $kmod ${DEPLOYDIR}/kernel_modules
    done
}

addtask deploy after do_install before do_package

FILES_${PN} += "${sysconfdir}/*"
FILES_${PN} += "/etc/initscripts/start_display_le"
FILES_${PN} += "${systemd_unitdir}/system/display.service"
FILES_${PN} += "${systemd_unitdir}/system/multi-user.target.wants/display.service"
FILES_${PN} += "${libdir}/modules/*"
