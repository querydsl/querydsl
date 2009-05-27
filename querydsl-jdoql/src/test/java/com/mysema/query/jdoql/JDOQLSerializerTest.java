package com.mysema.query.jdoql;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.query.jdoql.testdomain.Book;
import com.mysema.query.jdoql.testdomain.Department;
import com.mysema.query.jdoql.testdomain.Employee;
import com.mysema.query.jdoql.testdomain.QBook;
import com.mysema.query.jdoql.testdomain.QCompany;
import com.mysema.query.jdoql.testdomain.QDepartment;
import com.mysema.query.jdoql.testdomain.QProduct;
import com.mysema.query.types.expr.Expr;

public class JDOQLSerializerTest {

    private QBook book = QBook.book;

    private QProduct product = QProduct.product;

    private QCompany company = QCompany.company;
    
    private QDepartment department = QDepartment.department;

    @Test
    public void instanceOf() {
        assertEquals(
                "product instanceof com.mysema.query.jdoql.testdomain.Book",
                serialize(product.instanceOf(Book.class)));
    }

    @Test
    public void testHandle() {
        assertEquals("this.name == product.name", serialize(book.name.eq(product.name)));
        assertEquals("this == product", serialize(book.eq(product)));
    }

    @Test
    public void booleanTests() {
        // boolean
        assertEquals("product.name == a1 && product.price <= a2",
                serialize(product.name.eq("Sony Discman").and(product.price.loe(300.00))));
        assertEquals("product.name == a1 || product.price <= a2",
                serialize(product.name.eq("Sony Discman").or(product.price.loe(300.00))));
        assertEquals("!(product.name == a1)", serialize(product.name.eq(
                "Sony MP3 player").not()));
    }

    @Test
    public void collectionTests() {
        Department dep = new Department();
        // collection
        assertEquals("company.departments.contains(a1)",
                serialize(company.departments.contains(dep)));
        assertEquals("company.departments.get(0) == a1",
                serialize(company.departments.get(0).eq(dep)));
        assertEquals("company.departments.isEmpty()",
                serialize(company.departments.isEmpty()));
        assertEquals("!company.departments.isEmpty()",
                serialize(company.departments.isNotEmpty()));
        assertEquals("company.departments.size() == a1",
                serialize(company.departments.size().eq(1)));
    }

    @Test
    public void mapTests() {        
        assertEquals("department.employeesByUserName.containsKey(a1)", 
                serialize(department.employeesByUserName.containsKey("")));
        assertEquals("department.employeesByUserName.containsValue(a1)", 
                serialize(department.employeesByUserName.containsValue(new Employee())));
        
        assertEquals("department.employeesByUserName.isEmpty()",
                serialize(department.employeesByUserName.isEmpty()));
        assertEquals("!department.employeesByUserName.isEmpty()",
                serialize(department.employeesByUserName.isNotEmpty()));
    }

    @Test
    public void numericTests() {
        // numeric
        assertEquals("product.price == a1", serialize(product.price.eq(200.00)));
        assertEquals("product.price != a1", serialize(product.price.ne(100.00)));
        assertEquals("product.price > a1", serialize(product.price.gt(100.00)));
        assertEquals("product.price < a1", serialize(product.price.lt(300.00)));
        assertEquals("product.price >= a1", serialize(product.price.goe(100.00)));
        assertEquals("product.price <= a1", serialize(product.price.loe(300.00)));
        // TODO +
        // TODO -
        // TODO *
        // TODO /
        // TODO %
        // TODO Math.abs
        // TODO Math.sqrt
    }

    @Test
    public void stringTests() {
        // string
        assertEquals("product.name.startsWith(a1)", serialize(product.name.startsWith("Sony Discman")));
        assertEquals("product.name.endsWith(a1)", serialize(product.name.endsWith("Discman")));
        // FIXME assertEquals("like", 1,
        // serialize(product.name.like("Sony %")));
        assertEquals("product.name.toLowerCase() == a1", serialize(product.name.lower().eq("sony discman")));
        assertEquals("product.name.toUpperCase() == a1", serialize(product.name.upper().eq("SONY DISCMAN")));
        assertEquals("product.name.indexOf(a1) == a2", serialize(product.name.indexOf("S").eq(0)));
        // TODO indexOf
        // TODO matches
        assertEquals("product.name.substring(a1,a2) == a3", serialize(product.name.substring(0, 4).eq("Sony")));
        assertEquals("product.name.substring(a1) == a2", serialize(product.name.substring(5).eq("Discman")));
    }

    private String serialize(Expr<?> expr) {
        return new JDOQLSerializer(JDOQLPatterns.DEFAULT, book).handle(expr).toString();
    }

}
