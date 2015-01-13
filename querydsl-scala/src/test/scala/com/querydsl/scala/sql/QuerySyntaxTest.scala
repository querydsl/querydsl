package com.querydsl.scala.sql

import com.querydsl.sql._

import org.junit.Test
import org.junit.Assert._

class QuerySyntaxTest {

  val b = new QBook("b")
  val c = new QCategory("c")
  val c1 = new QCategory("c1")
  val company = new QCompany("company")
  val department = new QDepartment("department")
  val user = new QUser("user")
  val user2 = new QUser("user2")

  @Test
  def Path_Creation {
    assertEquals(new QCategory("c"), QCategory as "c")
  }

  @Test
  def Query_Syntax {
    query from(c) innerJoin(b) from(c1) where (c1.name like "a%") orderBy(c.name asc) //list(c)
    query from(c) innerJoin(b) list(b.id count)

    query from (c) list (c.id, c.name)
  }

  @Test
  def Key_Usage {
    // superiorId -> id
    query from(user) innerJoin(user.superiorIdKey, user2)
  }

  @Test
  def Key_Usage2 {
    // department -> id / company -> id
    query from(user) innerJoin(user.departmentKey, department) innerJoin(department.companyKey, company)
  }

  def query() = new SQLSubQuery()

}