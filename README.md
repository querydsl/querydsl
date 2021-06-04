## Querydsl

Querydsl is a framework which enables the construction of type-safe SQL-like queries for multiple backends including JPA, MongoDB and SQL in Java.

Instead of writing queries as inline strings or externalizing them into XML files they are constructed via a fluent API.

[![Website shields.io](https://img.shields.io/website-up-down-green-red/http/querydsl.github.io.svg)](https://querydsl.github.io/)
[![Build Status](https://github.com/querydsl/querydsl/workflows/querydsl/badge.svg)](https://github.com/querydsl/querydsl/actions)
[![Coverage Status](https://coveralls.io/repos/github/querydsl/querydsl/badge.svg?branch=master)](https://coveralls.io/github/querydsl/querydsl?branch=master)
[![Stackoverflow](https://img.shields.io/badge/StackOverflow-querydsl-yellow.svg)](https://stackoverflow.com/questions/tagged/querydsl)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.querydsl/querydsl-core/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.querydsl/querydsl-core/)

**Getting started**

Use these tutorials to get started

* [Querying JPA](http://www.querydsl.com/static/querydsl/latest/reference/html/ch02.html#jpa_integration)
* [Querying SQL](http://www.querydsl.com/static/querydsl/latest/reference/html/ch02s03.html)
* [Querying Mongodb](http://www.querydsl.com/static/querydsl/latest/reference/html/ch02s07.html)
* [Querying Lucene](http://www.querydsl.com/static/querydsl/latest/reference/html/ch02s05.html)
* [Querying Collections](http://www.querydsl.com/static/querydsl/latest/reference/html/ch02s08.html)
* [Querydsl Spatial](http://www.querydsl.com/static/querydsl/latest/reference/html/ch02s04.html)
* [Querying JDO](http://www.querydsl.com/static/querydsl/latest/reference/html/ch02s02.html)

**Examples**

[Querydsl example projects](https://github.com/querydsl/querydsl/tree/master/querydsl-examples)

**Support**

Free support is provided in the [Discussion Section](https://github.com/querydsl/querydsl/discussions) and on [StackOverflow](http://stackoverflow.com/questions/tagged/querydsl).
Please do not post questions as issue. Such issues will be closed immediately.

**How to build**

Querydsl provides releases via public Maven repositories, but you can also build the sources yourself like this

```BASH
$ mvn -Pquickbuild,{projectname} clean install
```
Where projectname is one of the Maven profiles (e.g. `jpa`, `sql`, `mongodb`, etc. or `all`)

For more information visit the project homepage at https://querydsl.github.io.

**Docker Compose setup**

For running tests, a Docker Compose setup is provided. It comes with the following databases:

* Oracle Express Edition 11g
* PostgreSQL 9.1.10
* MySQL 5.5.34
* Cubrid 9.2

You will need to install [Docker] and [docker-compose].

To launch the database containers:

```BASH
$ docker-compose up -d
``` 

All of the databases' default ports are forwarded to the host machine.


**How to contribute**

GitHub pull requests are the way to contribute to Querydsl.

If you are unsure about the details of a contribution, ask on the Querydsl Google Group or create a ticket on GitHub.

[Docker]: https://www.docker.com/products/docker-desktop
[docker-compose]: https://docs.docker.com/compose/

### Slack

If you want to join Slack workspace for Querydsl contributors join by following [this link](https://join.slack.com/t/querydsl/shared_invite/zt-r7ufzz6q-zxIHgpOSSMFvoU3YU4SclQ).
