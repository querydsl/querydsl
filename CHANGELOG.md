# Changelog

### 5.0.0

#### Breaking changes

* Java 8 minimal requirement. If you still rely on Java <7, please use the latest 4.x.x version.
* Removed bridge method that were in place for backwards compatibility of legacy API's. This may lead to some breaking API changes.

#### Dependency updates

* `cglib` to 3.3.0 for Java 8+ support
* DataNucleus 5.2.x for Java 8+ support
  * JDO now uses `org.datanucleus:javax.jdo` instead of `javax.jdo:jdo-api`

#### Plans

- [x] Require Java 8
- [ ] Optimize code for Java 8 (use new API's)
- [ ] Support Java 8 date/time for `querydsl-sql`
- [ ] Drop Guava dependency #2324 
   - [ ] Support Java 8 Optional in favour of Guava's Optional
- [ ] Assume Hibernate 5.3+ by default in Hibernate integration
- [ ] Switch from`com.vividsolutions` to `org.locationtech` for JTS #2404. `hibernate-spatial` doesn't support the old artifact for a while now. If you still rely on `com.vividsolutions`, please use the lastest 4.x.x version.
- [ ] Migrate off from jsr305 to https://github.com/JetBrains/java-annotations #2479
- [ ] Consider breaking out some integrations (Hibernate, Datanucleus, OpenJPA) and extensions (Alias.*) to separate modules (if it helps with cleaning up dependencies, and in particular, not providing a false sense of support).