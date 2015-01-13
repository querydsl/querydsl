## Querydsl JPA

The JPA module provides integration with the JPA 2 persistence API.

**Maven integration**

 Add the following dependencies to your Maven project :

```XML
<dependency>
  <groupId>com.querydsl</groupId>
  <artifactId>querydsl-apt</artifactId>
  <version>${querydsl.version}</version>
  <scope>provided</scope>
</dependency>    
    
<dependency>
  <groupId>com.querydsl</groupId>
  <artifactId>querydsl-jpa</artifactId>
  <version>${querydsl.version}</version>
</dependency>

<dependency>
  <groupId>org.slf4j</groupId>
  <artifactId>slf4j-log4j12</artifactId>
  <version>1.6.1</version>
</dependency>
```

And now, configure the Maven APT plugin :

```XML
<project>
  <build>
    <plugins>
      ...
      <plugin>
        <groupId>com.mysema.maven</groupId>
        <artifactId>apt-maven-plugin</artifactId>
        <version>1.1.3</version>
        <executions>
          <execution>
            <goals>
              <goal>process</goal>
            </goals>
            <configuration>
              <outputDirectory>target/generated-sources/java</outputDirectory>
              <processor>com.querydsl.apt.jpa.JPAAnnotationProcessor</processor>
            </configuration>
          </execution>
        </executions>
      </plugin>
      ...
    </plugins>
  </build>
</project>
```

The JPAAnnotationProcessor finds domain types annotated with the javax.persistence.Entity annotation and generates query types for them.

If you use Hibernate annotations in your domain types you should use the APT processor com.querydsl.apt.hibernate.HibernateAnnotationProcessor instead.

Run clean install and you will get your Query types generated into target/generated-sources/java.

If you use Eclipse, run mvn eclipse:eclipse to update your Eclipse project to include target/generated-sources/java as a source folder.

Now you are able to construct JPQL query instances and instances of the query domain model.     

**Querying**

Querying with Querydsl JPA is as simple as this :

```JAVA
QCustomer customer = QCustomer.customer;
JPAQuery query = new JPAQuery(entityManager);
Customer bob = query.from(customer)
  .where(customer.firstName.eq("Bob"))
  .uniqueResult(customer);
```

For more information on the Querydsl JPA module visit the reference documentation http://www.querydsl.com/static/querydsl/latest/reference/html/ch02.html#jpa_integration
