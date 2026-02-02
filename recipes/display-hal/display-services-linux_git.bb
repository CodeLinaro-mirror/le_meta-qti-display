inherit autotools pkgconfig

PACKAGE_ARCH = "${MACHINE_ARCH}"

DESCRIPTION = "display Library"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

PR = "r8"

FILESPATH   =+ "${WORKSPACE}:"
SRC_URI = "file://display/hardware/qcom/display"

S = "${WORKDIR}/display/hardware/qcom/display"
QDCM_S = "${WORKSPACE}/display/vendor/qcom/opensource/display-core/"

DEPENDS += "binder libcutils"

LDFLAGS += "-llog -lutils -lcutils"

EXTRA_OECONF += " --with-sanitized-headers=${STAGING_INCDIR}/linux-msm/usr/include"

do_install:append () {
  install -d ${D}/data/vendor/display/
  install -d ${D}/vendor/etc/
  install -m 0755 ${S}/config/clstc_config_library.xml ${D}/vendor/etc/
  install -m 0755 ${QDCM_S}/config/snapdragon_color_libs_config.xml ${D}/vendor/etc/
}

CPPFLAGS += "-fno-operator-names"
CPPFLAGS += "-DTRUSTED_VM"

FILES:${PN} += "/vendor/etc/*"
FILES:${PN} += "/data/vendor/display/"

SOLIBS = ".so"
FILES_SOLIBSDEV = ""
