#!/usr/bin/env bash
set -ex

docker-compose --file travis/docker-compose.yml up -d mysql postgresql firebird cubrid mongo
docker exec $(docker-compose --file travis/docker-compose.yml ps -q cubrid) /bin/bash -c 'mkdir -p ~/CUBRID_databases/demodb && cd $_ && cubrid createdb --db-volume-size=100M --log-volume-size=100M demodb en_US.iso88591 && cubrid loaddb -u dba -s $CUBRID/demo/demodb_schema -d $CUBRID/demo/demodb_objects demodb'
docker exec -d $(docker-compose --file travis/docker-compose.yml ps -q cubrid) /bin/bash -c 'cubrid server start demodb'
docker ps
