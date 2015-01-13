package com.querydsl.scala.sql

import com.querydsl.sql._
import com.querydsl.core.types.path._
import java.util.{ Arrays, Collections }

object QUser {
  def as(path: String) = new QUser(path)
}

class QUser(path: String) extends RelationalPathBase[QUser](classOf[QUser], path, null, "USER") {
  val id = createNumber("id", classOf[Integer])
  val department = createNumber("department", classOf[Integer])
  val superiorId = createNumber("superiorId", classOf[Integer])
  val idKey = createPrimaryKey(id)
  val departmentKey: ForeignKey[QDepartment] = createForeignKey(department, "ID")
  val superiorIdKey: ForeignKey[QUser] = createForeignKey(superiorId, "ID")

  addMetadata(id, ColumnMetadata.named("ID"))
  addMetadata(department, ColumnMetadata.named("DEPARTMENT"))
  addMetadata(superiorId, ColumnMetadata.named("SUPERIOR_ID"))
}

object QDepartment {
  def as(path: String) = new QDepartment(path)
}

class QDepartment(path: String) extends RelationalPathBase[QDepartment](classOf[QDepartment], path, null, "DEPARTMENT") {
  val id = createNumber("id", classOf[Integer])
  val company = createNumber("company", classOf[Integer])
  val idKey = createPrimaryKey(id)
  val companyKey: ForeignKey[QCompany] = createForeignKey(company, "ID")

  addMetadata(id, ColumnMetadata.named("ID"))
  addMetadata(company, ColumnMetadata.named("COMPANY"))
}

object QCompany {
  def as(path: String) = new QCompany(path)
}

class QCompany(path: String) extends RelationalPathBase[QCompany](classOf[QCompany], path, null, "COMPANY") {
  val id = createNumber("id", classOf[Integer])
  val idKey = createPrimaryKey(id)

  addMetadata(id, ColumnMetadata.named("ID"))
}

//class Category extends Record[Category] {
//  val id = field(Category.id)
//  val name = field(Category.name)
//  val books = oneToMany(Book.category)    // allows navigating between associations transparently
//}

object QCategory {
  def as(path: String) = new QCategory(path)
}

//@Table("CATEGORY")
class QCategory(path: String) extends RelationalPathBase[QCategory](classOf[QCategory], path, null, "CATEGORY") {
  val id = createNumber("id", classOf[Integer])
  val name = createString("name")
  val idKey = createPrimaryKey(id)
  val _categoryKey: ForeignKey[QBook] = createInvForeignKey(id, "category")

  addMetadata(id, ColumnMetadata.named("ID"))
  addMetadata(name, ColumnMetadata.named("NAME"))
}

//class Book extends Record[Book] {
//  val id = field(Book.id)
//  val title = field(Book.title)
//  val category = manyToOne(Book.category)
//}

object QBook {
  def as(path: String) = new QBook(path)
}

class QBook(path: String) extends RelationalPathBase[QBook](classOf[QBook], path, null, "BOOK") {
  val id = createNumber("id", classOf[Integer])
  val title = createString("title")
  val category = createNumber("category", classOf[Integer])
  val idKey = createPrimaryKey(id)
  val categoryKey: ForeignKey[QCategory] = createForeignKey(category, "ID")

  addMetadata(id, ColumnMetadata.named("ID"))
  addMetadata(title, ColumnMetadata.named("TITLE"))
  addMetadata(category, ColumnMetadata.named("CATEGORY"))
}