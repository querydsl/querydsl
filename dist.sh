#!/bin/sh
rm -rf target/dist
mkdir -p target/dist

echo "Creating javadocs"
mvn javadoc:aggregate

echo "Creating release bundles"
for module in apt collections hibernate-search jpa jdo lucene3 lucene4 sql sql-codegen
do
  cd querydsl-$module
  mvn -Dtest=X clean assembly:assembly
  cd ..
done

echo "Creating reference documentation"
cd querydsl-docs
mkdir -p ../target/dist/reference
mvn -Dxslthl.config=http://docbook.sourceforge.net/release/xsl/current/highlighting/xslthl-config.xml clean package
cp -R target/docbook/publish/en-US/* ../target/dist/reference/
cd ..

echo "done."
