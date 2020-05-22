#!/usr/bin/env bash

m=$(whoami)

if [[ "${m}" == "root" ]]
then
  echo -e "* ** can't use ROOT user running this application ** *"
  exit 1
fi

app='syslog-storage-1.0.0-SNAPSHOT.jar'
args='-Xms1g -Xmx1g'

cmd=$1

pidfile=./pid

startup(){
  if [ ! -f "$pidfile" ]
  then
	  java -jar $args $app > /dev/null 2>&1 &
    echo $! > $pidfile
  else
	  echo "$app is running at pid $(cat $pidfile) ?"
	  echo "$app is abnormal stopped, pls remove $(pwd)/pid file and try 'start' again."
  fi
}

stop(){
  if [ -f "$pidfile" ]
  then
	   # shellcheck disable=SC2002
	   pid=$(cat $pidfile)
	   kill -15 $pid
	   sleep 3
	   rm -rf $pidfile
	   echo "$app pid ${pid} is stopped."
  else
	  echo "$app is not start ?"
  fi
}

if [ ! "$cmd" ]; then
  echo "Please specify args 'start|restart|stop'"
  exit
fi

if [ "$cmd" == 'start' ]; then
  startup
fi

if [ "$cmd" == 'restart' ]; then
  stop
  startup
fi

if [ "$cmd" == 'stop' ]; then
  stop
fi