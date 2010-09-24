package com.mysema.query.scala.sql

import com.mysema.query.sql._
import org.junit.Test
import org.junit.Assert._

class QuerySyntaxTest {

  @Test
  def Path_Creation() {
    val c1 = new QCategory("c");
    val c2 = QCategory as "c";
    assertEquals(c1, c2);
  }

  @Test
  def Query_Syntax() {
    //  select()
    //      .from(Category as "c" join (Book as "b"), Category as "c1")
    //      .where("c1.name" like "a%")
    //      .addOrder(asc("c.name"))
    //      .list

    val c = new QCategory("c");
    val c1 = new QCategory("c1");
    val b = new QBook("b");
    query.from(c).innerJoin(b).from(c1)
      .where(c1.name like "a%")
      .orderBy(c.name asc)
      .list(c)

    //  select(count("b.id"), "c.name").from(Category as "c" join (Book as "b")).list

    query.from(c).innerJoin(b)
      .list(b.id count)
  }

  @Test
  def Key_Usage() {
    val user = new QUser("user");
    val user2 = new QUser("user2");
    val department = new QDepartment("department");
    val company = new QCompany("company");

    // superiorId -> id
    query.from(user).innerJoin(user.superiorIdKey, user2);

    // department -> id / company -> id
    query.from(user)
      .innerJoin(user.departmentKey, department)
      .innerJoin(department.companyKey, company);
  }

  def query() = new SQLSubQuery();

}