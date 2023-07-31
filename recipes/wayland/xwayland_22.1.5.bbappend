FILESEXTRAPATHS:prepend := "${THISDIR}/"
DEPENDS:append += "libxshmfence"

REQUIRED_DISTRO_FEATURES:remove = "opengl"

PACKAGECONFIG:remove = "glx"
PACKAGECONFIG[glamor] = "-Dglamor=true,-Dglamor=false,libepoxy virtual/libgbm,virtual/egl"
