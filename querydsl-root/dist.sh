#!/bin/sh
echo "Creating javadocs"
mvn javadoc:aggregate

echo "Creating release bundles"
mvn -Passembly assembly:assembly

echo "Creating reference documentation"
cd ../querydsl-docs
./build.sh
cd ../querydsl-root

echo "done."
