SRC_URI = "git://git.codelinaro.org/clo/le/mesa/drm.git;protocol=git;nobranch=1;rev=56f81e6776c1c100c3f627b2c1feb9dcae2aad3c"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"
FILESPATH =+ "${WORKSPACE}:"

SRC_URI += "${@bb.utils.contains('ARMPKGARCH', 'armv7a', ' \
								file://0001-DRM-Fix-the-negative-array-size-for-32-bit.patch \
								file://0001-libdrm-add-support-for-the-32-bit.patch \
								', '', d)}"
CFLAGS += "-I${STAGING_KERNEL_BUILDDIR}/usr/include"
S = "${WORKDIR}/git"

FILES:${PN} += "${bindir}/*"

do_install:append() {
    cp -rf ${S}/libdrm_macros.h ${D}${includedir}/libdrm/
}
do_configure[depends] += "virtual/kernel:do_shared_workdir"
