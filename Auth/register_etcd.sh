#!/bin/bash

if [ $# < 3 ]; then
  echo "Usage:"
  echo "/bin/bash $0 <route> <public_ip> <port>"
  echo "You provided $# arguments, 3 expected"
  exit 1
fi

echo "Registering ETCD path [ $1 ] as ip: [ $2 ], port: [ $3 ]"
while true; do
  curl -s -L -X PUT http://$COREOS_PUBLIC_IPV4:4001/v2/keys/$1?ttl=30\
    -d value="{\"ip\":\"$2\", \"port\":$3}"
  sleep 20
done
