#===============================================================================
#===  AUTH =====================================================================
#===============================================================================
FROM ubuntu:14.04


#===  CURL / PYTHON / NODEJS  ==================================================
RUN apt-get update -qq
RUN apt-get install -y curl
RUN apt-get install -y python python-pip python-dev
RUN /bin/bash -c -l 'apt-get install -qy nodejs'
RUN /bin/bash -c -l 'apt-get install -qy npm'
RUN /bin/bash -c -l 'npm update -g express'
RUN update-alternatives --install /usr/bin/node node /usr/bin/nodejs 10


#===  NODE PROJECT SETUP  ======================================================
ADD package.json /home/core/node/
RUN cd /home/core/node && npm install
ADD app.js /home/core/node/
ADD bin /home/core/node/bin
ADD public /home/core/node/public
ADD routes /home/core/node/routes


#===  ETCD  ====================================================================
ADD register_etcd.sh /home/core/node/
RUN chmod +x /home/core/node/register_etcd.sh


#===  MISX  ====================================================================
ADD shared_js_libs /home/core/node/node_modules
WORKDIR /home/core/node
CMD /bin/bash -c '\
  DB_IP=$(node -pe "JSON.parse(JSON.parse(process.argv[1]).node.value).ip"\
    "$(curl -s -L http://172.17.42.1:4001/v2/keys/databases/mongodb)");\
  DB_PORT=$(node -pe "JSON.parse(JSON.parse(process.argv[1]).node.value).port"\
    "$(curl -s -L http://172.17.42.1:4001/v2/keys/databases/mongodb)");\
  (\
    /home/core/node/register_etcd.sh services/http/auth/1 $COREOS_PUBLIC_IPV4\
      6060 &> /dev/null &\
  ) && (\
    NODE_ENV=production PORT=80 MONGO_IP="$DB_IP" MONGO_PORT="$DB_PORT" npm\
      start\
  )'
