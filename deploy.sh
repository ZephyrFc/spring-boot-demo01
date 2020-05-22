#!/usr/bin/env bash

RUN=$1

settingsPath=${HOME}/.m2/settings.xml
projectPath=$(pwd)

storagePkgName=syslog-storage-1.0.0-SNAPSHOT.jar

version=1.0.0-SNAPSHOT
#version=1.0.1

if [[ "${version}" == *SNAPSHOT ]]
then
  export repoId=nexus-snapshots
  export mvnRepo=http://218.17.41.205:20026/repository/maven-snapshots
else
  export repoId=nexus-releases
  export mvnRepo=http://218.17.41.205:20026/repository/maven-releases
fi

function deployPom(){
  mvn --settings "${settingsPath}" versions:set -DnewVersion="${version}" && \
  mvn --settings "${settingsPath}" -U -e -B -DskipTests=true deploy
}

function storagePkg(){
  cd syslog-storage && \
  mvn --settings "${settingsPath}" -U -e -B -DskipTests=true package spring-boot:repackage
}

function makeStorageArtifact() {
    rm -rf "${projectPath}/build/${version}" && \
    mkdir -p "${projectPath}/build/${version}" && \
    cp "${projectPath}/syslog-storage/target/${storagePkgName}" "${projectPath}/build/${version}" && \
    chmod a+x "${projectPath}/syslog-storage/scripts/storage.sh" && \
    cp "${projectPath}/syslog-storage/scripts/storage.sh"  "${projectPath}/build/${version}" && \
    cd "${projectPath}/build" && \
    tar -zcv -f "syslog-storage-${version}.tar.gz" "${version}"
}

if [ "$RUN" == "pkg" ]; then
  deployPom
  storagePkg
  makeStorageArtifact
fi

if [ "$RUN" == "upload" ]; then
  scp "${projectPath}/build/syslog-storage-${version}.tar.gz"  252:/app/update/sys-log/current
fi