#!/bin/sh
echo "Creating javadocs"
mvn javadoc:aggregate

echo "Creating release bundles"
for module in collections hibernate-search hql jdoql lucene sql
do
  cd ../querydsl-$module
  mvn -Dtest= clean package assembly:assembly
done

echo "Creating reference documentation"
cd ../querydsl-docs
mvn -Dxslthl.config=http://docbook.sourceforge.net/release/xsl/current/highlighting/xslthl-config.xml clean package
cd ../querydsl-root

echo "done."
