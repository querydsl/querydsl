REFACTORING

* move types.operation.* and types.expr.* into types

* split queries into two implementation categories :

  - real queries (Query + Projectable)
  - detached queries (Query + Detachable)

IDEAS

 * all fields : 
 
  from(customer).list(customer.allFields()) // SQL wildcard projection
  
  
  
  
