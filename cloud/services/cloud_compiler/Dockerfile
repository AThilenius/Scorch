FROM ubuntu:14.04

# Python
RUN apt-get update -qq && apt-get install -y python python-pip python-dev

# Node.js
RUN /bin/bash -c -l 'apt-get install -qy nodejs'
RUN /bin/bash -c -l 'apt-get install -qy npm'
RUN /bin/bash -c -l 'npm update -g express'
RUN update-alternatives --install /usr/bin/node node /usr/bin/nodejs 10

ADD . ./njs-app
RUN cd /njs-app && npm install

WORKDIR /njs-app

CMD /bin/bash -c -l 'NODE_ENV=production PORT=80 npm start'
