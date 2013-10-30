package test;

import com.mysema.query.types._;
import com.mysema.query.scala._;

import com.mysema.query.types.PathMetadataFactory._;
import com.mysema.query.scala.sql.RelationalPathImpl;

import com.mysema.query.sql._;

object QSurvey extends QSurvey("survey"){
  override def as(variable: String) = new QSurvey(variable)

}

class QSurvey(md: PathMetadata[_]) extends RelationalPathImpl[Survey](md, "PUBLIC", "SURVEY") {
  def this(variable: String) = this(forVariable(variable))

  def this(parent: Path[_], variable: String) = this(forProperty(parent, variable))

  val id = createNumber[Integer]("id")

  val name = createString("name")

  val sysIdx54: PrimaryKey[Survey] = createPrimaryKey(id);

  addMetadata(id, ColumnMetadata.named("ID"))
  addMetadata(name, ColumnMetadata.named("NAME"))

}

