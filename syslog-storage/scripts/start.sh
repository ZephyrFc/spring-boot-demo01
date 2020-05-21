#!/usr/bin/env bash

m=$(whoami)

version=1.0.0-SNAPSHOT

if [[ "${m}" == "root" ]]
then
  echo -e "* ** can't use ROOT user running this application ** *"
else
  nohup java -jar syslog-storage-${version}.jar  2>&1 < /dev/null &
fi
