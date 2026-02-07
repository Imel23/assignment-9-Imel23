# Recipe for scull
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "git://git@github.com/Imel23/assignment-7-Imel23.git;protocol=ssh;branch=main \
           file://scull-start-stop"

PV = "1.0+git${SRCPV}"
SRCREV = "147c28293d44b99acec92220e2d2f16bd0a92700"

S = "${WORKDIR}/git/scull"

inherit module
inherit update-rc.d

INITSCRIPT_NAME = "scull-start-stop"
INITSCRIPT_PARAMS = "defaults 98"


EXTRA_OEMAKE += "KERNELDIR=${STAGING_KERNEL_DIR}"

FILES:${PN} += "${sysconfdir}/*"
RDEPENDS:${PN} += "kernel-module-scull"

do_install () {
    install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${WORKDIR}/scull-start-stop ${D}${sysconfdir}/init.d/${INITSCRIPT_NAME}

    unset CFLAGS CPPFLAGS CXXFLAGS LDFLAGS
    oe_runmake -C ${STAGING_KERNEL_DIR} M=${S} modules_install INSTALL_MOD_PATH=${D}
}
