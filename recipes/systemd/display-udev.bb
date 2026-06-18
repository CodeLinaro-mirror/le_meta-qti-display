SUMMARY = "Udev rules to enable permission for display nodes"

LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM += "file://${COREBASE}/meta/files/common-licenses/\
BSD-3-Clause-Clear;md5=7a434440b651f4a472ca93716d01033a"

PACKAGE_ARCH = "${MACHINE_ARCH}"

S = "${WORKDIR}"

SRC_URI += "file://display_udev_rules.rules \
            file://display_udev_script.sh "

do_install() {
        install -d ${D}${sysconfdir}/udev/rules.d/
        install -m 0644 ${WORKDIR}/display_udev_rules.rules ${D}${sysconfdir}/udev/rules.d/display_udev_rules.rules
        install -d ${D}${sysconfdir}/udev/scripts/
        install -m 0755 ${WORKDIR}/display_udev_script.sh ${D}${sysconfdir}/udev/scripts/display_udev_script.sh
}
