#!/bin/sh
sudo apt-get install firebird2.5-superclassic firebird2.5-examples
sudo service firebird2.5-superclassic restart
PASSWORD=`sudo cat /etc/firebird/2.5/SYSDBA.password | grep PASS | sed -e 's/.*PASSWORD="\([^"]*\)".*/\1/'`
gsec -user SYSDBA -password $PASSWORD -modify sysdba -pw masterkey
sudo isql-fb -r sysdba -pas masterkey -input firebird.sql
