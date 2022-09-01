SUMMARY = "Startup script for the Weston Wayland compositor"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=b97a012949927931feb7793eee5ed924"
S = "${WORKDIR}"
DISTRO_FEATURES_append = " opengl"
FILESEXTRAPATHS_append := ":${THISDIR}/${PN}"

SRC_URI_append = "\
    file://init_qti \
    file://init_qti.service \
    file://weston-sdmsteppe.ini \
    file://weston-qrb5165.ini \
"

DISPLAY_SERVICE_FILENAME = "init_qti.service"

FILES_${PN} += "/data/*"

do_install() {
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

do_install_append_sdmsteppe() {
    install -m 0755 ${S}/weston-sdmsteppe.ini -D ${D}${sysconfdir}/xdg/weston/weston.ini
}

do_install_append_qrb5165() {
    install -m 0755 ${S}/weston-qrb5165.ini -D ${D}${sysconfdir}/xdg/weston/weston.ini
}

SYSTEMD_SERVICE_${PN} = "init_display.service"
