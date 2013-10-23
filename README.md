## Querydsl

Querydsl is a framework which enables the construction of type-safe SQL-like queries for multiple backends including JPA, JDO and SQL in Java.

Instead of writing queries as inline strings or externalizing them into XML files they are constructed via a fluent API.

**Getting started**

Use these tutorials to get started

* [Querying JPA](http://www.querydsl.com/static/querydsl/latest/reference/html/ch02.html#jpa_integration)
* [Querying SQL](http://www.querydsl.com/static/querydsl/latest/reference/html/ch02s03.html)
* [Querying JDO](http://www.querydsl.com/static/querydsl/latest/reference/html/ch02s02.html)
* [Querying Mongodb](http://www.querydsl.com/static/querydsl/latest/reference/html/ch02s06.html)
* [Querying Lucene](http://www.querydsl.com/static/querydsl/latest/reference/html/ch02s04.html)
* [Querying Collections](http://www.querydsl.com/static/querydsl/latest/reference/html/ch02s07.html)

**Support**

Free support is provided in the Querydsl Google Group https://groups.google.com/forum/#!forum/querydsl

**How to build**

Querydsl provides releases via public Maven repositories, but you can build the sources also yourself like this

    cd querydsl-root
    mvn -Pjenkins,all -DskipTests=true clean install 

For more information visit the project homepage at http://www.querydsl.com/.

**How to contribute**

GitHub pull requests are the way to contribute to Querydsl.

If you are unsure about the details of a contribution, ask on the Querydsl Google Group or create a ticket on GitHub.
