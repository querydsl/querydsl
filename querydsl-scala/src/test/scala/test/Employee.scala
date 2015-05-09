package test

object Employee extends QEmployee("employee") {
  override def as(variable: String) = new QEmployee(variable)
  
}

class Employee {

  var firstname: String = _

  var id: Integer = _

  var lastname: String = _

  var superiorId: Integer = _

}

