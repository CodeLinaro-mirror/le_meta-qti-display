SUMMARY = "Startup script for the Weston Wayland compositor"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=b97a012949927931feb7793eee5ed924"
S = "${WORKDIR}"
DISTRO_FEATURES:append = " opengl"
FILESEXTRAPATHS:append := ":${THISDIR}/${PN}"

SRC_URI:append = "\
    file://init_qti \
    file://init_qti.service \
"

DISPLAY_SERVICE_FILENAME = "init_qti.service"

FILES:${PN} += "/data/*"

do_install:qrb5165() {
    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
        install -d ${D}/data/misc/display/
        install -m 0755 ${S}/init_qti -D ${D}${sysconfdir}/initscripts/init_qti_display
        install -d ${D}/etc/systemd/system/
        install -m 0755 ${S}/${DISPLAY_SERVICE_FILENAME} -D ${D}${sysconfdir}/systemd/system/init_display.service
        install -d ${D}/etc/systemd/system/multi-user.target.wants
        ln -sf /etc/systemd/system/init_display.service ${D}/etc/systemd/system/multi-user.target.wants/init_display.service
    else
        install -d ${D}/${sysconfdir}/init.d
        install -m755 ${S}/init_qti ${D}/${sysconfdir}/init.d/weston
    fi
}

SYSTEMD_SERVICE:${PN}:qrb5165 = "init_display.service"
