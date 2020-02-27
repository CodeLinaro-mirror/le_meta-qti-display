RDEPENDS += "display-hal"

copy_edge_display_rpm() {
    cp ${DEPLOY_DIR_RPM}/aarch64/display-hal-git-r8.aarch64.rpm ${IMAGE_ROOTFS}/packages/aarch64/
}

ROOTFS_POSTPROCESS_COMMAND += "copy_edge_display_rpm;"
