DESCRIPTION = "QTI Display drivers"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

inherit linux-kernel-base deploy

PR = "r0"

DEPENDS = "rsync-native"
DEPENDS += "bc-native bison-native"

do_configure[depends] += "virtual/kernel:do_shared_workdir"

FILESPATH   =+ "${WORKSPACE}:"
SRC_URI     =  "file://display/vendor/qcom/opensource/display-drivers/"
SRC_URI    +=  "file://start_display_le"
SRC_URI    +=  "file://display@.service"
SRC_URI    +=  "file://display.service"
SRC_URI    +=  "file://display_load.conf"

S = "${WORKDIR}/display/vendor/qcom/opensource/display-drivers"

EXTRA_OEMAKE += "TARGET_SUPPORT=${BASEMACHINE}"

# Disable parallel make
PARALLEL_MAKE = ""

# Disable parallel make
PARALLEL_MAKE = "-j1"

do_compile[lockfiles] = "${TMPDIR}/build_modules.lock"

do_configure() {
	cp -f ${WORKSPACE}/display/vendor/qcom/opensource/display-drivers/Makefile.am ${WORKSPACE}/display/vendor/qcom/opensource/display-drivers/Makefile
}

do_compile() {

    cd ${WORKSPACE}/kernel-${PREFERRED_VERSION_linux-msm}/kernel_platform  && \

    BUILD_CONFIG=${KERNEL_BUILD_CONFIG} \
    EXT_MODULES=../../display/vendor/qcom/opensource/display-drivers \
    ROOTDIR=${WORKSPACE}/ \
    MODULE_DRM_MSM=m \
    MODULE_OUT=${WORKDIR}/display/vendor/qcom/opensource/display-drivers \
    KERNEL_KIT=${KERNEL_OUT_PATH}/ \
    OUT_DIR=temp_out_dir \
    KERNEL_UAPI_HEADERS_DIR=${STAGING_KERNEL_BUILDDIR} \
    INSTALL_MODULE_HEADERS=1 \
    ./build/build_module.sh
}

do_install() {
	install -d ${D}${sysconfdir}/initscripts
	install -d ${D}/usr/include/
	install -m 755 ${WORKDIR}/start_display_le ${D}${sysconfdir}/initscripts
	install -d ${D}/usr/lib/modules/
	install -m 0755 ${WORKDIR}/display/vendor/qcom/opensource/display-drivers/msm/msm_drm.ko -D ${WORKDIR}/msm_drm.ko

        # strip debug symbols and sign the module
        ${STAGING_DIR_NATIVE}/usr/libexec/aarch64-oe-linux/gcc/aarch64-oe-linux/11.4.0/strip \
              --strip-debug ${WORKDIR}/display/vendor/qcom/opensource/display-drivers/msm/msm_drm.ko

        LD_LIBRARY_PATH=${WORKSPACE}/kernel-${PREFERRED_VERSION_linux-msm}/kernel_platform/prebuilts/kernel-build-tools/linux-x86/lib64/ \
        ${KERNEL_PREBUILT_PATH}/dist/sign-file sha1 ${KERNEL_PREBUILT_PATH}/dist/signing_key.pem \
        ${KERNEL_PREBUILT_PATH}/dist/signing_key.x509 ${WORKDIR}/display/vendor/qcom/opensource/display-drivers/msm/msm_drm.ko

	install -m 0755 ${WORKDIR}/display/vendor/qcom/opensource/display-drivers/msm/msm_drm.ko -D ${D}${libdir}/modules/msm_drm.ko
	cp -r ${WORKDIR}/display/vendor/qcom/opensource/display-drivers/usr/include/display ${D}/usr/include/
	install -m 0644 ${WORKDIR}/display@.service -D ${D}${systemd_unitdir}/system/display@.service
	install -m 0644 ${WORKDIR}/display.service -D ${D}${systemd_unitdir}/system/display.service
	install -m 0755 ${WORKDIR}/display_load.conf -D ${D}${sysconfdir}/modules-load.d/display_load.conf
}

do_deploy() {
	cp -rp ${WORKDIR}/msm_drm.ko ${DEPLOYDIR}/
}

addtask do_deploy after do_install

FILES:${PN} += "${sysconfdir}/*"
FILES:${PN} += "/etc/initscripts/start_display_le"
FILES:${PN} += "${systemd_unitdir}/system/display@.service"
FILES:${PN} += "${systemd_unitdir}/system/display.service"
FILES:${PN} += "${libdir}/modules/*"
