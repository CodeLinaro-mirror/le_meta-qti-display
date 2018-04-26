FILESEXTRAPATHS_prepend := "${WORKSPACE}/graphics/:"
SRC_DIR = "${WORKSPACE}/graphics/weston/"
SRC_URI = "file://${@d.getVar('SRC_DIR', True).replace('${WORKSPACE}/graphics/', '')}"
REPO_SRC_URI = "file://${@d.getVar('SRC_DIR', True).replace('${WORKSPACE}/graphics/', '')}"
S = "${WORKDIR}/weston"

inherit agl-app-user
RDEPENDS_{PN} += " agl-users"

WESTONSTART ??=  "/usr/bin/weston ${WESTONARGS}"

python __anonymous () {

    # add early_init to DISTRO_FEATURES to use early user space feature
    if bb.utils.contains('DISTRO_FEATURES', 'early_init', True, False, d) or bb.utils.contains('DISTRO_FEATURES', 'early-ethernet', True, False, d):
        d.appendVar("SRC_URI", " file://0001-weston-early-init-support.patch")

}

FILESEXTRAPATHS_append := ":${THISDIR}/${PN}"
SRC_URI_append = "\
    file://weston.service_caf \
    file://weston_tmpfiles.conf \
    file://weston.ini_caf \
    file://0001-outpub_fbdev-follow-the-work-flow-of-MSM8996.patch \
    file://drm_firmware_load_trigger.service \
"

CFLAGS += "-idirafter ${STAGING_KERNEL_DIR}/include/"

# Remove community patch which is conflict with Weston SDM optimization
SRC_URI_remove = "file://0001-compositor-drm.c-Launch-without-input-devices.patch"

EXTRA_OECONF_append = "\
    --enable-drm-compositor \
"

EXTRA_OECONF_append = " --enable-sys-uid"

DEPENDS += "display-hal-linux display-noship-linux"
TARGET_CFLAGS += "-idirafter ${STAGING_KERNEL_DIR}/include/"
TARGET_CFLAGS += "-I${STAGING_INCDIR}/sdm"
TARGET_CFLAGS += "-I${STAGING_INCDIR}/sdm/core"

#
# Compositor choices
#
# Weston on KMS
PACKAGECONFIG[kms] = "--enable-drm-compositor,drm udev libgbm mtdev"
# Weston on Wayland (nested Weston)
PACKAGECONFIG[wayland] = "--enable-wayland-compositor,--disable-wayland-compositor,libgbm"

inherit systemd

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "weston.service"

do_install_append() {
    # Install systemd unit files
    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
        install -m 644 -p -D ${WORKDIR}/weston.service_caf ${D}${systemd_system_unitdir}/weston.service
    fi

    sed -e 's,Conflicts=getty@tty.*,Conflicts=getty@tty${WESTONTTY}.service,g' \
        -e 's,User=root,User=${WESTONUSER},g' \
        -e 's,Group=root,Group=${WESTONGROUP},g' \
        -e 's,ExecStart=.*,ExecStart=${WESTONSTART},g' \
        -e 's,@WESTONTTY@,${WESTONTTY},g' \
        -e 's,@XDG_RUNTIME_DIR@,${DISPLAY_XDG_RUNTIME_DIR},g' \
        -i ${D}${systemd_system_unitdir}/weston.service

    install -d ${D}${sysconfdir}/udev/rules.d
    cat >${D}${sysconfdir}/udev/rules.d/99-zz-dri.rules <<'EOF'
SUBSYSTEM=="drm", MODE="0660", GROUP="${WESTONGROUP}", SECLABEL{smack}="*", TAG+="systemd", ENV{SYSTEMD_WANTS}="weston.service"
EOF

    # user 'display' must own /dev/tty${WESTONTTY} for weston to start correctly
    cat >${D}${sysconfdir}/udev/rules.d/99-zz-tty.rules <<'EOF'
SUBSYSTEM=="tty", KERNEL=="tty${WESTONTTY}", OWNER="${WESTONUSER}", SECLABEL{smack}="^", TAG+="systemd", ENV{SYSTEMD_WANTS}="weston.service"
EOF

    # user 'display' must also be able to access /dev/input/*
    cat >${D}${sysconfdir}/udev/rules.d/99-zz-input.rules <<'EOF'
SUBSYSTEM=="input", MODE="0660", GROUP="input", SECLABEL{smack}="^", TAG+="systemd", ENV{SYSTEMD_WANTS}="weston.service"
EOF

    # user 'display' must also be able to access /dev/media*, etc.
    cat >${D}${sysconfdir}/udev/rules.d/99-zz-remote-display.rules <<'EOF'
SUBSYSTEM=="media", MODE="0660", GROUP="display", SECLABEL{smack}="*", TAG+="systemd", ENV{SYSTEMD_WANTS}="weston.service"
SUBSYSTEM=="video4linux", MODE="0660", GROUP="display", SECLABEL{smack}="*", TAG+="systemd", ENV{SYSTEMD_WANTS}="weston.service"
EOF

    # Prepare the dir for weston socket
    install -d ${D}${sysconfdir}/tmpfiles.d
    install -Dm755 ${WORKDIR}/weston_tmpfiles.conf ${D}/${sysconfdir}/tmpfiles.d/weston.conf

    sed -e 's,@WESTONUSER@,${WESTONUSER},g' \
        -e 's,@WESTONGROUP@,${WESTONGROUP},g' \
        -i ${D}/${sysconfdir}/tmpfiles.d/weston.conf

    install -m 0644 ${WORKDIR}/weston.ini_caf ${D}${WESTON_INI_CONFIG}/weston.ini
    # expose weston protocol to /usr/share/weston as video may use it
    install ${WORKSPACE}/graphics/weston/protocol/*.xml ${D}${datadir}/weston
}


pkg_postinst_${PN} () {
    setcap all=eip $D/usr/bin/weston
    if ${@bb.utils.contains('BASEMACHINE', '8x96autofusion', 'true', 'false', d)}; then
        if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
            if [ -n "$D" ]; then
                OPTS="--root=$D"
            fi
            systemctl $OPTS mask weston.service
        fi
    fi
}

FILES_${PN} += "${systemd_unitdir}/system/ ${sysconfdir}/"
FILES_SOLIBSDEV = ""
