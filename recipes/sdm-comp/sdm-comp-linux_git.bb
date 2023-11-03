inherit autotools pkgconfig

DESCRIPTION = "display Library"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=3775480a712fc46a69647678acb234cb"

PR = "r8"

FILESPATH   =+ "${WORKSPACE}:"
SRC_URI     =  "file://display/vendor/qcom/opensource/display/sdm-composer"

S = "${WORKDIR}/display/vendor/qcom/opensource/display/sdm-composer"
CFG_S = "${WORKDIR}/display/vendor/qcom/opensource/display/sdm-composer/config"

DEPENDS += "display-hal-linux libsync libion display-noship libdmabufheap"
DEPENDS += "qmi-framework libsystemdq"

LDFLAGS += "-llog -lutils -lcutils -lion -lsync"

CPPFLAGS += "-I${WORKSPACE}/system/core/include"
CPPFLAGS += "-I${WORKSPACE}/system/core/libsync/include"
CPPFLAGS += "-I${WORKSPACE}/system/core/libion/include"
CPPFLAGS += "-I${S}/include"
CPPFLAGS += "-I${S}/libformatutils/inc"
CPPFLAGS += "-I${STAGING_KERNEL_BUILDDIR}/usr/include"
CPPFLAGS += "-I${STAGING_INCDIR}/linux-msm/usr/include"

do_install:append () {
    install -d ${D}/${includedir}/
    cp -fR ${S}/include/* ${STAGING_INCDIR}/
    cp -fR ${S}/include/* ${D}/${includedir}/
    install -d ${D}/usr/data/display
    install -m 0644 ${CFG_S}/vendor_display_build.prop \
    -D ${D}/usr/data/display/vendor_display_build.prop
}
do_configure[depends] += "virtual/kernel:do_shared_workdir"

SOLIBS = ".so"
FILES_SOLIBSDEV = ""
FILES:${PN} +="/usr/data/display/vendor_display_build.prop"
