#!/bin/bash

# General
sudo locale-gen "en_US.UTF-8"
sudo dpkg-reconfigure locales

# Oracle XE 11g
# TODO

# MySQL 5.6
sudo debconf-set-selections <<< 'mysql-server mysql-server/root_password password root'
sudo debconf-set-selections <<< 'mysql-server mysql-server/root_password_again password root'
sudo apt-get -q -y install mysql-server-5.6
mysql -u root -proot -e "source /opt/querydsl/sql-snippets/mysql.sql"

# PostgreSQL 9.3
sudo apt-get -q -y install postgresql postgresql-contrib
sudo apt-get -q -y install postgis postgresql-9.3-postgis-2.1
cp /opt/querydsl/sql-snippets/postgresql.sql /tmp/
sudo -u postgres psql -U postgres -f /tmp/postgresql.sql

# Cubrid
# TODO
#echo 'yes' | sudo add-apt-repository ppa:cubrid/cubrid
#sudo apt-get update
#sudo apt-get install -y cubrid cubrid-demodb
#/etc/profile.d/cubrid.sh
#hostname | sed 's/^/127.0.0.1 /g' | cat - /etc/hosts > /tmp/etchoststemp && sudo mv /tmp/etchoststemp /etc/hosts --force

# Firebird
# TODO

# Mongodb
# based on https://docs.mongodb.org/manual/tutorial/install-mongodb-on-ubuntu/
sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 7F0CEB10
echo "deb http://repo.mongodb.org/apt/ubuntu trusty/mongodb-org/3.0 multiverse" | sudo tee /etc/apt/sources.list.d/mongodb-org-3.0.list
sudo apt-get update
sudo apt-get -q -y install mongodb-org