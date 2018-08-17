FILESEXTRAPATHS_prepend := "${WORKSPACE}/graphics/:"
SRC_DIR = "${WORKSPACE}/graphics/weston/"
SRC_URI = "file://${@d.getVar('SRC_DIR', True).replace('${WORKSPACE}/graphics/', '')}"
REPO_SRC_URI = "file://${@d.getVar('SRC_DIR', True).replace('${WORKSPACE}/graphics/', '')}"
S = "${WORKDIR}/weston"

FILESEXTRAPATHS_append := ":${THISDIR}/${PN}"
SRC_URI_append = "\
    file://weston.service_caf \
    file://weston.ini_caf \
    file://0001-outpub_fbdev-follow-the-work-flow-of-MSM8996.patch \
    file://drm_firmware_load_trigger.service \
"

CFLAGS += "-idirafter ${STAGING_KERNEL_DIR}/include/"

# Remove community patch which is conflict with Weston SDM optimization
SRC_URI_remove = "file://0001-compositor-drm.c-Launch-without-input-devices.patch"

EXTRA_OECONF_append = "\
    --enable-drm-compositor \
    --disable-fbdev-compositor \
"

DEPENDS += "display-hal-linux display-noship-linux wayland-native gbm-headers"
TARGET_CFLAGS += "-idirafter ${STAGING_KERNEL_DIR}/include/"
TARGET_CFLAGS += "-I${STAGING_INCDIR}/libdrm"
TARGET_CFLAGS += "-I${STAGING_INCDIR}/sdm"
TARGET_CFLAGS += "-I${WORKSPACE}/display/display-hal/include"
TARGET_CFLAGS += "-I${WORKSPACE}/display/display-hal/libdebug"
TARGET_CFLAGS += "-I${STAGING_INCDIR}/sdm/core"
TARGET_CFLAGS += "-lwayland-client"

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

    install -m 0644 ${WORKDIR}/weston.ini_caf ${D}${WESTON_INI_CONFIG}/weston.ini
    # expose weston protocol to /usr/share/weston as video may use it
    install ${WORKSPACE}/graphics/weston/protocol/*.xml ${D}${datadir}/weston
}


pkg_postinst_${PN} () {
    if ${@bb.utils.contains('BASEMACHINE', '8x96autofusion', 'true', 'false', d)}; then
        if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
            if [ -n "$D" ]; then
                OPTS="--root=$D"
            fi
            systemctl $OPTS mask weston.service
        fi
    fi
}

FILES_${PN} += "${systemd_unitdir}/system/"
FILES_SOLIBSDEV = ""
