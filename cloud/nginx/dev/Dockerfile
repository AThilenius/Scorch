#===============================================================================
#===  NGINX  ===================================================================
#===============================================================================
FROM ubuntu:14.04


#===  NGINX  ===================================================================
RUN apt-get update
RUN apt-get install -y curl
RUN apt-get install -y nginx


#===  CONFD  ===================================================================
RUN \
  cd /usr/local/bin &&\
  curl -L https://github.com/kelseyhightower/confd/releases/download/v0.9.0/confd-0.9.0-linux-amd64 -o confd &&\
  chmod +x confd

# Setup directory, remove nginx defaults
RUN rm /etc/nginx/sites-enabled/default

# Add the template and confd config
ADD ./nginx.toml /etc/confd/conf.d/nginx.toml
ADD ./nginx.tmpl /etc/confd/templates/nginx.tmpl


#===  MISX  ====================================================================
# Startup Scripts
ADD ./confd-watch /usr/local/bin/confd-watch
ADD ./register_etcd.sh /usr/local/bin/register_etcd.sh
RUN chmod +x /usr/local/bin/confd-watch
RUN chmod +x /usr/local/bin/register_etcd.sh

# Define default command.
CMD /bin/bash -c '\
  (\
    /usr/local/bin/register_etcd.sh proxies/http/nginx/1 $COREOS_PUBLIC_IPV4 80 &\
  ) && (\
    /usr/local/bin/confd-watch\
  )'
