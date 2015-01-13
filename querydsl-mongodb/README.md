## Querydsl Mongodb

The Mongodb module provides integration with the Mongodb API.

**Maven integration**

 Add the following dependencies to your Maven project :

```XML
<dependency>
  <groupId>com.querydsl</groupId>
  <artifactId>querydsl-mongodb</artifactId>
  <version>${querydsl.version}</version>
</dependency>

<dependency>
  <groupId>org.slf4j</groupId>
  <artifactId>slf4j-log4j12</artifactId>
  <version>1.6.1</version>
</dependency>
```

And now, configure the Maven APT plugin which generates the query types used by Querydsl :

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
              <processor>com.querydsl.mongodb.morphia.MorphiaAnnotationProcessor</processor>
            </configuration>
          </execution>
        </executions>
      </plugin>
      ...
    </plugins>
  </build>
</project>
```

The MorphiaAnnotationProcessor finds domain types annotated with the com.google.code.morphia.annotations.Entity annotation and generates Querydsl query types for them.

Run `clean install` and you will get your Query types generated into target/generated-sources/java.

If you use Eclipse, run mvn eclipse:eclipse to update your Eclipse project to include target/generated-sources/java as a source folder.

Now you are able to construct Mongodb queries and instances of the query domain model. 

**Example query**

Querying with Querydsl Mongodb with Morphia is as simple as this : 

```JAVA
Morphia morphia;
Datastore datastore;
// ...  
QUser user = new QUser("user");
MorphiaQuery<User> query = new MorphiaQuery<User>(morphia, datastore, user);
List<User> list = query
    .where(user.firstName.eq("Bob"))
    .list();
```


For more information on the Querydsl Mongodb module visit the reference documentation http://www.querydsl.com/static/querydsl/latest/reference/html/ch02s07.html
