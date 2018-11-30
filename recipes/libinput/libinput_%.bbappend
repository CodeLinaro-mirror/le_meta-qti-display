
FILESEXTRAPATHS_append := ":${THISDIR}/libinput"

SRC_URI += "https://source.codeaurora.org/quic/le/external/libinput/libinput/patch/?id=23d543b711cf027df3f7322e5dc51d352ed6179c;downloadfilename=0001-udev-validate-input-devices-during-cold-plug.patch;md5sum=6bdcf7b759bc386c6d6215472ad43124;sha256sum=ab9e876e3e339b01ba0932684eb04e2db20bcb5007d01179c769801aa4f76a8f"
SRC_URI += "file://0002-libinput-fix-race-condition-for-device-add.patch"
