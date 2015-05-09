package test

object Survey extends QSurvey("survey"){
  override def as(variable: String) = new QSurvey(variable)

}

class Survey {

 var id: Integer = _

 var name: String = _

}

