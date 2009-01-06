/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql;

import static com.mysema.query.grammar.Grammar.add;
import static com.mysema.query.grammar.Grammar.div;
import static com.mysema.query.grammar.Grammar.mult;
import static com.mysema.query.grammar.Grammar.not;
import static com.mysema.query.grammar.Grammar.sub;
import static com.mysema.query.grammar.HqlGrammar.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.mysema.query.grammar.HqlOps;
import com.mysema.query.grammar.HqlQueryBase;
import com.mysema.query.grammar.HqlSerializer;
import com.mysema.query.grammar.types.Constructor;
import com.mysema.query.grammar.types.Custom;
import com.mysema.query.grammar.types.Expr;


/**
 * FeaturesTest provides.
 * 
 * @author tiwe
 * @version $Id$
 */
public class FeaturesTest extends HqlQueryBase<FeaturesTest>{
    
    public FeaturesTest(){super(new HqlOps());}
    
 // AuditLog
    QAuditLog log = new QAuditLog("log");
   
   // QCat
    QCat cat = new QCat("cat");
    QCat cat1 = new QCat("cat1");
    QCat cat2 = new QCat("cat2");
    QCat cat3 = new QCat("cat3");
    QCat cat4 = new QCat("cat4");
    QCat cat5 = new QCat("cat5");
   
    QCat kitten = new QCat("kitten");
    QCat kitten2 = new QCat("kitten2");
    QCat child = new QCat("child");
    QCat mate = new QCat("mate"); 
   
   // QCatalog
    QCatalog catalog = new QCatalog("catalog");
   
   // QCompany
    QCompany company = new QCompany("company");
    QCompany company1 = new QCompany("company1");
    QCompany company2 = new QCompany("company2");
    QCompany company3 = new QCompany("company3");
    QCompany company4 = new QCompany("company4");
    QCompany company5 = new QCompany("company5");
   
   // Customer
    QCustomer cust = new QCustomer("cust");
   
   // QDocument
    QDocument doc = new QDocument("doc");
   
   // DomesticQCat
    QDomesticCat domesticCat = new QDomesticCat("domesticCat");
   
   // QItem
    QItem item = new QItem("item");
   
   // Order
    QOrder order = new QOrder("order");
   
   // Payment
    QPayment payment = new QPayment("payment");
   
   // Price
    QPrice price = new QPrice("price");
   
   // Product
    QProduct product= new QProduct("product");
   
   // User
    QUser user = new QUser("user");
    QUser user1 = new QUser("user1");
    QUser user2 = new QUser("user2");
    QUser user3 = new QUser("user3");
    QUser user4 = new QUser("user4");
    QUser user5 = new QUser("user5");
    
    private HqlSerializer visitor = new HqlSerializer(new HqlOps());
    
    @Test
    public void tstDomainConstruction(){
        QInheritatedProperties i = new QInheritatedProperties("i");
        assertNotNull(i.superclassProperty);
        assertNotNull(i.classProperty);
        
        QAccount a = new QAccount("a");
        assertNotNull(a.embeddedData.someData);
    }
    
    @Test
    public void testBasicStructure(){
        assertNull(cat.getMetadata().getParent());
        assertEquals(cat, cat.alive.getMetadata().getParent());
        assertEquals("cat", cat.getMetadata().getExpression().toString());        
    }
    
    @Test
    public void testArgumentHandling(){
        // Kitty is reused, so it should be used via one named parameter
        toString("cat.name = :a1 or cust.name.firstName = :a2 or kitten.name = :a1",
            cat.name.eq("Kitty")
            .or(cust.name.firstName.eq("Hans"))
            .or(kitten.name.eq("Kitty")));
    }
    
