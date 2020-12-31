# Changelog

### 5.0.0

This release of QueryDSL targets Java 8 minimally and comes with various improvements to make QueryDSL ready for the modern Java ecosystem.
This version also removes `joda-time`, `guava` and `JSR305` as required runtime dependencies for using QueryDSL.

QueryDSL 5.0 is the long awaited major release after the QueryDSL project was left mostly unmaintained for over two years.
With this release the team worked hard on resolving the most pressing issues that have been requested repeatedly for a long time.

A huge thanks goes out to all contributors that made this release possible in their free time:

* @mp911de, for working on the MongoDB Document API
* @daniel-shuy, for working on decoupling `querydsl-sql` from `joda-time`.
* @heesuk-ahn, for working on improved Hibernate support and count query generation in `JPASQLQuery`
* @jwgmeligmeyling, @Shredder121, @Johnktims and @idosal

#### New features

* [#2672](https://github.com/querydsl/querydsl/pull/2672) - Various performance and code improvements possible by targeting Java 8 source level.
* [#2672](https://github.com/querydsl/querydsl/pull/2672) - Added `Fetchable#stream()` which returns a `Stream<T>`.
  Make sure that the returned stream is always closed to free up resources, for example using _try-with-resources_.
  It does not suffice to rely on a terminating operation on the stream for this (i.e. `forEach`, `collect`).
* [#2324](https://github.com/querydsl/querydsl/issues/2324) - Removal of Guava as dependency.
  Almost no required transitive dependencies to get started with QueryDSL.
  And no more conflicts with Guava versions required by your other tools or own application.
* [#2025](https://github.com/querydsl/querydsl/issues/2025) - `joda-time` is no longer a required dependency for `querydsl-sql`.
  By default, the Java 8 date/time API is used for date/time operations.
  The `joda-time` types will still be registered automatically if they are on the classpath.
* [#2215](https://github.com/querydsl/querydsl/issues/2215) - MongoDB 4 support through the Document API 
* [#2697](https://github.com/querydsl/querydsl/issues/2697) - Allow `com.querydsl.core.alias.Alias.*` to be used on a JRE by relying on ECJ as compiler
* [#2479](https://github.com/querydsl/querydsl/issues/2479) - Swap out JSR305 for Jetbrains Annotations.

#### Bugfixes

* [#2579](https://github.com/querydsl/querydsl/issues/2579) - Count query generation in `JPASQLQuery`
* [#2671](https://github.com/querydsl/querydsl/issues/2671) - Fixed a concurrency issue in `Alias.*`. `Alias.*` is now Thread safe.

#### Breaking changes

* Java 8 minimal requirement. If you still rely on Java <7, please use the latest 4.x.x version.
* `JavaSE6SQLExceptionWrapper` and other parts regarding pre-Java 7 exception handling are removed. 
* Removed bridge method that were in place for backwards compatibility of legacy API's. This may lead to some breaking API changes.
* Removed Guava as a dependency. If your application relies on Guava, make sure to add it as a direct dependency for your project and not rely on QueryDSL shipping it transitively.
* In order for Guava to be removed Mysema Codegen had to be rereleased as QueryDSL Codegen Utils.
  Therefore, the classes in this module moved to a different package: `com.mysema.codegen` is now `com.querydsl.codegen.utils.model`.
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
* This release targets Hibernate 5 in the Hibernate integration. If you need Hibernate 4 dialect specific workarounds, use the `HQLTemplates` instead of the `Hibernate5Templates`.
* Removal of various deprecated methods.
* `joda-time` is now an optional dependency. If your application relies on `joda-time` make sure to specify it as a direct dependency rather than relying on QueryDSL to include it transitively.
* `com.google.code.findbugs:jsr305` is no longer a dependency. If your application currently relies on QueryDSL shipping JSR305 transitivily, you should add JSR305 as a direct dependency to your project.

#### Dependency updates

* `cglib` to 3.3.0 for Java 8+ support
* `ecj` to 4.6.1 for Java 8+ support
* `joda-time` to 2.10.8 for better interoperability with other frameworks that use more recent versions than QueryDSL.
* DataNucleus 5.2.x for Java 8+ support
  * JDO now uses `org.datanucleus:javax.jdo` instead of `javax.jdo:jdo-api`
  