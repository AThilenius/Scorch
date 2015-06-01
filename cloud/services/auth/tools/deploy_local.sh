#!/bin/bash
pushd ${PWD/\/Scorch\/*//Scorch}/cloud/services/auth/tools &> /dev/null
source ${PWD/\/Scorch\/*//Scorch}/tools/shell/*

# Auth/tools folder
INFO 'Checking that Boot2Docker is running...'
boot2docker up | grep 'Started' &> /dev/null
if [ $? != 0 ]; then
   EROR 'Failed to start boot2docker'
   exit 1
fi
$(boot2docker shellinit) &> /dev/null
INFO 'Boot2Docker Running'
# Build docker
INFO 'Building Docker'
/bin/bash build_docker.sh || EROR "Failed to build docker";


# Return to original pwd
popd &> /dev/null
