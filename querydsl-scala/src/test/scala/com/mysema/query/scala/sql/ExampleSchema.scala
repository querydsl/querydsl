package com.mysema.query.scala.sql

import com.mysema.query.sql._
import com.mysema.query.types.path._
import java.util.{ Arrays, Collections }

object QUser {
  def as(path: String) = new QUser(path)
}

class QUser(path: String) extends RelationalPathBase[QUser](classOf[QUser], path, null, "USER") {
  val id = createNumber("ID", classOf[Integer])
  val department = createNumber("DEPARTMENT", classOf[Integer])
  val superiorId = createNumber("SUPERIOR_ID", classOf[Integer])
  val idKey = createPrimaryKey(id)
  val departmentKey: ForeignKey[QDepartment] = createForeignKey(department, "ID")
  val superiorIdKey: ForeignKey[QUser] = createForeignKey(superiorId, "ID")
}

object QDepartment {
  def as(path: String) = new QDepartment(path)
}

class QDepartment(path: String) extends RelationalPathBase[QDepartment](classOf[QDepartment], path, null, "DEPARTMENT") {
  val id = createNumber("ID", classOf[Integer])
  val company = createNumber("COMPANY", classOf[Integer])
  val idKey = createPrimaryKey(id)
  val companyKey: ForeignKey[QCompany] = createForeignKey(company, "ID")
}

object QCompany {
  def as(path: String) = new QCompany(path)
}

class QCompany(path: String) extends RelationalPathBase[QCompany](classOf[QCompany], path, null, "COMPANY") {
  val id = createNumber("ID", classOf[Integer])
  val idKey = createPrimaryKey(id)
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
  val id = createNumber("ID", classOf[Integer])
  val name = createString("NAME")
  val idKey = createPrimaryKey(id)
  val _categoryKey: ForeignKey[QBook] = createInvForeignKey(id, "category")
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
  val id = createNumber("ID", classOf[Integer])
  val title = createString("TITLE")
  val category = createNumber("CATEGORY", classOf[Integer])
  val idKey = createPrimaryKey(id)
  val categoryKey: ForeignKey[QCategory] = createForeignKey(category, "ID")
}