    @Test
    public void testArithmeticOperationsInFunctionalWay(){
        toString("cat.bodyWeight + :a1",add(cat.bodyWeight,10));
        toString("cat.bodyWeight - :a1",sub(cat.bodyWeight,10));
        toString("cat.bodyWeight * :a1",mult(cat.bodyWeight,10));
        toString("cat.bodyWeight / :a1",div(cat.bodyWeight,10));
        
        toString("cat.bodyWeight + :a1 < :a1",add(cat.bodyWeight,10).lt(10));
        toString("cat.bodyWeight - :a1 < :a1",sub(cat.bodyWeight,10).lt(10));
        toString("cat.bodyWeight * :a1 < :a1",mult(cat.bodyWeight,10).lt(10));
        toString("cat.bodyWeight / :a1 < :a1",div(cat.bodyWeight,10).lt(10));
        
        toString("(cat.bodyWeight + :a1) * :a2", mult(add(cat.bodyWeight,10),20));
        toString("(cat.bodyWeight - :a1) * :a2", mult(sub(cat.bodyWeight,10),20));        
        toString("cat.bodyWeight * :a1 + :a2", add(mult(cat.bodyWeight,10),20));
        toString("cat.bodyWeight * :a1 - :a2", sub(mult(cat.bodyWeight,10),20));
        
        QCat c1 = new QCat("c1");
        QCat c2 = new QCat("c2");
        QCat c3 = new QCat("c3");
        toString("c1.id + c2.id * c3.id", add(c1.id, mult(c2.id,c3.id)));
        toString("c1.id * (c2.id + c3.id)", mult(c1.id, add(c2.id,c3.id)));
        toString("(c1.id + c2.id) * c3.id", mult(add(c1.id,c2.id),c3.id));
    }
    
    @Test
    public void testBasicOperations(){
        toString("cat.bodyWeight = kitten.bodyWeight", cat.bodyWeight.eq(kitten.bodyWeight));
        toString("cat.bodyWeight != kitten.bodyWeight",cat.bodyWeight.ne(kitten.bodyWeight));
        
        toString("cat.bodyWeight + kitten.bodyWeight = kitten.bodyWeight", 
            add(cat.bodyWeight,kitten.bodyWeight).eq(kitten.bodyWeight));
    }    
    
    @Test
    public void testBinaryComparisonOperations(){
        // binary comparison operators =, >=, <=, <>, !=, like        
        toString("cat.bodyWeight = kitten.bodyWeight",cat.bodyWeight.eq(kitten.bodyWeight));
        toString("cat.bodyWeight >= kitten.bodyWeight",cat.bodyWeight.goe(kitten.bodyWeight)); 
        toString("cat.bodyWeight > kitten.bodyWeight",cat.bodyWeight.gt(kitten.bodyWeight)); 
        toString("cat.bodyWeight <= kitten.bodyWeight",cat.bodyWeight.loe(kitten.bodyWeight));
        toString("cat.bodyWeight < kitten.bodyWeight",cat.bodyWeight.lt(kitten.bodyWeight)); 
        toString("cat.bodyWeight != kitten.bodyWeight",cat.bodyWeight.ne(kitten.bodyWeight));
        toString("cat.name like :a1", cat.name.like("Kitty"));        
    }
    
    @Test
    public void testBooleanOpeations(){
        toString("cust is null or cat is null", cust.isnull().or(cat.isnull()));
        toString("cust is null and cat is null", cust.isnull().and(cat.isnull()));
        toString("not (cust is null)", not(cust.isnull()));
        cat.name.eq(cust.name.firstName).and(cat.bodyWeight.eq(kitten.bodyWeight));
        cat.name.eq(cust.name.firstName).or(cat.bodyWeight.eq(kitten.bodyWeight));
    }
    
    /**
     * The Class MyCustomExpr.
     */
    public class MyCustomExpr extends Custom.String{
        private Expr<?>[] args;
        public MyCustomExpr(Expr<?>... args) {
            this.args = args;
        }
        public Expr<?>[] getArgs() {return args;}
        public java.lang.String getPattern() {return "myCustom(%s,%s)";}        
    }
    
    @Test
    public void testCustomExpressions(){
        toString("myCustom(cust,cat)", new MyCustomExpr(cust, cat));
    }
    
