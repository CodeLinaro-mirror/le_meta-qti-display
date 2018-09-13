FILESEXTRAPATHS_prepend := "${WORKSPACE}/graphics/:"
SRC_DIR = "${WORKSPACE}/graphics/weston/"
SRC_URI = "file://${@d.getVar('SRC_DIR', True).replace('${WORKSPACE}/graphics/', '')}"
REPO_SRC_URI = "file://${@d.getVar('SRC_DIR', True).replace('${WORKSPACE}/graphics/', '')}"
S = "${WORKDIR}/weston"
SOURCE_WESTON_PATCHES = "https://source.codeaurora.org/quic/le/AGL/meta-agl-demo/plain/recipes-graphics/wayland/weston/"
SRC_URI_append = "\
    https://source.codeaurora.org/quic/le/AGL/meta-agl/plain/meta-agl/recipes-graphics/wayland/weston/fix-touchscreen-crash.patch?h=automotivelinux/chinook;downloadfilename=fix-touchscreen-crash.patch;md5sum=62798230b8bb88f00ee43247fef61713;sha256sum=dd25f196cbe7e8b1ca59ec2b16e7f73dd43995c72ce2175447e3787b98635b28 \
"
SRC_URI_append = "\
    ${SOURCE_WESTON_PATCHES}/0001-weston-patch-for-wl-shell-emulator.patch?h=automotivelinux/chinook;downloadfilename=0001-weston-patch-for-wl-shell-emulator.patch;md5sum=ab4bbc2ec8d5eee375b6b8e5edcb203f;sha256sum=c44d787aa8fabf4f60ab4bf6c0f24cdc3817fbe763f384cf223b7979b44c77f0 \
    ${SOURCE_WESTON_PATCHES}/0001-ivi-shell-fix-TODO-which-expects-only-one-screen-in-.patch?h=automotivelinux/chinook;downloadfilename=0001-ivi-shell-fix-TODO-which-expects-only-one-screen-in-.patch;md5sum=b243e514098fa6978dd4c7e6080f3351;sha256sum=5791aee2ec7b408755d77c5ac01a882360c60fcafb69495f90acd0600efa74da \
    ${SOURCE_WESTON_PATCHES}/0002-ivi-shell-avoid-inserting-a-ivi_layer-to-multiple-sc.patch?h=automotivelinux/chinook;downloadfilename=0002-ivi-shell-avoid-inserting-a-ivi_layer-to-multiple-sc.patch;md5sum=390ef0d6ad7e34ff00e63883498e132a;sha256sum=c7e4adf7a5aadedb087cbd3704af9c9b0c8036d3a3b644d0076c53208e89cb22 \
    ${SOURCE_WESTON_PATCHES}/0003-ivi-shell-fix-layout_layer.view_list-is-not-initiliz.patch?h=automotivelinux/chinook;downloadfilename=0003-ivi-shell-fix-layout_layer.view_list-is-not-initiliz.patch;md5sum=f58ae6cb9100373a61a1f0d4e75c20d5;sha256sum=b0bb7d4c1bc701446ad631dc40f58fe4b4463c0c9f6360f5957578c24384a673 \
    ${SOURCE_WESTON_PATCHES}/0004-ivi-shell-remove-a-code-which-expects-only-a-screen-.patch?h=automotivelinux/chinook;downloadfilename=0004-ivi-shell-remove-a-code-which-expects-only-a-screen-.patch;md5sum=e13439a08fe622d7e605fd80880683c8;sha256sum=50243cbd9cfcfcf6365472ecb760ebfbd9a497c7f82b4d8a01fd961d750809f5 \
    ${SOURCE_WESTON_PATCHES}/0005-ivi-shell-multi-screen-support.-ivi_layout_screen-to.patch?h=automotivelinux/chinook;downloadfilename=0005-ivi-shell-multi-screen-support.-ivi_layout_screen-to.patch;md5sum=a82e3e17a569e9da55f2fc450b9aa224;sha256sum=6d3295a29eda5bbe05409b251f3d60650d0ade5137e0e618b17bf236a443618a \
    ${SOURCE_WESTON_PATCHES}/0006-ivi-shell-transforming-from-a-single-screen-coordina.patch?h=automotivelinux/chinook;downloadfilename=0006-ivi-shell-transforming-from-a-single-screen-coordina.patch;md5sum=7e29fbe0b9715ae56dd82b582f2e044e;sha256sum=a055d40ea563566b4e9e467d6021521a21c1e5ae13e3e595a4624a53d76f4bc9 \
    ${SOURCE_WESTON_PATCHES}/0007-RFR-ivi-shell-multi-screen-support-to-calcuration-of.patch?h=automotivelinux/chinook;downloadfilename=0007-RFR-ivi-shell-multi-screen-support-to-calcuration-of.patch;md5sum=04db444670948332220fc70e9fd7d9c8;sha256sum=2924b27224529d065d543c0f396ee9c32b061bd65baf5b82ad80ab12ca4aafea \
"

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

DEPENDS += "display-hal-linux display-noship-linux wayland-native gbm-headers display-ship-linux"
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
