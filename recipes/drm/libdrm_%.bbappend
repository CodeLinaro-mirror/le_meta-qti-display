SRC_URI = "${CLO_LE_GIT}/mesa/drm;protocol=https;nobranch=1;rev=56f81e6776c1c100c3f627b2c1feb9dcae2aad3c"

CFLAGS += "-I${STAGING_KERNEL_BUILDDIR}/usr/include"
S = "${WORKDIR}/git"

FILES:${PN} += "${bindir}/*"

do_install:append() {
    cp -rf ${S}/libdrm_macros.h ${D}${includedir}/libdrm/
}
do_configure[depends] += "virtual/kernel:do_shared_workdir"
