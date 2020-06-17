FILESPATH =+ "${WORKSPACE}:"
FILES_${PN} += "${bindir}/*"
SRC_URI   = "file://vendor/qcom/opensource/display/libdrm"
SRCREV = "${AUTOREV}"
S      = "${WORKDIR}/vendor/qcom/opensource/display/libdrm"

CFLAGS += "-I${STAGING_KERNEL_BUILDDIR}/usr/include"

do_install_append() {
cp -rf ${S}/libdrm_macros.h ${D}${includedir}/libdrm/
}
