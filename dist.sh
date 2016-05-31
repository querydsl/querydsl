#!/bin/sh
rm -rf target/dist
mkdir -p target/dist
mvn javadoc:aggregate

for module in apt collections hibernate-search jpa jdo lucene3 lucene4 sql sql-codegen
do
  mvn -pl querydsl-$module -Dtest=X clean assembly:assembly
done

mkdir -p target/dist/reference
mvn -f querydsl-docs/pom.xml -Dxslthl.config=http://docbook.sourceforge.net/release/xsl/current/highlighting/xslthl-config.xml clean package
cp -R querydsl-docs/target/docbook/publish/en-US/* target/dist/reference/
