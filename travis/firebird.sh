#!/bin/sh
sudo apt-get install firebird2.5-super firebird2.5-dev
sudo sed /ENABLE_FIREBIRD_SERVER=/s/no/yes/ -i /etc/default/firebird2.5
cat /etc/default/firebird2.5 | grep ENABLE_FIREBIRD_SERVER
sudo service firebird2.5-super start
#sudo service firebird2.5-superclassic restart
PASSWORD=`sudo cat /etc/firebird/2.5/SYSDBA.password | grep PASS | sed -e 's/.*PASSWORD="\([^"]*\)".*/\1/'`
gsec -user SYSDBA -password $PASSWORD -modify sysdba -pw masterkey
sudo isql-fb -r sysdba -p masterkey -i travis/firebird.sql
