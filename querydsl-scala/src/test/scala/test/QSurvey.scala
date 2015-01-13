package test;

import com.querydsl.core.types._;
import com.querydsl.scala._;

import com.querydsl.core.types.PathMetadataFactory._;
import com.querydsl.scala.sql.RelationalPathImpl;

import com.querydsl.sql._;

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

