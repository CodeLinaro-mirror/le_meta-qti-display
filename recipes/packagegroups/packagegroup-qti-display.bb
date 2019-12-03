SUMMARY = "QTI Display package groups"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

LICENSE = "BSD-3-CLAUSE"

PROVIDES = "${PACKAGES}"

PACKAGES = ' \
    packagegroup-qti-display \
    ${@bb.utils.contains("DISTRO_FEATURES", "wayland", bb.utils.contains("COMBINED_FEATURES", "fbdev", "packagegroup-qti-display-wayland", "", d), "", d)} \
    '

RDEPENDS_packagegroup-qti-display = ' \
    ${@bb.utils.contains("DISTRO_FEATURES", "wayland", bb.utils.contains("COMBINED_FEATURES", "fbdev", "packagegroup-qti-display-wayland", "", d), "", d)} \
    '

RDEPENDS_packagegroup-qti-display-wayland = ' \
    wayland \
    weston \
    weston-init \
    display-hal-linux \
    '

