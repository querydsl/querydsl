package test;

import scala.reflect.BeanProperty;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;
import com.querydsl.sql._;

object Survey extends QSurvey("survey"){
  override def as(variable: String) = new QSurvey(variable)
  
}

class Survey {

 var id: Integer = _;

 var name: String = _;

}

