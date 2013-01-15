# Querydsl #

Querydsl is a framework which enables the construction of type-safe SQL-like queries for multiple backends including JPA, JDO and SQL in Java.

Instead of writing queries as inline strings or externalizing them into XML files they are constructed via a fluent API.

*How to build*

Querydsl provides releases via public Maven repositories, but you can build the sources also yourself like this

    cd querydsl-root
    mvn -Pjenkins,all -DskipTests=true clean install 

For more information visit the project homepage at http://www.querydsl.com/.
