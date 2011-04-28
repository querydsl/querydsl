package test;

import com.mysema.query.types._;
import com.mysema.query.scala._;

import com.mysema.query.types.PathMetadataFactory._;
import com.mysema.query.sql.Table;
import com.mysema.query.sql.Schema;

import com.mysema.query.scala.sql.RelationalPathImpl;

import com.mysema.query.sql._;

object QSurvey {
  def as(variable: String) = new QSurvey(variable)
}

@Table("SURVEY")
@Schema("PUBLIC")
class QSurvey(cl: Class[_ <: Survey], md: PathMetadata[_]) extends RelationalPathImpl[Survey](cl, md) {
  def this(variable: String) = this(classOf[Survey], forVariable(variable));

  def this(parent: Path[_], variable: String) = this(classOf[Survey], forProperty(parent, variable));

  val id = createNumber("ID", classOf[Integer]);

  val name = createString("NAME");

  val sysIdx54: PrimaryKey[Survey] = createPrimaryKey(id, name);

}

