#!/bin/sh
VERSION=$2

pre() {
  git checkout master
  git pull
  mvn -Pall,docs versions:set -DgenerateBackupPoms=false -DnewVersion=$VERSION
  sed -i '' "s/4\..\../$VERSION/" querydsl-docs/src/main/docbook/en-US/Querydsl_Reference.xml
  git checkout -b querydsl-$VERSION
  git add .
  git commit -m "Querydsl $VERSION"
  git push origin querydsl-$VERSION
  # TODO create PR
}

doit() {
  git checkout master
  git pull
  mvn versions:set -DgenerateBackupPoms=false
  mvn clean deploy -DskipTests -Dgpg.skip=false
  ./dist.sh
  # TODO upload
  echo -e "Don't forget \x1b[33mquerydsl-contrib\x1b[m."
  echo -e "After deploying the \x1b[33mreference documentation\x1b[m, update the \x1b[33mcurrent symlink\x1b[m"
}

post() {
  echo "post release stuff"
  # TODO update querydsl.com
  # TODO close github milestone
}

if [ “$1” == “pre” ]; then
  pre
elif [ “$1” == “doit” ]; then
  doit
elif [ “$1” == “post” ]; then
  post
else
  echo "./release (pre|doit|post) VERSION"
fi
