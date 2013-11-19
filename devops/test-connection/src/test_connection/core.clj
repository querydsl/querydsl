(ns test-connection.core
  (:require [clojure.java.jdbc :as j]
            [clojure.java.jdbc.sql :as s]))

(def oracle-db {:classname "oracle.jdbc.OracleDriver"  
                :subprotocol "oracle"
                :subname "thin:@localhost:1521:XE" 
                :user "querydsl"
                :password "querydsl"})

(def postgres-db {:classname "org.postgresql.Driver"
                  :subprotocol "postgresql"
                  :subname "//localhost:5432/querydsl"
                  :user "querydsl"
                  :password "querydsl"})

(def mysql-db {:classname "com.mysql.jdbc.Driver"
               :subprotocol "mysql"
               :subname "//localhost:3306/querydsl"
               :user "querydsl"
               :password "querydsl"})

(def cubrid-db {:classname "cubrid.jdbc.driver.CUBRIDDriver"
                :subprotocol "cubrid"
                :subname "localhost:30000:demodb:public::"})

(count (j/query oracle-db (s/select * :all_tables)))
(count (j/query postgres-db (s/select * :pg_catalog.pg_tables)))
(count (j/query mysql-db (s/select * :information_schema.tables)))
(count (j/query cubrid-db (s/select * :athlete)))


