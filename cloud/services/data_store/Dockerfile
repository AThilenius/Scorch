#===============================================================================
#===  AUTH =====================================================================
#===============================================================================
FROM ubuntu:14.04

#===  PYTHON / NODEJS  =========================================================
RUN apt-get update -qq && apt-get install -y python python-pip python-dev
RUN /bin/bash -c -l 'apt-get install -qy nodejs'
RUN /bin/bash -c -l 'apt-get install -qy npm'
RUN /bin/bash -c -l 'npm update -g express'
RUN update-alternatives --install /usr/bin/node node /usr/bin/nodejs 10

#===  NODE PROJECT SETUP  ======================================================
ADD app.js /home/core/node/
ADD bin /home/core/node/bin
ADD package.json /home/core/node/
ADD public /home/core/node/public
ADD routes /home/core/node/routes
ADD ../node_modules /home/core/node/node_modules
RUN cd /home/core/node && npm install

#===  ETCD  ====================================================================
ADD register_etcd.sh /home/core/node/
RUN chmod +x /home/core/node/register_etcd.sh


#===  MISX  ====================================================================
WORKDIR /home/core/node
CMD /bin/bash -c '\
  (\
    /home/core/node/register_etcd.sh services/http/auth $COREOS_PUBLIC_IPV4 6060\
      2> etcd.log &\
  ) && (\
    NODE_ENV=production PORT=80 npm start\
  )'
