SRC_URI   = "git://cgit.freedesktop.org/mesa/drm;protocol=git;nobranch=1;rev=b2103fa3257daa6acfdc6f4d4d8565abebaec4a8"

CFLAGS += "-I${STAGING_KERNEL_BUILDDIR}/usr/include"
S = "${WORKDIR}/git"

FILES_${PN} += "${bindir}/*"

do_install_append() {
cp -rf ${S}/libdrm_macros.h ${D}${includedir}/libdrm/
}
do_configure[depends] += "virtual/kernel:do_shared_workdir"
