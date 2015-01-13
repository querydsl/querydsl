package test;

import scala.reflect.BeanProperty;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;
import com.querydsl.sql._;

object Employee extends QEmployee("employee"){
  override def as(variable: String) = new QEmployee(variable)
  
}

class Employee {

  var firstname: String = _;

  var id: Integer = _;

  var lastname: String = _;

  var superiorId: Integer = _;

}

