#!/bin/bash
source ${PWD/\/Scorch\/*//Scorch}/tools/shell/*

INFO 'Killing any old Docker Containers'
docker kill nginx_dev 2> /dev/null
docker rm nginx_dev 2> /dev/null


INFO 'Building Docker image'
docker build -t nginx:dev . || EROR 'Failed to build Dockerfile'

INFO 'Starting Docker container'
docker run -d -p 3000:80 --name nginx_dev --env-file=/etc/environment nginx:dev

CONTAINER_ID=$(docker ps -a | grep nginx:dev | cut -f1 -d" ")
INFO "Started in container [$CONTAINER_ID]. Waiting a few seconds for logs"
sleep 4
docker logs $CONTAINER_ID
