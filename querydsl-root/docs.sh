#!/bin/sh
echo "Creating reference documentation"
cd ../querydsl-docs
mvn -Dxslthl.config=http://docbook.sourceforge.net/release/xsl/current/highlighting/xslthl-config.xml clean package
cd ../querydsl-root
echo "done."
