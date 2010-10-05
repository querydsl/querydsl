Naming options :

Caps start : (domainType.firstName Like "An%") And (domainType.firstName Like "Be%")

Full caps  : (domainType.firstName LIKE "An%") AND (domainType.firstName LIKE "Be%")

_          : (domainType.firstName _like "An%") _and (domainType.firstName _like "Be%")

_          : (domainType.firstName like_ "An%") and_ (domainType.firstName like_ "Be%")

$          : (domainType.firstName $like "An%") $and (domainType.firstName $like "Be%")




// query from (person) where (person.age < 5) unique (person.firstName);
               RelationalPath Predicate               Expression
               
* any    -> Expression               
               
* any    -> RelationalPath

* String -> StringFunctions
* Number -> NumberFunctions
...

                