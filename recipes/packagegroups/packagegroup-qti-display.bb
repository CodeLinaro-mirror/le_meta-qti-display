SUMMARY = "QTI Display package groups"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

LICENSE = "BSD-3-Clause"

PROVIDES = "${PACKAGES}"

PACKAGES = ' \
    packagegroup-qti-display \
    '

RDEPENDS:packagegroup-qti-display = ' \
    libdrm \
    display-hal-linux \
    mmdlkm \
    displaydlkm \
    gbm \
    weston \
    '

RDEPENDS:packagegroup-qti-display:remove:sun += 'display-hal-linux'
RDEPENDS:packagegroup-qti-display:append:sun += 'mmrm-kernel'
