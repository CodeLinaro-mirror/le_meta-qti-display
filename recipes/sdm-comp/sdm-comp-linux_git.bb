inherit autotools pkgconfig

DESCRIPTION = "display Library"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=3775480a712fc46a69647678acb234cb"

PR = "r8"

FILESPATH   =+ "${WORKSPACE}:"
SRC_URI     =  "file://display/vendor/qcom/opensource/display/sdm-composer"

S = "${WORKDIR}/display/vendor/qcom/opensource/display/sdm-composer"

DEPENDS += "display-hal-linux libsync libion display-noship libdmabufheap"
DEPENDS += "qmi-framework"

LDFLAGS += "-llog -lutils -lcutils -lion -lsync"

CPPFLAGS += "-I${WORKSPACE}/system/core/include"
CPPFLAGS += "-I${WORKSPACE}/system/core/libsync/include"
CPPFLAGS += "-I${WORKSPACE}/system/core/libion/include"
CPPFLAGS += "-I${S}/include"
CPPFLAGS += "-I${S}/libformatutils/inc"
CPPFLAGS += "-I${STAGING_KERNEL_BUILDDIR}/usr/include"

do_install_append () {
    cp -fR ${S}/include/* ${STAGING_INCDIR}/
}
do_configure[depends] += "virtual/kernel:do_shared_workdir"

SOLIBS = ".so"
FILES_SOLIBSDEV = ""