    @Test
    public void testCastOperations(){
        // cast(... as ...), where the second argument is the name of a Hibernate type, and extract(... from ...) if ANSI cast() and extract() is supported by the underlying database
    }
    
    @Test
    public void testCollectionOperations(){
        // HQL functions that take collection-valued path expressions: size(), minelement(), maxelement(), minindex(), maxindex(), along with the special elements() and indices functions which may be quantified using some, all, exists, any, in.
        cat.kittens.size();
        minelement(cat.kittens); 
        maxelement(cat.kittens); 
        minindex(cat.kittens); 
        maxindex(cat.kittens);
        toString("cat.kittens[0]",cat.kittens(0));
        toString("cat.kittens[0]",cat.kittens.get(0));
        
//        some, all, exists, any, in.
    }
    
    @Test
    public void testConstructors(){
        Constructor<com.mysema.query.hql.HqlDomain.Cat> c = new Constructor<com.mysema.query.hql.HqlDomain.Cat>(com.mysema.query.hql.HqlDomain.Cat.class, cat.name);
        toString("new "+com.mysema.query.hql.HqlDomain.Cat.class.getName()+"(cat.name)", c);
        toString("new "+getClass().getName()+"$BookmarkDTO()", new _BookmarkDTO());
        toString("new "+getClass().getName()+"$BookmarkDTO(cat.name)", new _BookmarkDTO(cat.name));
    }
    
    @Test
    public void testDateOperations(){
        // current_date(), current_time(), current_timestamp()
        toString("current_date()", current_date());
        toString("current_time()", current_time());
        toString("current_timestamp()", current_timestamp());
        // second(...), minute(...), hour(...), day(...), month(...), year(...),
        second(catalog.effectiveDate);
        minute(catalog.effectiveDate);
        hour(catalog.effectiveDate);
        day(catalog.effectiveDate);
        month(catalog.effectiveDate);
        year(catalog.effectiveDate);
    }
        
    @Test
    public void testEJBQL3Functions(){
        // Any function or operator defined by EJB-QL 3.0: substring(), trim(), lower(), upper(), length(), locate(), abs(), sqrt(), bit_length(), mod()    
//        substring(), 
//        trim(), 
//        lower(), 
//        upper(), 
//        length(), 
//        locate(), 
//        abs(), 
//        sqrt(), 
//        bit_length(), 
//        mod()    
        toString("trim(cat.name)",cat.name.trim());
        toString("lower(cat.name)",cat.name.lower());
        toString("upper(cat.name)",cat.name.upper());
//        cat.name.length();
    }
    
    @Test
    public void testEqualsAndNotEqualsForAllExpressions(){
        toString("cat.name = cust.name.firstName",cat.name.eq(cust.name.firstName));        
        toString("cat.name != cust.name.firstName",cat.name.ne(cust.name.firstName));
    }
    
    @Test
    public void testGrammarConstructs(){
        add(cat.bodyWeight,kitten.bodyWeight);        
    }
    
    @Test
    public void testGroupingOperationsAndNullChecks(){
        // in, not in, between, is null, is not null, is empty, is not empty, member of and not member of
//        in, 
//        not in, 
//        between, 
//        is null, 
//        is not null, 
//        is empty, 
//        is not empty, 
//        member of
//        not member of 
        kitten.in(cat.kittens);
        not(kitten.in(cat.kittens));
        kitten.bodyWeight.between(10, 20);
        kitten.bodyWeight.isnull();
        kitten.bodyWeight.isnotnull();
        isempty(cat.kittens);
        isnotempty(cat.kittens);
    }
    
    @Test
    public void testHQLIndexOperations(){
        // the HQL index() function, that applies to aliases of a joined indexed collection    
    }
    
    @Test
    public void testIsNullAndIsNotNullInFunctionalWay(){
        toString("cat.bodyWeight is null",cat.bodyWeight.isnull());
    }

