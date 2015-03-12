FROM ubuntu:14.04

RUN apt-get update -q
RUN apt-get install -qy openjdk-7-jre

ADD . mc_server
WORKDIR mc_server

CMD /bin/bash -c -l "java -jar forge-1.7.10-10.13.2.1230-universal.jar nogui -db_host '$DB_HOST' -db_port '$DB_PORT' -db_database '$DB_DATABASE' -db_login '$DB_LOGIN' -db_password '$DB_PASSWORD'"