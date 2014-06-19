#!/bin/sh
sudo apt-get install firebird2.5-superclassic
sudo service firebird2.5-superclassic restart
#PASSWORD=`sudo cat /etc/firebird/2.5/SYSDBA.password | grep PASS | sed -e 's/.*PASSWORD="\([^"]*\)".*/\1/'`
#gsec -user SYSDBA -password $PASSWORD -modify sysdba -pw masterkey
sudo isql-fb -r sysdba -p masterkey -i travis/firebird.sql
