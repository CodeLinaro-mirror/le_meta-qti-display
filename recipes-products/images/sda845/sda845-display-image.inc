# DISPLAY Open source Packages
IMAGE_INSTALL += "${@base_contains('MULTILIB_VARIANTS', 'lib32', 'lib32-display-hal', '', d)}"
IMAGE_INSTALL += "display-hal"
