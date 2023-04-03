SRC_URI = "git://git.codelinaro.org/clo/le/mesa/drm;protocol=git;nobranch=1;rev=6b4e956d299c6ff09a4abf89642ccc1beb455c88"

CFLAGS += "-I${STAGING_KERNEL_BUILDDIR}/usr/include"
S = "${WORKDIR}/git"

FILES_${PN} += "${bindir}/*"

do_install_append() {
cp -rf ${S}/libdrm_macros.h ${D}${includedir}/libdrm/
}
do_configure[depends] += "virtual/kernel:do_shared_workdir"
