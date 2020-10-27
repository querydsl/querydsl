# Changelog

## 5.0.0

### Breaking changes

* Java 8 minimal requirement. If you still rely on Java <7, please use the latest 4.x.x version.
* Removed bridge method that were in place for backwards compatibility of legacy API's. This may lead to some breaking API changes.
* `Fetchable#fetchFirst()` and `Fetchable#fetchOne()` now return an `Optional<T>`. As an `Optional` cannot hold a `null` value, the `NULL` result set returns `Optional.empty()`.

### New features

* Added `Fetchable#stream()` which returns a `Stream<T>`. _Make sure that the returned stream is always closed to free up resources, for example using try-with-resources. It does **not** suffice to rely on a terminating operation on the stream for this (i.e. `forEach`, `collect`)._

### Dependency updates

* `cglib` to 3.3.0 for Java 8+ support
* `asm` to 9.0 for Java 8+ support
* DataNucleus 5.2.x for Java 8+ support
  * JDO now uses `org.datanucleus:javax.jdo` instead of `javax.jdo:jdo-api`. Seems more widely used and also got more recently updated.