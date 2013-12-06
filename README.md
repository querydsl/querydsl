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

**Examples**

Querydsl example projects

* [Querydsl JPA example](https://github.com/mysema/querydsl-jpa-example)
* [Querydsl SQL example](https://github.com/mysema/querydsl-sql-example)

**Support**

Free support is provided in the Querydsl Google Group https://groups.google.com/forum/#!forum/querydsl

**How to build**

Querydsl provides releases via public Maven repositories, but you can build the sources also yourself like this

    cd querydsl-root
    mvn -DskipTests=true clean install 

For more information visit the project homepage at http://www.querydsl.com/.

**Vagrant/Puppet setup**

For running tests, a Vagrant/Puppet setup is provided. It is based on Ubuntu 12.04 and comes with the following databases:

* Oracle Express Edition 11g
* PostgreSQL 9.1.10
* MySQL 5.5.34
* Cubrid 9.2

You will need to install [VirtualBox], [Puppet], [Vagrant], the [vagrant-vbguest] plugin and [librarian-puppet]. You will also need to 
download the Oracle XE 11g installer file (```oracle-xe-11.2.0-1.0.x86_64.rpm.zip```) manually and 
place it in the ```devops``` directory.

To launch the virtual machine:

```
$> cd devops
devops$> librarian-puppet install
devops$> vagrant up
``` 

All of the databases' default ports are forwarded to the host machine. See the Vagrantfile for details.


**How to contribute**

GitHub pull requests are the way to contribute to Querydsl.

If you are unsure about the details of a contribution, ask on the Querydsl Google Group or create a ticket on GitHub.

[VirtualBox]: https://www.virtualbox.org/
[Vagrant]: http://www.vagrantup.com/
[Puppet]: http://puppetlabs.com/
[vagrant-vbguest]: https://github.com/dotless-de/vagrant-vbguest
[librarian-puppet]: http://librarian-puppet.com/
