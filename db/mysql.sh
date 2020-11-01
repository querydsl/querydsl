#!/usr/bin/env bash

mysql -uroot -p$MYSQL_ROOT_PASSWORD -e "CREATE SCHEMA querydsl"
mysql -uroot -p$MYSQL_ROOT_PASSWORD -e "CREATE SCHEMA querydsl2"

mysql -uroot -p$MYSQL_ROOT_PASSWORD -e "GRANT ALL PRIVILEGES ON *.* TO 'querydsl'@'%'"
