package com.mysema.query.scala

import com.mysema.query.sql._
import com.mysema.query.types.path._
import java.util.{Arrays, Collections}

@Table("USER")
class QUser(path: String) extends BeanPath[QUser](classOf[QUser], path) with RelationalPath[QUser]{
    val id = createNumber("ID", classOf[Integer]);
    val department = createNumber("DEPARTMENT", classOf[Integer]);
    val superiorId = createNumber("SUPERIOR_ID", classOf[Integer]);
    val idKey = new PrimaryKey[QUser](this, id);
    val departmentKey = new ForeignKey[QDepartment](this, department, "ID");
    val superiorIdKey = new ForeignKey[QUser](this, superiorId,"ID");
    def getColumns() = Arrays.asList(id, department, superiorId);
    def getForeignKeys() = Arrays.asList(departmentKey, superiorIdKey);
    def getInverseForeignKeys() = Collections.emptyList();
    def getPrimaryKey() = idKey
}

@Table("DEPARTMENT")
class QDepartment(path: String) extends BeanPath[QDepartment](classOf[QDepartment], path) with RelationalPath[QDepartment]{
    val id = createNumber("ID", classOf[Integer]);
    val company = createNumber("COMPANY", classOf[Integer]);
    val idKey = new PrimaryKey[QDepartment](this, id);
    val companyKey = new ForeignKey[QCompany](this, company, "ID");
    def getColumns() = Arrays.asList(id, company)
    def getForeignKeys() = Arrays.asList(companyKey);
    def getInverseForeignKeys() = Collections.emptyList();
    def getPrimaryKey() = idKey;
}

@Table("COMPANY")
class QCompany(path: String) extends BeanPath[QCompany](classOf[QCompany], path) with RelationalPath[QCompany]{
    val id = createNumber("ID", classOf[Integer]);
    val idKey = new PrimaryKey[QCompany](this, id);
    def getColumns() = Arrays.asList(id);
    def getForeignKeys() = Collections.emptyList();
    def getInverseForeignKeys() = Collections.emptyList();    
    def getPrimaryKey() = idKey;
}

//class Category extends Record[Category] {
//  val id = field(Category.id)
//  val name = field(Category.name)
//  val books = oneToMany(Book.category)    // allows navigating between associations transparently
//}

@Table("CATEGORY")
class QCategory(path: String) extends BeanPath[QCategory](classOf[QCategory], path) with RelationalPath[QCategory] {
    val id = createNumber("ID", classOf[Integer]);
    val name = createString("NAME");
    val idKey = new PrimaryKey[QCategory](this, id);
    val _categoryKey = new ForeignKey[QBook](this, id, "category");
    def getColumns() = Arrays.asList(id, name);
    def getForeignKeys() = Collections.emptyList();
    def getInverseForeignKeys() = Collections.emptyList();    
    def getPrimaryKey() = idKey;
}

//class Book extends Record[Book] {
//  val id = field(Book.id)
//  val title = field(Book.title)
//  val category = manyToOne(Book.category)
//}

@Table("BOOK")
class QBook(path: String) extends BeanPath[QBook](classOf[QBook], path) with RelationalPath[QBook] {
    val id = createNumber("ID",classOf[Integer]);
    val title = createString("TITLE")
    val category = createNumber("CATEGORY", classOf[Integer])
    val idKey = new PrimaryKey[QBook](this, id);
    val categoryKey = new ForeignKey[QCategory](this, category, "ID");
    def getColumns() = Arrays.asList(id, title, category);
    def getForeignKeys() = Arrays.asList(categoryKey)
    def getInverseForeignKeys() = Collections.emptyList();    
    def getPrimaryKey() = idKey;
}