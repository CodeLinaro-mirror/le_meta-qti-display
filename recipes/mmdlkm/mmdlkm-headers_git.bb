DESCRIPTION = "QTI mm drivers"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=801f80980d171dd6425610833a22dbe6"

PACKAGE_ARCH = "${MACHINE_ARCH}"

PR = "r0"

FILESEXTRAPATHS:prepend := "${WORKSPACE}:"
do_compile[noexec] = "1"

SRC_URI     =  "file://display/vendor/qcom/opensource/mm-drivers/"
S = "${WORKDIR}/display/vendor/qcom/opensource/mm-drivers"

do_install() {
    install -d ${D}${includedir}
    install -m 0755 ${B}/hw_fence/include/*.h -D ${D}${includedir}
    install -m 0755 ${B}/sync_fence/include/uapi/sync_fence/*.h -D ${D}${includedir}
}