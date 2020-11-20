DESCRIPTION = "QTI Display drivers"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=3775480a712fc46a69647678acb234cb"

inherit linux-kernel-base

PR = "r0"

FILESPATH   =+ "${WORKSPACE}:"
SRC_URI     =  "file://start_display_le"
SRC_URI    +=  "file://display.service"
SRC_URI    +=  "file://display_load.conf"

S = "${WORKDIR}"
B = "${STAGING_KERNEL_BUILDDIR}"
KERNEL_VERSION = "${@get_kernelversion_headers('${B}')}"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install_append() {
	if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
	   install -d ${D}${sysconfdir}/initscripts
	   install -d ${D}${systemd_unitdir}/system/multi-user.target.wants/
	   install -m 755 ${WORKDIR}/start_display_le ${D}${sysconfdir}/initscripts
	   install -m 0644 ${WORKDIR}/display.service -D ${D}${systemd_unitdir}/system/display.service
	   install -m 0755 ${WORKDIR}/display_load.conf -D ${D}${sysconfdir}/modules-load.d/display_load.conf
	   # enable the service for multi-user.target
	   ln -sf ${systemd_unitdir}/system/display.service ${D}${systemd_unitdir}/system/multi-user.target.wants/display.service
	fi
}


FILES_${PN} += "${sysconfdir}/*"
FILES_${PN} += "/etc/initscripts/start_display_le"
FILES_${PN} += "${systemd_unitdir}/system/display.service"
FILES_${PN} += "${systemd_unitdir}/system/multi-user.target.wants/display.service"
