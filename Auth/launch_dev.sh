#!/bin/bash

docker kill auth_dev
docker rm auth_dev
docker build -t auth:dev .
docker run -d -p 6060:80 --name auth_dev --env-file=/etc/environment auth:dev
