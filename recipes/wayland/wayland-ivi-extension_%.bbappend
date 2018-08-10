#package libs from correct libdir after adding mulitilib support.

SOURCE_IVIEXT_PATCHES = "https://source.codeaurora.org/quic/le/AGL/meta-agl-demo/plain/recipes-graphics/wayland/wayland-ivi-extension/"
SRC_URI = "git://github.com/GENIVI/${PN}.git;protocol=https \
          "
SRCREV = "44598504503eea5ac7f94c88477a5a78bda01f30"

SRC_URI_append = "\
    ${SOURCE_IVIEXT_PATCHES}/0001-wayland-ivi-extension-patch-for-wl-shell-emulator.patch?h=automotivelinux/chinook;downloadfilename=0001-wayland-ivi-extension-patch-for-wl-shell-emulator.patch;md5sum=a5752111a6f0737ab37d7b23dbd674b9 \
    "

PACKAGE_ARCH = "${MACHINE_ARCH}"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
SRC_URI_append = "\
     file://0001-ivi-controller-enable-ivi-share-function.patch \
"
EXTRA_OECMAKE_remove = "-DIVI_SHARE=OFF"
EXTRA_OECMAKE = "-DIVI_SHARE=ON"

do_install_append() {
install -d ${D}${libdir}/
cp -r  ${D}/usr/lib/* ${D}${libdir}
rm -rf ${D}/usr/lib
}

FILES_${PN} += "${includedir}/*"
FILES_${PN} += "${libdir}/*.so*"
FILES_${PN}-dbg += "${libdir}/.debug/*"

INSANE_SKIP_${PN} += "dev-so"

FILES_${PN}-dev = ""

