DESCRIPTION = "Userspace interface to the kernel DRM services"
HOMEPAGE = "http://dri.freedesktop.org"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://xf86drm.c;beginline=9;endline=32;md5=c8a3b961af7667c530816761e949dc71"
PROVIDES = "drm"

inherit autotools pkgconfig manpages

FILESPATH =+ "${WORKSPACE}:"
SRC_URI   = "file://display/libdrm"
SRC_URI_append_sdmsteppe = " file://0001-libdrm-Include-sysmacros-from-sys-domain.patch"
SRC_URI_append_sdm845 = " file://0001-libdrm-Include-sysmacros-from-sys-domain.patch"
SRCREV = "${AUTOREV}"
S      = "${WORKDIR}/display/libdrm"

do_patch[depends] += "${PN}:do_unpack"

DEPENDS = "libpthread-stubs libpciaccess"

EXTRA_OECONF += "--disable-cairo-tests \
                 --enable-omap-experimental-api \
                 --enable-install-test-programs \
                 --disable-valgrind \
                "

CFLAGS_sdmsteppe += "-Wno-format-truncation"
CFLAGS_sdmsteppe += "-Wno-error=implicit-function-declaration"
CFLAGS_sdm845 += "-Wno-format-truncation"
CFLAGS_sdm845 += "-Wno-error=implicit-function-declaration"

PACKAGECONFIG[manpages] = "--enable-manpages, --disable-manpages, libxslt-native xmlto-native"


do_install_append() {
cp -rf ${S}/libdrm_macros.h ${D}${includedir}/libdrm/
}


ALLOW_EMPTY_${PN}-drivers = "1"
PACKAGES =+ "${PN}-tests \
             ${PN}-drivers \
             ${PN}-radeon \
             ${PN}-nouveau \
             ${PN}-omap \
             ${PN}-intel \
             ${PN}-exynos \
             ${PN}-kms \
             ${PN}-freedreno \
             ${PN}-amdgpu \
             ${PN}-etnaviv"

RRECOMMENDS_${PN}-drivers = "${PN}-radeon \
                             ${PN}-nouveau \
                             ${PN}-omap \
                             ${PN}-intel \
                             ${PN}-exynos \
                             ${PN}-freedreno \
                             ${PN}-amdgpu \
                             ${PN}-etnaviv"

FILES_${PN}-tests = "${bindir}/*"
FILES_${PN}-radeon = "${libdir}/libdrm_radeon.so.*"
FILES_${PN}-nouveau = "${libdir}/libdrm_nouveau.so.*"
FILES_${PN}-omap = "${libdir}/libdrm_omap.so.*"
FILES_${PN}-intel = "${libdir}/libdrm_intel.so.*"
FILES_${PN}-exynos = "${libdir}/libdrm_exynos.so.*"
FILES_${PN}-kms = "${libdir}/libkms*.so.*"
FILES_${PN}-freedreno = "${libdir}/libdrm_freedreno.so.*"
FILES_${PN}-amdgpu = "${libdir}/libdrm_amdgpu.so.*"
FILES_${PN}-etnaviv = "${libdir}/libdrm_etnaviv.so.*"

