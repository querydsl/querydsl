#!/bin/sh
git checkout master
git pull
mvn versions:set -DgenerateBackupPoms=false
mvn clean deploy -DskipTests -Dgpg.skip=false
./dist.sh
