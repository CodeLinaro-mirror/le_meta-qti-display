FILESPATH =+ "${WORKSPACE}:"
SRC_URI   = "file://display/libdrm"
SRCREV = "${AUTOREV}"
S      = "${WORKDIR}/display/libdrm"

EXTRA_OECONF_append += "--disable-intel \
		  --disable-exynos \
		  --disable-radeon \
		  --disable-amdgpu \
		  --disable-freedreno \
		  --disable-freedreno-kgsl \
		  --disable-noveau \
		  --disable-vc4 \
		  --disable-vmwgfx \
		  --enable-install-test-programs \"

do_install_append() {
cp -rf ${S}/libdrm_macros.h ${D}${includedir}/libdrm/
}
