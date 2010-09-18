Naming options :

Caps start : (domainType.firstName Like "An%") And (domainType.firstName Like "Be%")

Full caps  : (domainType.firstName LIKE "An%") AND (domainType.firstName LIKE "Be%")

_          : (domainType.firstName _like "An%") _and (domainType.firstName _like "Be%")

_          : (domainType.firstName like_ "An%") and_ (domainType.firstName like_ "Be%")

$          : (domainType.firstName $like "An%") $and (domainType.firstName $like "Be%")


TODO :

* Resolve paths in operation handling as well

* Hibernate test