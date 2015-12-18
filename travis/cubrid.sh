#!/bin/sh
hostname | sed 's/^/127.0.0.1 /g' | cat - /etc/hosts > /tmp/etchoststemp && sudo mv /tmp/etchoststemp /etc/hosts --force
echo 'yes' | sudo add-apt-repository ppa:cubrid/cubrid
sudo apt-get update
sudo apt-get install cubrid
sudo apt-get install cubrid-demodb
/etc/profile.d/cubrid.sh
