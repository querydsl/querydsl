#!/bin/sh
VERSION=$2
TAG=QUERYDSL_`sed 's/\./_/g' <<< $VERSION`

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
  git pull --ff-only
  mvn clean deploy -DskipTests -Dgpg.skip=false
  ./dist.sh
  ssh root@querydsl.com "mkdir /var/www/html/static/querydsl/$VERSION"
  scp -r target/dist/* root@querydsl.com:/var/www/html/static/querydsl/$VERSION/
  ssh root@querydsl.com "cd /var/www/html/static/querydsl && unlink latest && ln -sT $VERSION latest"
  git tag $TAG
  git push --tags
  echo -e "Don't forget \x1b[33mquerydsl-contrib\x1b[m."
}

post() {
  echo "post release stuff"
  # TODO update querydsl.com
  # TODO close github milestone
  # TODO bump version
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
