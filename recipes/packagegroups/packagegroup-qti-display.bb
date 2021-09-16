SUMMARY = "QTI Display package groups"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

LICENSE = "BSD-3-Clause"

PROVIDES = "${PACKAGES}"

PACKAGES = ' \
    packagegroup-qti-display \
    ${@bb.utils.contains("DISTRO_FEATURES", "wayland", bb.utils.contains_any("COMBINED_FEATURES", "fbdev drm", "packagegroup-qti-display-wayland", "", d), "", d)} \
    ${@bb.utils.contains("COMBINED_FEATURES", "fbdev", "packagegroup-qti-display-fbdev", "", d)} \
    ${@bb.utils.contains("COMBINED_FEATURES", "drm", "packagegroup-qti-display-drm", "", d)} \
    '

RDEPENDS_packagegroup-qti-display = ' \
    ${@bb.utils.contains("DISTRO_FEATURES", "wayland", bb.utils.contains_any("COMBINED_FEATURES", "fbdev drm", "packagegroup-qti-display-wayland", "", d), "", d)} \
    '

RDEPENDS_packagegroup-qti-display-wayland = ' \
    wayland \
    weston \
    display-commonsys \
    ${@bb.utils.contains("DISTRO_FEATURES", "sdm", "display-hal-linux", "", d)} \
    ${@bb.utils.contains("COMBINED_FEATURES", "fbdev", "packagegroup-qti-display-fbdev", "", d)} \
    ${@bb.utils.contains("COMBINED_FEATURES", "drm", "packagegroup-qti-display-drm", "", d)} \
    '

RDEPENDS_packagegroup-qti-display-fbdev = ' \
    weston-init \
    '

RDEPENDS_packagegroup-qti-display-drm = ' \
    libdrm \
    '
