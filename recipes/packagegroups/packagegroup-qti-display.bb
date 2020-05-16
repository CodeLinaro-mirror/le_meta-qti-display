SUMMARY = "QTI Display package groups"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

LICENSE = "BSD-3-Clause"

PROVIDES = "${PACKAGES}"

PACKAGES = ' \
    packagegroup-qti-display \
    '

RDEPENDS_packagegroup-qti-display = ' \
    libdrm \
    display-hal-linux \
    sdm-comp-linux \
    '

