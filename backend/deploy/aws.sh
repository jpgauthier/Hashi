#!/bin/sh
BASEDIR=$(dirname $0)
cd $BASEDIR/..
activator universal:packageZipTarball
zip -j target/aws.zip deploy/Dockerfile target/universal/hashibackend-1.0-SNAPSHOT.tgz
