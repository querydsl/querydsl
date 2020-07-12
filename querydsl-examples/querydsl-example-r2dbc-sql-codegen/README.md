## Querydsl Customer DAO

Querydsl Customer DAO is an example project that demonstrates some best practices on how to use Querydsl R2DBC on the DAO level in Spring projects.

Compared to direct JDBC usage Querydsl R2DBC is typesafe, closer to SQL and abstracts over SQL dialect specific differences.

This example project presents a version of Querydsl R2DBC usage where no generated bean types are used, but external DTO types are populated from queries.
