## Querydsl Collections

The Collections module provides integration with Java Collections and Beans.

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
  <artifactId>querydsl-collections</artifactId>
  <version>${querydsl.version}</version>
</dependency>
<dependency>
  <groupId>org.slf4j</groupId>
  <artifactId>slf4j-log4j12</artifactId>
  <version>1.6.1</version>
</dependency>   
```

If you are not using JPA or JDO you can generate Querydsl query types for your domain types by annotating them with the com.querydsl.core.annotations.QueryEntity annotation and adding the following plugin configuration into your Maven configuration (pom.xml) :

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
              <processor>com.querydsl.apt.QuerydslAnnotationProcessor</processor>
            </configuration>
          </execution>
        </executions>
      </plugin>
      ...
    </plugins>
  </build>
</project>
```

**Querying**

Querying with Querydsl Collections is as simple as this :

```JAVA
import static com.querydsl.collections.CollQueryFactory.*;

QCat cat = new QCat("cat");
for (String name : from(cat,cats)
  .where(cat.kittens.size().gt(0))
  .list(cat.name)){
    System.out.println(name);
}
```

For more information on the Querydsl Collections module visit the reference documentation http://www.querydsl.com/static/querydsl/latest/reference/html/ch02s08.html
