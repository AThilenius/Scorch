#!/bin/bash

docker kill nginx_dev
docker rm nginx_dev
docker build -t nginx:dev .
docker run -d -p 3000:80 --name nginx_dev --env-file=/etc/environment nginx:dev
