#!/bin/sh
git checkout master
git pull
mvn versions:set -DgenerateBackupPoms=false
mvn clean deploy -DskipTests -Dgpg.skip=false
./dist.sh
echo -e "Don't forget \x1b[33mquerydsl-contrib\x1b[m."
echo -e "After deploying the \x1b[33mreference documentation\x1b[m, update the \x1b[33mcurrent symlink\x1b[m"
