#!/bin/bash
source ${PWD/\/Scorch\/*//Scorch}/tools/shell/*

INFO 'Killing any old Docker Containers'
docker kill nginx_dev 2> /dev/null
docker rm nginx_dev 2> /dev/null


INFO 'Building Docker image'
docker build -t nginx:dev . || EROR 'Failed to build Dockerfile'

INFO 'Starting Docker container'
docker run -d -p 3000:80 --name nginx_dev --env-file=/etc/environment nginx:dev
