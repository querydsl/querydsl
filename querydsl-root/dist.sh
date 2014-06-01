#!/bin/sh
rm -rf target/dist
mkdir -p target/dist

echo "Creating javadocs"
mvn javadoc:aggregate

echo "Creating release bundles"
for module in apt collections hibernate-search jpa jdo lucene3 lucene4 sql sql-codegen
do
  cd ../querydsl-$module
  mvn -Dtest=X clean assembly:assembly
done

echo "Creating reference documentation"
cd ../querydsl-docs
mkdir -p ../querydsl-root/target/dist/reference
mvn clean package
cp -R target/docbook/publish/en-US/* ../querydsl-root/target/dist/reference/
mvn -Dtranslation=ko-KR clean package
cp -R target/docbook/publish/ko-KR ../querydsl-root/target/dist/reference/
cd ../querydsl-root

echo "done."
