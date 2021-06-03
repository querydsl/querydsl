# Changelog

### 5.0.0

This release of QueryDSL targets Java 8 minimally and comes with various improvements to make QueryDSL ready for the modern Java ecosystem.
This version also removes `joda-time:joda-time`, `com.google.guava:guava`  and `com.google.code.findbugs:jsr305` as required runtime dependencies for using QueryDSL.

QueryDSL 5.0 is the long awaited major release after the QueryDSL project was left mostly unmaintained for over two years.
With this release the team worked hard on resolving the most pressing issues that have been requested repeatedly for a long time.

A huge thanks goes out to all contributors that made this release possible in their free time:

* **[@mp911de](https://github.com/mp911de)**, for working on the MongoDB Document API;
* **[@daniel-shuy](https://github.com/daniel-shuy)**, for working on decoupling `querydsl-sql` from `joda-time:joda-time`;
* **[@heesuk-ahn](https://github.com/heesuk-ahn)**, for working on improved Hibernate support and count query generation in `JPASQLQuery`;
* **[@harshtuna](https://github.com/harshtuna)**, for working on NullsLast ordering in `querydsl-collections`;
* **[@kherrala](https://github.com/kherrala)**, **[@ridoo](https://github.com/ridoo)** and **[@NikitaKochkurov](https://github.com/NikitaKochkurov)** for working on the JTS and GeoLatte upgrade for `querydsl-spatial`;
* **[@ridoo](https://github.com/ridoo)**, for working on Spatial support in `HibernateDomainExporter` and `JPADomainExporter`;
* **[@lpandzic](https://github.com/lpandzic)**, for working on codegen support for Java 15 records and general improvements;
* **[@F43nd1r](https://github.com/F43nd1r)**, for working on Kotlin Code generation, Java 11 support, general improvements and Continuous Integration;
* **[@jwgmeligmeyling](https://github.com/jwgmeligmeyling)**, **[@Shredder121](https://github.com/Shredder121)**, **[@johnktims](https://github.com/johnktims)**, **[@idosal](https://github.com/idosal)** and **[@robertandrewbain](https://github.com/robertandrewbain)**.

#### New features

* [#2672](https://github.com/querydsl/querydsl/pull/2672) - Various performance and code improvements possible by targeting Java 8 source level.
* [#2672](https://github.com/querydsl/querydsl/pull/2672) - Added `Fetchable#stream()` which returns a `Stream<T>`.
  Make sure that the returned stream is always closed to free up resources, for example using _try-with-resources_.
  It does not suffice to rely on a terminating operation on the stream for this (i.e. `forEach`, `collect`).
* [#2324](https://github.com/querydsl/querydsl/issues/2324) - Removal of Guava as dependency.
  Almost no required transitive dependencies to get started with QueryDSL.
  And no more conflicts with Guava versions required by your other tools or own application.
* [#2025](https://github.com/querydsl/querydsl/issues/2025) - `joda-time:joda-time` is no longer a required dependency for `querydsl-sql`.
  By default, the Java 8 date/time API is used for date/time operations.
  The `joda-time:joda-time` types will still be registered automatically if they are on the classpath.
* [#2215](https://github.com/querydsl/querydsl/issues/2215) - MongoDB 4 support through the Document API 
* [#2697](https://github.com/querydsl/querydsl/issues/2697) - Allow `com.querydsl.core.alias.Alias.*` to be used on a JRE by relying on ECJ as compiler
* [#2479](https://github.com/querydsl/querydsl/issues/2479) - Swap out JSR305 for Jetbrains Annotations.
  Because the Jetbrains Annotations, contrary to the JSR305 annotations, use a Class retention level, Jetbrains Annotations
  does not have to be available at runtime and is not a transitive dependency.
* [#658](https://github.com/querydsl/querydsl/issues/658) - Added `JPAExpressions#treat` which can be used to generate JPA 2.1 Treated path expressions.
* [#2666](https://github.com/querydsl/querydsl/issues/2666) - More descriptive error message when using unsupported query features in JPA.
* [#2106](https://github.com/querydsl/querydsl/issues/2106) - Support NullsLast ordering in `querydsl-collections`.
* [#2404](https://github.com/querydsl/querydsl/issues/2404) - Upgrade of JTS / Geolatte in `querydsl-spatial`
* [#2320](https://github.com/querydsl/querydsl/issues/2320) - Make Spatial support available to `HibernateDomainExporter` and `JPADomainExporter`. 
* [#2612](https://github.com/querydsl/querydsl/issues/2612) - Support jakarta.* packages for new Jakarta EE releases (available through the`jakarta` classifiers for Maven)
* [#1376](https://github.com/querydsl/querydsl/issues/1376) - Return typed expression from `nullif` and `coalesce` methods.
* [#1828](https://github.com/querydsl/querydsl/issues/1828) - Kotlin Codegen support
* [#2798](https://github.com/querydsl/querydsl/pull/2798) - Java Record support

#### Bugfixes

* [#2579](https://github.com/querydsl/querydsl/issues/2579) - Count query generation in `JPASQLQuery`
* [#2671](https://github.com/querydsl/querydsl/issues/2671) - Fixed a concurrency issue in `Alias.*`. `Alias.*` is now Thread safe.
* [#2053](https://github.com/querydsl/querydsl/issues/2053) - Work around issues with `AbstractJPAQuery#fetchResults` and `AbstractJPAQuery#fetchCount` in a query with a having clause by using an in-memory calculation.
* [#2504](https://github.com/querydsl/querydsl/issues/2504) - Work around issues with `AbstractJPAQuery#fetchResults` and `AbstractJPAQuery#fetchCount` in a query with multiple group by expressions by using an in-memory calculation.
* [#2663](https://github.com/querydsl/querydsl/issues/2663) - Fix issues with the JPA implementation of `InsertClause`.
* [#2706](https://github.com/querydsl/querydsl/pull/2706) - Fix a memory leak in `TemplateFactory`.
* [#2467](https://github.com/querydsl/querydsl/issues/2467) - Prevent `ExtendedBeanSerializer` from generating `toString` method twice
* [#2326](https://github.com/querydsl/querydsl/issues/2326) - Use JPA indexed parameters instead of HQL's legacy positional parameters
* [#2816](https://github.com/querydsl/querydsl/issues/2816) - Generated JPA query with incorrect argument binding indexes
* [#1413](https://github.com/querydsl/querydsl/issues/1413) - Incorrect parameter values with Hibernate custom types
* [#1429](https://github.com/querydsl/querydsl/issues/1429) - Reusing of constants in JPQL generation causes issues with hibernate query caching

#### Breaking changes

* Java 8 minimal requirement. If you still rely on Java <7, please use the latest 4.x.x version.
* `JavaSE6SQLExceptionWrapper` and other parts regarding pre-Java 7 exception handling are removed. 
* Removed bridge method that were in place for backwards compatibility of legacy API's. This may lead to some breaking API changes.
* Removed Guava as a dependency. If your application relies on Guava, make sure to add it as a direct dependency for your project and not rely on QueryDSL shipping it transitively.
* In order for Guava to be removed Mysema Codegen had to be rereleased as QueryDSL Codegen Utils.
  Therefore, the classes in this module moved to a different package: `com.mysema.codegen` is now `com.querydsl.codegen.utils`.
  This for example affects `com.mysema.codegen.model.SimpleType`.
  Although many applications won't touch the codgen internal classes, custom APT extensions might be affected by this.
* Due to the removal of Guava, any method that received an `ImmutableList` as parameter, now accepts any `List` instead.
  Normal code should handle this signature just fine.
  However, make sure to check any reflective uses of these parameters.
* The `querydsl.variableNameFunctionClass` property for the `DefaultConfiguration` should now be provided as a `java.util.function.Function` instead of a `com.google.common.base.Function`.
* `CodeWriter#beginStaticMethod` now takes a `java.util.function.Function` instead of a `com.google.common.base.Function`.
* `AbstractLuceneQuery` now takes a `java.util.function.Function` instead of a `com.google.common.base.Function`.
* `AbstractMongodbQuery` now takes a `java.util.function.Function` instead of a `com.google.common.base.Function`.
* `com.querydsl.codegen.NamingFunction`, `EvaluatorFunction`, `DefaultVariableFunction` now extend `java.util.function.Function` instead of `com.google.common.base.Function`.
* Any constructor that received a `javax.inject.Provider`, now takes a `java.util.function.Supplier` instead. In most cases you can replace the argument with `provider::get`.
* This release targets Hibernate 5.2 in the Hibernate integration. If you need Hibernate 4 dialect specific workarounds, use the `HQLTemplates` instead of the `Hibernate5Templates`.
* Removal of various deprecated methods.
* `joda-time:joda-time` is now an optional dependency. If your application relies on `joda-time:joda-time` make sure to specify it as a direct dependency rather than relying on QueryDSL to include it transitively.
* `com.google.code.findbugs:jsr305` is no longer a dependency. If your application currently relies on QueryDSL shipping JSR305 transitively, you should add JSR305 as a direct dependency to your project.
* MDC keys now use an underscore instead of a dot as separator: ` querydsl.query` now is `querydsl_query` and `querydsl.parameters` is `querydsl_parameters`.
* Removal of `PolyHedralSurface` in `querydsl-spatial` and `querydsl-sql-spatial` due to the upgrade of `geolatte-geom`.
* `com.querydsl.apt.Extension` moved to `com.querydsl.codegen` and now resides in the `querydsl-codegen` module.
* `com.querydsl.apt.SpatialSupport` moved to `com.querydsl.spatial.apt.SpatialSupport` and now resides in the `querydsl-spatial` module.
* `com.querydsl.sql.codegen.SpatialSupport` moved to `com.querydsl.sql.spatial.SpatialSupport` and now resides in the `querydsl-sql-spatial` module.
* `SQLServerGeometryReader` in `querydsl-sql-spatial` is removed in favour of `org.geolatte.geom.codec.db.sqlserver.*`.
* `PGgeometryConverter` in `querydsl-sql-spatial` is removed in favour of `org.geolatte.geom.codec.Wkt`.
* `JGeometryConverter` in `querydsl-sql-spatial` is removed in favour of `org.geolatte.geom.codec.db.oracle.*`.
* Removal of `HibernateDomainExporter` in `querysql-jpa-codegen`. `HibernateDomainExporter` only supported Hibernate 4, which QueryDSL no longer actively supports. Instead, use the `JPADomainExporter` with Hibernate.
* `ComparableExpression#coalesce` (and subtypes) no longer return a mutable `Coalesce` expression, but instead return a typed expression.
  If you need the Coalesce builder, use `new Coalesce<T>().add(expression)` instead.
* `getConstantToNamedLabel`, `getConstantToNumberedLabel` and `getConstantToAllLabels` that were temporarily introduced to `SerializerBase` and `JPQLSerializer` 
  in QueryDSL 4.3.0 to eventually replace `getConstantToLabel` are now removed in favor of `getConstants`.

#### Deprecations
* `AbstractJPAQuery#fetchResults` and `AbstractJPAQuery#fetchCount` are now deprecated for queries that have multiple group by
  expressions or a having clause, because these scenarios cannot be supported by pure JPA and are instead computed in-memory.
  If the total count of results is not necessary, we recommend to always use `AbstractJPAQuery#fetch` instead.
  If you want a reliable way of computing the result count for a paginated result for even the most complicated queries,
  we recommend using the [Blaze-Persistence QueryDSL integration](https://persistence.blazebit.com/documentation/1.5/core/manual/en_US/#querydsl-integration).
  `BlazeJPAQuery` properly implements both `fetchResults` and `fetchCount` and even comes with a `page` method.
* `getConstantToLabel` which was deprecated in QueryDSL 4.3.0 is no longer deprecated.

#### Dependency updates

* `cglib:cglib` to 3.3.0 for Java 8+ support
* `org.eclipse.jdt.core.compiler:ecj` to 4.6.1 for Java 8+ support
* `joda-time:joda-time` to 2.10.10 for better interoperability with other frameworks that use more recent versions than QueryDSL.
    `joda-time:joda-time` is also no longer a required dependency and as such is no longer provided transitively to your application.
    If your application relies on `joda-time:joda-time` being available, make sure to add the dependency to your project. 
* `org.geolatte:geolatte-geom` to 1.8.1 for better interopability with Hibernate Spatial.
  `querydsl-spatial` is still backwards compatible with older versions of Geolatte, however, `querydsl-sql-spatial` is not and requires 1.4.0 or newer.
* `com.vividsolutions:jts` to `org.locationtech:jts` for better interopability with Hibernate Spatial.
  `com.vividsolutions:jts` is still supported for `querydsl-spatial` if an older version of `org.geolatte:geolatte-geom` is provided.
* DataNucleus 5.2.x for Java 8+ support
  * JDO now uses `org.datanucleus:javax.jdo` instead of `javax.jdo:jdo-api`
* `com.google.guava:guava` is no longer a dependency of QueryDSL  and as such is no longer provided transitively to your application.
    If your application relies on `com.google.guava:guava` being available, make sure to add the dependency to your project.
* `com.google.code.findbugs:jsr305` is no longer a dependency of QueryDSL  and as such is no longer provided transitively to your application.
    If your application relies on `com.google.code.findbugs:jsr305` being available, make sure to add the dependency to your project.