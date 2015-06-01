FROM dockerfile/ubuntu


#===  NGINX  ===================================================================
RUN add-apt-repository -y ppa:nginx/stable
RUN apt-get update
RUN apt-get install -y nginx
RUN rm -rf /var/lib/apt/lists/*
RUN chown -R www-data:www-data /var/lib/nginx
# ADD ./nginx.conf /etc/nginx/nginx.conf


#===  CONFD  ===================================================================
# Install
RUN curl -L https://github.com/kelseyhightower/confd/releases/download/v0.7.1/confd-0.7.1-linux-amd64 -o /usr/local/bin/confd
RUN chmod +x /usr/local/bin/confd

# Setup directory, remove nginx defaults
RUN mkdir -p /etc/confd/{conf.d,templates}
RUN rm /etc/nginx/sites-enabled/default

# Add the template and confd config
ADD ./nginx.toml /etc/confd/conf.d/nginx.toml
ADD ./nginx.tmpl /etc/confd/templates/nginx.tmpl


#===  MISX  ====================================================================
# Startup Script
ADD ./confd-watch /usr/local/bin/confd-watch
RUN chmod +x /usr/local/bin/confd-watch

# Define default command.
CMD /usr/local/bin/confd-watch