#!/bin/sh
mvn javadoc:aggregate

cd ../querydsl-docs
./build.sh

for module in "collections hibernate-search hql jdoql lucene sql"
do
  cd ../querydsl-$module
  mvn assembly:assembly
done

cd ../querydsl-root
