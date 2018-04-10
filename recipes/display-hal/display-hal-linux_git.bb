inherit autotools qcommon

DESCRIPTION = "display Library"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=3775480a712fc46a69647678acb234cb"

PR = "r8"

PACKAGES = "${PN}"

SRC_DIR = "${WORKSPACE}/display/display-hal/"
S = "${WORKDIR}/display/display-hal/"

def get_depends(d):
    if d.getVar('DISTRO', True) == 'robot-som':
        return ""
    elif d.getVar('DISTRO', True) == 'robot-som-ros':
        return ""
    else:
        return "gbm"

DEPENDS += "system-core"
DEPENDS += "libhardware"
DEPENDS += "drm"
DEPENDS += "libdrm"
DEPENDS += " ${@get_depends(d)}"
DEPENDS += "adreno"
DEPENDS += " ${@base_contains('DISTRO', 'robot-som', 'binder', '', d)}"
DEPENDS += " ${@base_contains('DISTRO', 'robot-som', 'libui', '', d)}"
DEPENDS += " ${@base_contains('DISTRO', 'robot-som-ros', 'binder', '', d)}"
DEPENDS += " ${@base_contains('DISTRO', 'robot-som-ros', 'libui', '', d)}"

EXTRA_OECONF = " --with-core-includes=${WORKSPACE}/system/core/include"
EXTRA_OECONF += " --with-sanitized-headers=${STAGING_KERNEL_BUILDDIR}/usr/include"

EXTRA_OECONF_append_apq8098 = " --enable-sdmhaldrm"

LDFLAGS += "-llog -lhardware -lutils -lcutils"

CPPFLAGS_append_apq8098 += "-DCOMPILE_DRM"
CPPFLAGS += "-DTARGET_HEADLESS"
CPPFLAGS += "-DVENUS_COLOR_FORMAT"
CPPFLAGS += "-DPAGE_SIZE=4096"
CPPFLAGS_append_apq8098 += "-I${WORKSPACE}/display/display-hal/libdrmutils"
CPPFLAGS += "-I${WORKSPACE}/display/display-hal/gpu_tonemapper"
CPPFLAGS += "-I${WORKSPACE}/display/display-hal/sdm/include"
CPPFLAGS += "-I${WORKSPACE}/display/display-hal/include"
CPPFLAGS += "-I${WORKSPACE}/system/core/include"
CPPFLAGS += " ${@base_contains('DISTRO', 'robot-som', '-I${WORKSPACE}/display/display-hal/libqservice', '', d)}"
CPPFLAGS += " ${@base_contains('DISTRO', 'robot-som', '-I${WORKSPACE}/display/display-hal/libgralloc', '', d)}"
CPPFLAGS += " ${@base_contains('DISTRO', 'robot-som', '-I${WORKSPACE}/display/display-hal/libqdutils', '', d)}"
CPPFLAGS += " ${@base_contains('DISTRO', 'robot-som-ros', '-I${WORKSPACE}/display/display-hal/libqservice', '', d)}"
CPPFLAGS += " ${@base_contains('DISTRO', 'robot-som-ros', '-I${WORKSPACE}/display/display-hal/libgralloc', '', d)}"
CPPFLAGS += " ${@base_contains('DISTRO', 'robot-som-ros', '-I${WORKSPACE}/display/display-hal/libqdutils', '', d)}"
CPPFLAGS_append_apq8098 += "-I${STAGING_INCDIR}/libdrm"
CPPFLAGS_append_apq8098 += "-I${STAGING_INCDIR}/gbm"
CPPFLAGS_append_apq8098 += "-I${STAGING_INCDIR}/adreno"

do_install_append () {
    # libhardware expects to find /usr/lib/hw/gralloc.*.so
    install -d ${D}${libdir}/hw
    ln -s ${libdir}/libgralloc.so ${D}${libdir}/hw/gralloc.default.so
    cp -fR ${WORKSPACE}/display/display-hal/include/* ${STAGING_INCDIR}/
    cp -fR ${WORKSPACE}/display/display-hal/gpu_tonemapper/*.h ${STAGING_INCDIR}
}

FILES_${PN} = "${libdir}/*.so"
FILES_${PN} += "${libdir}/hw/gralloc.default.so"
INSANE_SKIP_${PN} = "dev-so"
