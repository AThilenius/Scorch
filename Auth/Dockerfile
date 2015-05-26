FROM ubuntu:14.04

# Python (needed for NodeJS)
RUN apt-get update -qq && apt-get install -y python python-pip python-dev

# Node.js
RUN /bin/bash -c -l 'apt-get install -qy nodejs'
RUN /bin/bash -c -l 'apt-get install -qy npm'
RUN /bin/bash -c -l 'npm update -g express'
RUN update-alternatives --install /usr/bin/node node /usr/bin/nodejs 10

# Supervisor
RUN apt-get install -qy supervisor
COPY supervisord.conf /etc/supervisor/conf.d/supervisord.conf

# Node JS
ADD app.js node/
ADD bin node/bin
ADD package.json node/
ADD public node/public
ADD routes node/routes
# Also need to add the master node_modules
ADD ../node_modules node/node_modules

# Finally, install all modules
RUN cd node && npm install

WORKDIR /node
CMD /bin/bash -c -l 'NODE_ENV=production PORT=80 npm start'
