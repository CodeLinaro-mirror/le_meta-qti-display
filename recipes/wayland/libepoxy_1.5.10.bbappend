FILESEXTRAPATHS:prepend := "${THISDIR}/"

REQUIRED_DISTRO_FEATURES:remove = "opengl"
PACKAGECONFIG[x11] = "-Dglx=yes, -Dglx=no -Dx11=false, virtual/libx11"
