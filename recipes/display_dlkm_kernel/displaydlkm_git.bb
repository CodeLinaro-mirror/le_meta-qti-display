DESCRIPTION = "QTI Display drivers"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=3775480a712fc46a69647678acb234cb"

inherit linux-kernel-base deploy

PR = "r0"

DEPENDS = "rsync-native"
DEPENDS += "bc-native bison-native"

do_configure[depends] += "virtual/kernel:do_shared_workdir"

FILESPATH   =+ "${WORKSPACE}:"
SRC_URI     =  "file://vendor/qcom/opensource/display-drivers/"
SRC_URI    +=  "file://start_display_le"
SRC_URI    +=  "file://display.service"
SRC_URI    +=  "file://display_load.conf"

S = "${WORKDIR}/vendor/qcom/opensource/display-drivers"

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
    BUILD_CONFIG=msm-kernel/build.config.msm.*.tuivm \
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
	install -d ${D}${sysconfdir}/initscripts
	install -d ${D}${systemd_unitdir}/system/multi-user.target.wants/
	install -d ${D}/usr/include/
	install -m 755 ${WORKDIR}/start_display_le ${D}${sysconfdir}/initscripts
	install -d ${D}/usr/lib/modules/
	install -m 0755 ${WORKDIR}/vendor/qcom/opensource/display-drivers/msm/msm_drm.ko -D ${WORKDIR}/msm_drm.ko

        # strip debug symbols and sign the module
        ${STAGING_DIR_NATIVE}/usr/libexec/aarch64-oe-linux/gcc/aarch64-oe-linux/9.3.0/strip \
              --strip-debug ${WORKDIR}/vendor/qcom/opensource/display-drivers/msm/msm_drm.ko

        ${STAGING_KERNEL_BUILDDIR}/scripts/sign-file sha512 ${STAGING_KERNEL_BUILDDIR}/certs/signing_key.pem \
             ${STAGING_KERNEL_BUILDDIR}/certs/signing_key.x509 ${WORKDIR}/vendor/qcom/opensource/display-drivers/msm/msm_drm.ko

	install -m 0755 ${WORKDIR}/vendor/qcom/opensource/display-drivers/msm/msm_drm.ko -D ${D}${libdir}/modules/msm_drm.ko
	cp -r ${WORKDIR}/vendor/qcom/opensource/display-drivers/usr/include/display ${STAGING_KERNEL_BUILDDIR}/usr/include/display
	cp -r ${WORKDIR}/vendor/qcom/opensource/display-drivers/usr/include/display ${D}/usr/include/
	install -m 0644 ${WORKDIR}/display.service -D ${D}${systemd_unitdir}/system/display.service
	install -m 0755 ${WORKDIR}/display_load.conf -D ${D}${sysconfdir}/modules-load.d/display_load.conf
	ln -sf ${systemd_unitdir}/system/display.service ${D}${systemd_unitdir}/system/multi-user.target.wants/display.service
}

do_deploy() {
	cp -rp ${WORKDIR}/msm_drm.ko ${DEPLOYDIR}/
}

addtask do_deploy after do_install

FILES_${PN} += "${sysconfdir}/*"
FILES_${PN} += "/etc/initscripts/start_display_le"
FILES_${PN} += "${systemd_unitdir}/system/display.service"
FILES_${PN} += "${systemd_unitdir}/system/multi-user.target.wants/display.service"
FILES_${PN} += "${libdir}/modules/*"
