inherit autotools pkgconfig

DESCRIPTION = "display Library"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=3775480a712fc46a69647678acb234cb"

PR = "r8"

PACKAGES = "${PN}"

FILESPATH   =+ "${WORKSPACE}:"
SRC_URI     =  "file://display/display-hal/"

S = "${WORKDIR}/display/display-hal/"

DEPENDS += "system-core"
DEPENDS += "libhardware"
DEPENDS += "native-frameworks"

EXTRA_OECONF = " --with-core-includes=${WORKSPACE}/system/core/include"
EXTRA_OECONF += " --with-sanitized-headers=${STAGING_KERNEL_BUILDDIR}/usr/include"

LDFLAGS += "-llog -lhardware -lutils -lcutils"

CPPFLAGS += "-DTARGET_HEADLESS"
CPPFLAGS += "-DVENUS_COLOR_FORMAT"
CPPFLAGS += "-DPAGE_SIZE=4096"
CPPFLAGS += "-I${S}/libqdutils"
CPPFLAGS += "-I${S}/libqservice"
CPPFLAGS += "-I${S}/sdm/include"
CPPFLAGS += "-I${S}/include"
CPPFLAGS += "-I${WORKSPACE}/system/core/include"

# Need to revisit
# libcamera and libadreno giving compilation errors
# so exporting libqservice headers and qdMetaData.h to ${D}${includedir}
do_install:append () {
    install -d ${D}${includedir}
    install -m 0644 ${S}/libqdutils/qdMetaData.h   -D ${D}${includedir}/libqdutils/qdMetaData.h
    install -m 0644 ${S}/libqdutils/qdMetaData.h   -D ${D}${includedir}
    install -m 0644 ${S}/libqservice/*.h   -D ${D}${includedir}
}

addtask fix_sysroot after do_install before do_populate_sysroot

FILES:${PN} = "${libdir}/*.so"
INSANE_SKIP:${PN} = "dev-so"
