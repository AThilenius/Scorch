#!/bin/bash
source ${PWD/\/Scorch\/*//Scorch}/tools/shell/*

INFO 'Starting Nginx'
pushd ${PWD/\/Scorch\/*//Scorch}/cloud/nginx/dev
./launch_dev.sh
popd

INFO 'Starting Mongo DB'
pushd ${PWD/\/Scorch\/*//Scorch}/cloud/mongo_db
./launch_dev.sh
popd

INFO 'Starting Auth'
pushd ${PWD/\/Scorch\/*//Scorch}/cloud/services/auth
./launch_dev.sh
popd
