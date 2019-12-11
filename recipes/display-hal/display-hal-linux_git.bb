inherit autotools pkgconfig

DESCRIPTION = "display Library"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=3775480a712fc46a69647678acb234cb"

PR = "r8"

FILESPATH   =+ "${WORKSPACE}:"
SRC_URI     =  "file://display/display-hal"

S = "${WORKDIR}/display/display-hal/"

EXTRA_OECONF = " --with-core-includes=${WORKSPACE}/system/core/include"
EXTRA_OECONF += " --with-sanitized-headers=${STAGING_KERNEL_BUILDDIR}/usr/include"

DEPENDS += " libhardware virtual/kernel"

do_configure[depends] += "virtual/kernel:do_shared_workdir"

PACKAGECONFIG ?= "gbm \
                 adreno \
                 ${@bb.utils.contains('COMBINED_FEATURES', 'fbdev', 'fbdev', '', d)} \
                 ${@bb.utils.contains('COMBINED_FEATURES', 'drm', 'drm', '', d)} \
                 headless-target \
                 "

PACKAGECONFIG[gbm] = "--with-gbm, --without-gbm, gbm, gbm"
PACKAGECONFIG[fbdev] = "--enable-sdmhalfb, --disable-sdmhalfb"
PACKAGECONFIG[drm] = "--enable-sdmhaldrm, --disable-sdmhaldrm, libdrm, libdrm"
PACKAGECONFIG[adreno] = "--enable-adreno, --disable-adreno, adreno, adreno"
PACKAGECONFIG[headless-target] = "--enable-headless-target, --disable-headless-target"

CPPFLAGS += "-I${S}/gpu_tonemapper"
CPPFLAGS += "-I${S}/sdm/include"
CPPFLAGS += "-I${S}/include"

do_install_append () {
    cp -fR ${WORKSPACE}/display/display-hal/include/* ${STAGING_INCDIR}/
    cp -fR ${WORKSPACE}/display/display-hal/gpu_tonemapper/*.h ${STAGING_INCDIR}
}

SOLIBS = ".so"
FILES_SOLIBSDEV = ""
