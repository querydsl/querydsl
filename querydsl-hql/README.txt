* When installing, remember to also genenerate-test-sources, e.g.

$ mvn clean generate-test-sources install -Dtest

* Or when creating Eclipse project files:

$ mvn clean generate-test-sources eclipse:clean eclipse:eclipse -DdownloadSources=true install -Dtest