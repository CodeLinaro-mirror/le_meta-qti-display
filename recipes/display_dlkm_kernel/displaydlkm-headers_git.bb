DESCRIPTION = "QTI Display drivers"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=801f80980d171dd6425610833a22dbe6"

PACKAGE_ARCH = "${MACHINE_ARCH}"

PR = "r0"

FILESEXTRAPATHS:prepend := "${WORKSPACE}:"
do_compile[noexec] = "1"

SRC_URI    =  "file://display/vendor/qcom/opensource/display-drivers/"
S = "${WORKDIR}/display/vendor/qcom/opensource/display-drivers"

do_install() {
	install -d ${D}/usr/include/
	install -d ${D}/usr/include/display/drm
	install -d ${D}/usr/include/display/hdcp
	install -d ${D}/usr/include/display/media
	install -m 0755 ${B}/include/uapi/display/drm/*.h -D ${D}${includedir}/display/drm/
	install -m 0755 ${B}/include/uapi/display/hdcp/*.h -D ${D}${includedir}/display/hdcp/
	install -m 0755 ${B}/include/uapi/display/media/*.h -D ${D}${includedir}/display/media/
}