SUMMARY = "QTI Display package groups"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

LICENSE = "BSD-3-Clause"

PROVIDES = "${PACKAGES}"

PACKAGES = ' \
    packagegroup-qti-display \
    ${@bb.utils.contains("COMBINED_FEATURES", "drm", "packagegroup-qti-display-drm", "", d)} \
    '

RDEPENDS_packagegroup-qti-display = ' \
    gbm \
    display-commonsys \
    display-hal-linux \
    displaydlkm \
    ${@bb.utils.contains("COMBINED_FEATURES", "drm", "packagegroup-qti-display-drm", "", d)} \
    '

RDEPENDS_packagegroup-qti-display-drm = ' \
    libdrm \
    '
