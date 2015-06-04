#===============================================================================
#===  MongoDB  =================================================================
#===============================================================================
FROM ubuntu:14.04


#===  MongoDB  =================================================================
RUN apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 7F0CEB10
RUN echo 'deb http://downloads-distro.mongodb.org/repo/ubuntu-upstart dist 10gen' > /etc/apt/sources.list.d/mongodb.list
RUN apt-get update
RUN apt-get install -y mongodb-org
RUN apt-get install -y curl
RUN rm -rf /var/lib/apt/lists/*


#===  ETCD  ====================================================================
ADD register_etcd.sh /home/core/mongodb/
RUN chmod +x /home/core/mongodb/register_etcd.sh


#===  MISX  ====================================================================
RUN mkdir /home/core/mongodb/data
WORKDIR /home/core/mongodb
CMD /bin/bash -c '\
  (\
    /home/core/mongodb/register_etcd.sh databases/mongodb $COREOS_PUBLIC_IPV4\
    27017 &> /dev/null &\
  ) && (\
    mongod --dbpath /home/core/mongodb/data\
  )'
