#!/bin/sh
echo "Creating reference documentation"
cd ../querydsl-docs
mvn clean package
cd ../querydsl-root
echo "done."
