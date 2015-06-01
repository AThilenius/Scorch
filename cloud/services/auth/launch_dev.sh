#!/bin/bash
source ${PWD/\/Scorch\/*//Scorch}/tools/shell/*

INFO 'Killing any old Docker Containers'
docker kill auth_dev 2> /dev/null
docker rm auth_dev 2> /dev/null

INFO 'Copying shared js libs'
cp -ar ../../shared/js_libs ./shared_js_libs || EROR\
        'Failed to copy shares js libs'

INFO 'Building Docker image'
docker build -t auth:dev . || EROR 'Failed to build Docker Image'

INFO 'Cleaning up shared_node_modules link'
rm -rf shared_js_libs

INFO 'Starting Docker container'
docker run -d -p 6060:80 --name auth_dev --env-file=/etc/environment auth:dev