    @Test
    public void testLogicalOperations(){
        // logical operations and, or, not        
        toString("cat = kitten or kitten = cat",cat.eq(kitten).or(kitten.eq(cat)));
        toString("cat = kitten and kitten = cat", cat.eq(kitten).and(kitten.eq(cat)));
        toString("cat is null and (kitten is null or kitten.bodyWeight > :a1)", cat.isnull().and(kitten.isnull().or(kitten.bodyWeight.gt(10))));
    }
    
    @Test
    public void testMathematicalOperations(){
        // mathematical operators +, -, *, /    
        add(cat.bodyWeight, kitten.bodyWeight);
        sub(cat.bodyWeight, kitten.bodyWeight);
        mult(cat.bodyWeight, kitten.bodyWeight);
        div(cat.bodyWeight, kitten.bodyWeight);
    }
    
    // Parentheses ( ), indicating grouping

    @Test
    public void testOrderExpressionInFunctionalWay(){
        cat.bodyWeight.asc();
        add(cat.bodyWeight,kitten.bodyWeight).asc();
    }
    
    // "Simple" case, case ... when ... then ... else ... end, and "searched" case, case when ... then ... else ... end
    
    @Test
    public void testSimpleAliasForNonEntityPaths(){
        toString("cat.bodyWeight as catbodyWeight", cat.bodyWeight.as("catbodyWeight"));
        toString("count(*) as numPosts", count().as("numPosts"));
        toString("cat.bodyWeight + kitten.bodyWeight as abc", add(cat.bodyWeight,kitten.bodyWeight).as("abc"));
    }
    
    @Test
    public void testSQLScalarOperations(){
        // Any database-supported SQL scalar function like sign(), trunc(), rtrim(), sin()    
    }
    
    @Test
    public void testStringConcatenations(){
        // string concatenation ...||... or concat(...,...)        
        toString("cat.name || kitten.name", cat.name.concat(kitten.name));
    }
    
    // coalesce() and nullif()

    @Test
    public void testStringConversionOperations(){
        // str() for converting numeric or temporal values to a readable string
    }
    
    @Test
    public void testStringOperationsInFunctionalWay(){
        toString("cat.name || cust.name.firstName", cat.name.concat(cust.name.firstName));
        toString("cat.name like :a1",cat.name.like("A%"));
        toString("lower(cat.name)",cat.name.lower());
    }
        
    @Test
    public void testToString(){
        toString("cat", cat);
        toString("cat.alive", cat.alive);
        toString("cat.bodyWeight",cat.bodyWeight);
        toString("cat.name",cat.name);
        
        toString("cust.name",cust.name);                     
        toString("cust.name.firstName = :a1", cust.name.firstName.eq("Martin"));
        
        toString("cat.kittens as kitten", cat.kittens.as(kitten));
        
        toString("cat.bodyWeight + :a1", add(cat.bodyWeight,10));
        toString("cat.bodyWeight - :a1", sub(cat.bodyWeight,10));
        toString("cat.bodyWeight * :a1", mult(cat.bodyWeight,10));
        toString("cat.bodyWeight / :a1", div(cat.bodyWeight,10));        
        
        toString("cat.bodyWeight as bw", cat.bodyWeight.as("bw"));
        
        toString("kitten in elements(cat.kittens)", kitten.in(cat.kittens)); 
        
        toString("distinct cat.bodyWeight", distinct(cat.bodyWeight));
        
        toString("count(*)", count());
        toString("count(distinct cat.bodyWeight)", count(distinct(cat.bodyWeight)));
        toString("count(cat)", count(cat));        
    }
       
    private void toString(String expected, Expr<?> expr) {
        assertEquals(expected, visitor.handle(expr).toString());
//        visitor.clear();
        visitor = new HqlSerializer(new HqlOps());
    }
    
    /**
     * The Class _BookmarkDTO.
     */
    public static final class _BookmarkDTO extends Constructor<BookmarkDTO>{
        public _BookmarkDTO(){
            super(BookmarkDTO.class);
        }
        public _BookmarkDTO(Expr<java.lang.String> address){
            super(BookmarkDTO.class,address);
        }
    }
    
    /**
     * The Class BookmarkDTO.
     */
    public static final class BookmarkDTO{
        
    }
    
}
