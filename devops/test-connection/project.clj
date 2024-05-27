; The Oracle JDBC driver is not available in any public Maven repository.
; You can however download it and install it in your local Maven repository manually:
;
; ./mvnw install:install-file -Durl=file:repo -Dfile=ojdbc6.jar -DgroupId=com.oracle \
;     -DartifactId=ojdbc6 -Dversion=11.2.0 -Dpackaging=jar
;

(defproject test-connection "0.1.0-SNAPSHOT"
  :repositories {"cubrid-releases" "http://maven.cubrid.org/"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/java.jdbc "0.3.0-alpha4"]
                 [com.oracle/ojdbc6 "11.2.0"]
                 [postgresql/postgresql "9.1-901.jdbc4"]
                 [mysql/mysql-connector-java "5.1.27"]
                 [cubrid/cubrid-jdbc "9.2.0.0155"]])
