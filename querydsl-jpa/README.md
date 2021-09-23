## Querydsl JPA

The JPA module provides integration with the JPA 2 persistence API.

**Maven integration**

 Add the following dependencies to your Maven project :

```XML
<dependency>
  <groupId>com.querydsl</groupId>
  <artifactId>querydsl-jpa</artifactId>
  <version>${querydsl.version}</version>
</dependency>
```

And now, configure the Maven APT plugin :

```XML
<project>
  <build>
    <plugins>
      ...
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <version>3.8.1</version>
        <configuration>
            <annotationProcessors>
                <annotationProcessor>com.querydsl.apt.jpa.JPAAnnotationProcessor</annotationProcessor>
            </annotationProcessors>
            <annotationProcessorPaths>
                <path>
                    <groupId>com.querydsl</groupId>
                    <artifactId>querydsl-apt</artifactId>
                    <version>${querydsl.version}</version>
                    <classifier>jpa</classifier>
                </path>
                <path>
                    <groupId>jakarta.persistence</groupId>
                    <artifactId>jakarta.persistence-api</artifactId>
                    <version>2.2.3</version>
                </path>
                <!-- Only necessary when using JDK 11+ -->
                <path>
                    <groupId>jakarta.annotation</groupId>
                    <artifactId>jakarta.annotation-api</artifactId>
                    <version>1.3.5</version>
                </path>
            </annotationProcessorPaths>
        </configuration>
        ...
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
JPAQuery<?> query = new JPAQuery<Void>(entityManager);
Customer bob = query.select(customer)
  .from(customer)
  .where(customer.firstName.eq("Bob"))
  .fetchOne();
```

For more information on the Querydsl JPA module visit the reference documentation http://www.querydsl.com/static/querydsl/latest/reference/html/ch02.html#jpa_integration
