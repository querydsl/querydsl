#!/bin/sh
sudo apt-get install -qq firebird2.5-super firebird2.5-dev
sudo sed /ENABLE_FIREBIRD_SERVER=/s/no/yes/ -i /etc/default/firebird2.5
cat /etc/default/firebird2.5 | grep ENABLE_FIREBIRD_SERVER
sudo service firebird2.5-super start
#PASSWORD=`sudo cat /etc/firebird/2.5/SYSDBA.password | grep PASS | sed -e 's/.*PASSWORD="\([^"]*\)".*/\1/'`
echo Create user
#sudo gsec -user sysdba -pass $PASSWORD -add querydsl -pw querydsl
sudo gsec -user sysdba -pass masterkey -add querydsl -pw querydsl
echo Create database
sudo isql-fb -u querydsl -pas querydsl -i travis/firebird.sql -q
#sudo isql-fb -u sysdba -pas $PASSWORD -i travis/firebird.sql -q
