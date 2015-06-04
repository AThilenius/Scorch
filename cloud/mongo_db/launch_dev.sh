#!/bin/bash
source ${PWD/\/Scorch\/*//Scorch}/tools/shell/*

INFO 'Killing any old Docker Containers'
docker kill mongo_dev 2> /dev/null
docker rm mongo_dev 2> /dev/null

INFO 'Building Docker image'
docker build -t mongo:dev . || EROR 'Failed to build Docker Image'

INFO 'Starting Docker container'
docker run -d -p 27017:27017 --name mongo_dev --env-file=/etc/environment mongo:dev

CONTAINER_ID=$(docker ps -a | grep mongo:dev | cut -f1 -d" ")
INFO 'Started in container [$CONTAINER_ID]. Waiting a few seconds for logs'
sleep 4
docker logs $CONTAINER_ID
