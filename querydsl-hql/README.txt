* When installing, remember to also genenerate-test-sources, e.g.

$ mvn clean generate-test-sources install -Dtest

* Or when creating Eclipse project files:

$ mvn clean generate-test-sources eclipse:clean eclipse:eclipse -DdownloadSources=true install -Dtest

* For tests, configure your JDBC connection in (see default.properties.sample for an example) 

/querydsl-hql/src/test/resources/com/mysema/query/hql/default.properties

