package test;

import scala.reflect.BeanProperty;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;
import com.mysema.query.sql._;

/**
 * Employee is a Querydsl bean type
 */
//@Schema("PUBLIC") 
//@Table("EMPLOYEE")
class Employee {

  var firstname: String = _;

  var id: Integer = _;

  var lastname: String = _;

  var superiorId: Integer = _;

}

