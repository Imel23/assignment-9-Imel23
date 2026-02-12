#!/bin/bash
# Script to build image for qemu.
# Author: Siddhant Jajoo.

git submodule init
git submodule sync
git submodule update

# local.conf won't exist until this step on first execution
source poky/oe-init-build-env

CONFLINE="MACHINE = \"qemuarm64\""

cat conf/local.conf | grep "${CONFLINE}" > /dev/null
local_conf_info=$?

if [ $local_conf_info -ne 0 ];then
	echo "Append ${CONFLINE} in the local.conf file"
	echo ${CONFLINE} >> conf/local.conf
	
else
	echo "${CONFLINE} already exists in the local.conf file"
fi

# When running as root (e.g. in CI containers), BitBake's sanity checker
# will refuse to run. Add an override to disable the sanity check.
if [ "$(whoami)" = "root" ]; then
	SANITYLINE='INHERIT:remove = "sanity"'
	cat conf/local.conf | grep "${SANITYLINE}" > /dev/null
	sanity_conf_info=$?
	if [ $sanity_conf_info -ne 0 ]; then
		echo "Running as root, disabling bitbake sanity check"
		echo "${SANITYLINE}" >> conf/local.conf
	fi
fi


bitbake-layers show-layers | grep "meta-aesd" > /dev/null
layer_info=$?

if [ $layer_info -ne 0 ];then
	echo "Adding meta-aesd layer"
	bitbake-layers add-layer ../meta-aesd
else
	echo "meta-aesd layer already exists"
fi

set -e
bitbake core-image-aesd