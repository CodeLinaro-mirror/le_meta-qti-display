EXTRA_OECONF += "${@bb.utils.contains('BASEMACHINE', 'qtiquingvm', '--enable-drm_fe=yes', '', d)}"
