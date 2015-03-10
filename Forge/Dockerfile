FROM ubuntu:14.04

RUN apt-get update -q
RUN apt-get install -qy curl
RUN apt-get install -qy libmysqlclient-dev

RUN gpg --keyserver hkp://keys.gnupg.net --recv-keys D39DC0E3
RUN curl -sSL https://get.rvm.io | bash -s stable --rails
RUN /bin/bash -c -l 'rvm requirements'
RUN /bin/bash -c -l 'rvm install 2.1.2'
RUN /bin/bash -c -l 'rvm use 2.1.2'
RUN /bin/bash -c -l 'gem install bundler --no-ri --no-rdoc'
RUN /bin/bash -c -l 'gem install passenger'
RUN /bin/bash -c -l 'bundle config path "$HOME/bundler"'

# For Gemfile caching in Docker
WORKDIR /tmp 
ADD ./Gemfile Gemfile
ADD ./Gemfile.lock Gemfile.lock
RUN /bin/bash -c -l 'RAILS_ENV=production bundle install'

# ADD app pl-site/app
# ADD bin pl-site/bin
# ADD config pl-site/config
# ADD db pl-site/db
# ADD lib pl-site/lib
# ADD vendor pl-site/vendor
# ADD config.ru pl-site/
# ADD Rakefile pl-site/

ADD . pl-site
WORKDIR pl-site

CMD /bin/bash -c -l 'passenger start -p80 -e production'
