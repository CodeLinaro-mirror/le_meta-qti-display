
FILESEXTRAPATHS_append := ":${THISDIR}/libinput"

SRC_URI += "https://git.codelinaro.org/clo/le/external/libinput/libinput/-/commit/23d543b711cf027df3f7322e5dc51d352ed6179c.patch;downloadfilename=0001-udev-validate-input-devices-during-cold-plug.patch;md5sum=6159018629a68c458ee0012734edd464;sha256sum=c31755dd4097b903a237fdc23828b940ae8d76240bba55c8edf493ba52401944"
SRC_URI += "file://0002-libinput-fix-race-condition-for-device-add.patch"
