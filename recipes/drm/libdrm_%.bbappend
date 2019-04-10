FILESEXTRAPATHS_prepend := "${WORKSPACE}/graphics/:"
SRC_DIR = "${WORKSPACE}/graphics/libdrm/"
SRC_URI = "file://${@d.getVar('SRC_DIR', True).replace('${WORKSPACE}/graphics/', '')}"
REPO_SRC_URI = "file://${@d.getVar('SRC_DIR', True).replace('${WORKSPACE}/graphics/', '')}"
S = "${WORKDIR}/libdrm"

FILESEXTRAPATHS_append := ":${THISDIR}/${PN}"

EXTRA_OECONF += "${@bb.utils.contains('BASEMACHINE', 'qtiquingvm', '--enable-drm_fe=yes', '', d)}"
