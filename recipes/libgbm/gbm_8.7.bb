inherit autotools-brokensep pkgconfig qprebuilt

PACKAGE_ARCH = "${MACHINE_ARCH}"
HOMEPAGE         = "https://source.codeaurora.org/"
LICENSE          = "BSD-3-Clause & MIT & Apache-2.0"
BSD-3-Clause_LICENSE  = "file://${COREBASE}/meta/files/common-licenses/BSD-3-Clause"
MIT_LICENSE = "file://${COREBASE}/meta/files/common-licenses/MIT"
Apache-2.0_LICENSE = "file://${COREBASE}/meta/files/common-licenses/Apache-2.0"
LIC_FILES_CHKSUM = " \
${BSD-3-Clause_LICENSE};md5=550794465ba0ec5312d6919e203a55f9 \
${MIT_LICENSE};md5=0835ade698e0bcf8506ecda2f7b4f302 \
${Apache-2.0_LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"

DESCRIPTION = "libgbm Library"
PR = "r2"

SRC_URI     =  "file://display/libgbm"
FILESEXTRAPATHS:prepend := "${WORKSPACE}:"

PROVIDES        += "virtual/libgbm"
RPROVIDES:${PN} += "virtual/libgbm"

S = "${WORKDIR}/display/libgbm/"

DEPENDS += "linux-msm-headers \
            glib-2.0 \
            displaydlkm-headers \
            display-commonsys \
            libdmabufheap \
            libcutils \
            libvmmem "

PACKAGECONFIG ??= "glib \
                  "

PACKAGECONFIG[glib] = "--with-glib, --without-glib, glib-2.0"

EXTRA_OECONF += " --with-sanitized-headers=${STAGING_INCDIR}/linux-msm/usr/include"

TOOLCHAIN = "sdllvm"
INSANE_SKIP:gbm += "dev-deps"
PACKAGES = "${PN}-dbg ${PN}"
FILES:${PN}-dbg  = "${libdir}/.debug/* ${bindir}/.debug/* /usr/lib/.debug/*"
FILES:${PN}      = "${libdir}/* /usr/lib/* ${bindir}/* ${includedir}/*"
