#!/usr/bin/env bash

service_name=syslog-storage

ps -ef |grep ${service_name} |grep -v grep |awk '{print $2}' |xargs kill -15