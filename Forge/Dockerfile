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

ADD . pl-site
WORKDIR pl-site
RUN /bin/bash -c -l "export AWS_ACCESS_KEY_ID=AKIAIXBMI5CUPWETNDCQ; \
export AWS_SECRET_ACCESS_KEY=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; \
export FOG_DIRECTORY=scorchforge; \
export FOG_PROVIDER=AWS; \
RAILS_ENV=production rake assets:precompile"

CMD /bin/bash -c -l 'passenger start -p80 -e production'