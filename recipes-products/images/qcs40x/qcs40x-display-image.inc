# DISPLAY Open source Packages
IMAGE_INSTALL += "${@bb.utils.contains("MACHINE_FEATURES", "qti-display","wayland" , "",d)}"
IMAGE_INSTALL += "${@bb.utils.contains("MACHINE_FEATURES", "qti-display","weston" , "",d)}"
IMAGE_INSTALL += "${@bb.utils.contains("MACHINE_FEATURES", "qti-display","weston-init" , "",d)}"
IMAGE_INSTALL += "${@bb.utils.contains("MACHINE_FEATURES", "qti-display","display-hal-linux" , "",d)}"
