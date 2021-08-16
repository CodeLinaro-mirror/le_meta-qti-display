inherit autotools pkgconfig

DESCRIPTION = "display Library"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=3775480a712fc46a69647678acb234cb"

PR = "r8"

PACKAGES = "${PN}"

FILESPATH   =+ "${WORKSPACE}:"
SRC_URI     =  "file://hardware/qcom/display"

S = "${WORKDIR}/hardware/qcom/display/"

EXTRA_OECONF = " --with-core-includes=${WORKSPACE}/system/core/include"
EXTRA_OECONF += " --with-sanitized-headers=${STAGING_KERNEL_BUILDDIR}/usr/include"

PACKAGECONFIG ?= "gbm \
		 drm \
                 ${@bb.utils.contains('COMBINED_FEATURES', 'fbdev', 'fbdev', '', d)} \
                 ${@bb.utils.contains('COMBINED_FEATURES', 'drm', 'drm', '', d)} \
                 "

DEPENDS += "libhardware linux-msm-headers displaydlkm"

PACKAGECONFIG[gbm] = "--with-gbm, --without-gbm, gbm, gbm"
PACKAGECONFIG[fbdev] = "--enable-sdmhalfb, --disable-sdmhalfb"
PACKAGECONFIG[drm] = "--enable-sdmhaldrm, --disable-sdmhaldrm, libdrm, libdrm"

LDFLAGS += "-llog -lhardware -lutils -lcutils"

CPPFLAGS += "-DCOMPILE_DRM"

CFLAGS += "-I${STAGING_KERNEL_BUILDDIR}/usr/include"
CPPFLAGS += "-I${STAGING_KERNEL_BUILDDIR}/usr/include"
CPPFLAGS += "-DVENUS_COLOR_FORMAT"
CPPFLAGS += "-DPAGE_SIZE=4096"
CPPFLAGS += "-D__GBM__"
CPPFLAGS += "-I${S}/libdebug"
CPPFLAGS += "-I${S}/libdrmutils"

CPPFLAGS += "-I${S}gpu_tonemapper"
CPPFLAGS += "-I${S}sdm/include"
CPPFLAGS += "-I${S}include"

CPPFLAGS += "-I${WORKSPACE}/system/core/include"
CPPFLAGS += "-I${STAGING_INCDIR}/libdrm"
CPPFLAGS += "-I${STAGING_INCDIR}/gbm"

do_install_append () {
    # libhardware expects to find /usr/lib/hw/gralloc.*.so
    install -d ${D}${libdir}/hw
    ln -s ${libdir}/libgralloc.so ${D}${libdir}/hw/gralloc.default.so
    cp -fR ${WORKSPACE}/hardware/qcom/display/include/*.h ${STAGING_INCDIR}
}

FILES_${PN} += "${libdir}/* ${includedir}"
FILES_${PN} += "${libdir}/hw/gralloc.default.so"
INSANE_SKIP_${PN} = "dev-so"
