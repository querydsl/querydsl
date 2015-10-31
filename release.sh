#!/bin/sh
git checkout QUERYDSL_3
git pull origin QUERYDSL_3
mvn versions:set -DgenerateBackupPoms=false
mvn clean deploy -DskipTests -Dgpg.skip=false
./dist.sh
