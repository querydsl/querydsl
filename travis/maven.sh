#!/bin/sh
wget http://ftp.cubrid.org/CUBRID_Drivers/JDBC_Driver/CUBRID-8.4.0_jdbc.jar
mvn install:install-file -Dfile=CUBRID-8.4.0_jdbc.jar -DgroupId=cubrid -DartifactId=cubrid-jdbc -Dversion=8.4.0 -Dpackaging=jar

