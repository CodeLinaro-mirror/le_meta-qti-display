RDEPENDS += "weston"
RDEPENDS += "libdrm"
RDEPENDS += "wayland"

copy_edge_display_rpm() {
    cp ${DEPLOY_DIR_RPM}/aarch64/weston-3.0.0-r0.aarch64.rpm ${IMAGE_ROOTFS}/packages/aarch64/
    cp ${DEPLOY_DIR_RPM}/aarch64/libdrm2-2.4.83-r0.aarch64.rpm ${IMAGE_ROOTFS}/packages/aarch64/
    cp ${DEPLOY_DIR_RPM}/aarch64/wayland-1.14.0-r0.aarch64.rpm ${IMAGE_ROOTFS}/packages/aarch64/
    cp ${DEPLOY_DIR_RPM}/noarch/wayland-protocols-1.12-r0.noarch.rpm ${IMAGE_ROOTFS}/packages/noarch/
    cp ${DEPLOY_DIR_RPM}/aarch64/libinput10-1.8.4-r0.aarch64.rpm ${IMAGE_ROOTFS}/packages/aarch64/
    cp ${DEPLOY_DIR_RPM}/aarch64/libweston-3-3.0.0-r0.aarch64.rpm ${IMAGE_ROOTFS}/packages/aarch64/
}

ROOTFS_POSTPROCESS_COMMAND += "copy_edge_display_rpm;"
