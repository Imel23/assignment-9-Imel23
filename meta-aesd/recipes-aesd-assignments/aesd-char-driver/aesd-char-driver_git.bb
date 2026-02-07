
SUMMARY = "AESD Char Driver"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "git@github.com:Imel23/assignment-8-Imel23.git;protocol=ssh;branch=main"

PV = "1.0+git${SRCPV}"
SRCREV = "91eff8bbf2eda25551491b06e1d5a77b82e23ff1"

S = "${WORKDIR}/git/aesd-char-driver"

inherit module
inherit update-rc.d

INITSCRIPT_NAME = "aesd-char-driver-start-stop"
INITSCRIPT_PARAMS = "defaults 98"


EXTRA_OEMAKE:append:task-install = " -C ${STAGING_KERNEL_DIR} M=${S}/aesd-char-driver"

FILES:${PN} += "${bindir}/aesdchar_load ${bindir}/aesdchar_unload ${sysconfdir}/*"
RDEPENDS:${PN} += "kernel-module-aesdchar"


do_install () {
    install -d ${D}${bindir}
    install -m 0755 ${S}/aesdchar_load ${D}${bindir}/
    install -m 0755 ${S}/aesdchar_unload ${D}${bindir}/

    install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${S}/aesd-char-driver-start-stop ${D}${sysconfdir}/init.d/${INITSCRIPT_NAME}

    unset CFLAGS CPPFLAGS CXXFLAGS LDFLAGS
    oe_runmake -C ${STAGING_KERNEL_DIR} M=${S} modules_install INSTALL_MOD_PATH=${D}
}
