# Changelog

### 5.0.0

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

#### New features

* Added `Fetchable#stream()` which returns a `Stream<T>`.
  Make sure that the returned stream is always closed to free up resources, for example using _try-with-resources_.
  It does not suffice to rely on a terminating operation on the stream for this (i.e. `forEach`, `collect`).
* Removal of Guava as dependency.
  Almost no required transitive dependencies to get started with QueryDSL.
  And no more conflicts with Guava versions required by your other tools or own application.
* Fixed a concurrency issue in `Alias.*`. `Alias.*` is now Thread safe.

#### Dependency updates

* `cglib` to 3.3.0 for Java 8+ support
* `ecj` to 4.6.1 for Java 8+ support
* DataNucleus 5.2.x for Java 8+ support
  * JDO now uses `org.datanucleus:javax.jdo` instead of `javax.jdo:jdo-api`
  