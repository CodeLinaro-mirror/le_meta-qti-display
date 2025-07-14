PACKAGE_ARCH = "${MACHINE_ARCH}"
DESCRIPTION = "display commonsys headers"

LICENSE = "BSD-3-Clause & BSD-3-Clause-Clear & Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
BSD-3-Clause;md5=550794465ba0ec5312d6919e203a55f9"
LIC_FILES_CHKSUM += "file://${COREBASE}/meta/files/common-licenses/\
BSD-3-Clause-Clear;md5=7a434440b651f4a472ca93716d01033a"

PR = "r1"

FILESEXTRAPATHS:prepend := "${WORKSPACE}:"
do_compile[noexec] = "1"

SRC_URI = "file://display/vendor/qcom/opensource/"
S = "${WORKDIR}/display/vendor/qcom/opensource/"

do_install() {
	install -d ${D}/usr/include/
	install -m 0755 ${B}/commonsys-intf/display/include/*.h -D ${D}${includedir}/
	install -m 0755 ${B}/display-intf/common/*.h -D ${D}${includedir}/
	install -m 0755 ${B}/display-intf/snapalloc/*.h -D ${D}${includedir}/
}
