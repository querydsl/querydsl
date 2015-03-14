package test

import com.mysema.query.types._
import com.mysema.query.scala._

import com.mysema.query.types.PathMetadataFactory._

import com.mysema.query.scala.sql.RelationalPathImpl

import com.mysema.query.sql._

object QEmployee extends QEmployee("employee"){
  override def as(variable: String) = new QEmployee(variable)

}

class QEmployee(md: PathMetadata[_]) extends RelationalPathImpl[Employee](md, "PUBLIC", "EMPLOYEE") {
  def this(variable: String) = this(forVariable(variable))

  def this(parent: Path[_], variable: String) = this(forProperty(parent, variable))

  val firstname = createString("firstname")

  val id = createNumber[Integer]("id")

  val lastname = createString("lastname")

  val superiorId = createNumber[Integer]("superiorId")

  val sysIdx55: PrimaryKey[Employee] = createPrimaryKey(id)

  val superiorFk: ForeignKey[Employee] = createForeignKey(superiorId, "ID")

  val _superiorFk: ForeignKey[Employee] = createInvForeignKey(id, "SUPERIOR_ID")

  addMetadata(firstname, ColumnMetadata.named("FIRSTNAME"))
  addMetadata(id, ColumnMetadata.named("ID"))
  addMetadata(lastname, ColumnMetadata.named("LASTNAME"))
  addMetadata(superiorId, ColumnMetadata.named("SUPERIOR_ID"))

}

