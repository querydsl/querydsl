## Querydsl SQL

The SQL module provides integration with the JDBC API.

**Maven integration**

 Add the following dependencies to your Maven project :

```XML
<dependency>
  <groupId>com.querydsl</groupId>
  <artifactId>querydsl-sql</artifactId>
  <version>${querydsl.version}</version>
</dependency>
<dependency>
  <groupId>org.slf4j</groupId>
  <artifactId>slf4j-log4j12</artifactId>
  <version>1.6.1</version>
</dependency>
```

**Code generation via Maven**

This functionality is also available as a Maven plugin. The presented example can be declared like this in the POM :

```XML
<project>
  <build>
    <plugins>
      ...
      <plugin>
        <groupId>com.querydsl</groupId>
        <artifactId>querydsl-maven-plugin</artifactId>
        <version>${querydsl.version}</version>
        <executions>
          <execution>
            <goals>
              <goal>export</goal>
            </goals>
          </execution>
        </executions>
        <configuration>
          <jdbcDriver>org.apache.derby.jdbc.EmbeddedDriver</jdbcDriver>
          <jdbcUrl>jdbc:derby:target/demoDB;create=true</jdbcUrl>
          <packageName>com.myproject.domain</packageName>
          <targetFolder>${project.basedir}/target/generated-sources/java</targetFolder>
        </configuration>
        <dependencies>
          <dependency>
            <groupId>org.apache.derby</groupId>
            <artifactId>derby</artifactId>
            <version>${derby.version}</version>
          </dependency>
        </dependencies>
      </plugin>
      ...
    </plugins>
  </build>
</project>
```

Use the goal test-export to add the targetFolder as a test compile source root instead of a compile source root.

**Querying**

Querying with Querydsl SQL is as simple as this :

```JAVA 
QCustomer customer = new QCustomer("c");

SQLTemplates dialect = new HSQLDBTemplates(); // SQL-dialect
SQLQuery query = new SQLQuery(connection, dialect); 
List<String> lastNames = query.from(customer)
    .where(customer.firstName.eq("Bob"))
    .list(customer.lastName);
```
For more information on the Querydsl SQL module visit the reference documentation http://www.querydsl.com/static/querydsl/latest/reference/html/ch02s03.html
