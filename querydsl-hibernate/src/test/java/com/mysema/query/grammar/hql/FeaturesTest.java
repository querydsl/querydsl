package com.mysema.query.grammar.hql;

import static com.mysema.query.grammar.Grammar.*;
import static com.mysema.query.grammar.HqlGrammar.*;
import static com.mysema.query.grammar.hql.domain.Domain.*;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.query.grammar.HqlQueryBase;
import com.mysema.query.grammar.HqlSerializer;
import com.mysema.query.grammar.HqlGrammar.Constructor;
import com.mysema.query.grammar.Types.Expr;

/**
 * FeaturesTest provides
 *
 * @author tiwe
 * @version $Id$
 */
public class FeaturesTest extends HqlQueryBase<FeaturesTest>{
    
    private HqlSerializer visitor = new HqlSerializer();
    
    @Test
    public void testArgumentHandling(){
        // Kitty is reused, so it should be used via one named parameter
        toString("((cat.name = :a1 or cust.name.firstName = :a2) or kitten.name = :a1)",
            cat.name.eq("Kitty")
            .or(cust.name().firstName.eq("Hans"))
            .or(kitten.name.eq("Kitty")));
    }
    
    @Test
    public void testArithmeticOperationsInFunctionalWay(){
        toString("(cat.bodyWeight + :a1)",add(cat.bodyWeight,10));
        toString("(cat.bodyWeight - :a1)",sub(cat.bodyWeight,10));
        toString("(cat.bodyWeight * :a1)",mult(cat.bodyWeight,10));
        toString("(cat.bodyWeight / :a1)",div(cat.bodyWeight,10));
        
        toString("(cat.bodyWeight + :a1) < :a1",add(cat.bodyWeight,10).lt(10));
        toString("(cat.bodyWeight - :a1) < :a1",sub(cat.bodyWeight,10).lt(10));
        toString("(cat.bodyWeight * :a1) < :a1",mult(cat.bodyWeight,10).lt(10));
        toString("(cat.bodyWeight / :a1) < :a1",div(cat.bodyWeight,10).lt(10));
    }
    
    @Test
    public void testBasicOperations(){
        toString("cat.bodyWeight = kitten.bodyWeight", cat.bodyWeight.eq(kitten.bodyWeight));
        toString("cat.bodyWeight != kitten.bodyWeight",cat.bodyWeight.ne(kitten.bodyWeight));
        
        toString("(cat.bodyWeight + kitten.bodyWeight) = kitten.bodyWeight", 
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
        toString("(cust is null or cat is null)", cust.isnull().or(cat.isnull()));
        toString("(cust is null and cat is null)", cust.isnull().and(cat.isnull()));
        toString("not cust is null", not(cust.isnull()));
    }
    
    @Test
    public void testBooleanOperationsInOOPWay(){
        cat.name.eq(cust.name().firstName).and(cat.bodyWeight.eq(kitten.bodyWeight));
        cat.name.eq(cust.name().firstName).or(cat.bodyWeight.eq(kitten.bodyWeight));
    }
    
    @Test
    public void testCastOperations(){
        // cast(... as ...), where the second argument is the name of a Hibernate type, and extract(... from ...) if ANSI cast() and extract() is supported by the underlying database
    }
    
    @Test
    public void testCollectionOperations(){
        // HQL functions that take collection-valued path expressions: size(), minelement(), maxelement(), minindex(), maxindex(), along with the special elements() and indices functions which may be quantified using some, all, exists, any, in.
//        size(cat.kittens());
//        minelement(cat.kittens()); 
//        maxelement(cat.kittens()); 
//        minindex(cat.kittens()); 
//        maxindex(cat.kittens());
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testConstructors(){
        Constructor c = new Constructor(com.mysema.query.grammar.hql.domain.Cat.class, cat.name);
        toString("new com.mysema.query.grammar.hql.domain.Cat(cat.name)", c);
    }
    
    @Test
    public void testConstructors2(){
        toString("new "+getClass().getName()+"$BookmarkDTO()", new _BookmarkDTO());
        toString("new "+getClass().getName()+"$BookmarkDTO(cat.name)", new _BookmarkDTO(cat.name));   
    }
    
    @Test
    public void testDateOperations(){
        // current_date(), current_time(), current_timestamp()
        toString("current_date()", current_date());
        toString("current_time()", current_time());
        toString("current_timestamp()", current_timestamp());
    }
    
    @Test
    public void testDateOperations2(){
        // second(...), minute(...), hour(...), day(...), month(...), year(...),
//        second(catalog.effectiveDate);
//        minute(catalog.effectiveDate);
//        hour(catalog.effectiveDate);
//        day(catalog.effectiveDate);
//        month(catalog.effectiveDate);
//        year(catalog.effectiveDate);
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
        toString("cat.name = cust.name.firstName",cat.name.eq(cust.name().firstName));        
        toString("cat.name != cust.name.firstName",cat.name.ne(cust.name().firstName));
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
        toString("(cat = kitten or kitten = cat)",cat.eq(kitten).or(kitten.eq(cat)));
        toString("(cat = kitten and kitten = cat)", cat.eq(kitten).and(kitten.eq(cat)));
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
    public void testPublicStaticFinalConstants(){
        // Java public static final constants eg.Color.TABBY    
    }
    
    @Test
    public void testSimpleAliasForNonEntityPaths(){
        toString("cat.bodyWeight as catbodyWeight", cat.bodyWeight.as("catbodyWeight"));
        toString("count(*) as numPosts", count().as("numPosts"));
        toString("(cat.bodyWeight + kitten.bodyWeight) as abc", add(cat.bodyWeight,kitten.bodyWeight).as("abc"));
    }
    
    @Test
    public void testSQLScalarOperations(){
        // Any database-supported SQL scalar function like sign(), trunc(), rtrim(), sin()    
    }
    
    @Test
    public void testStringConcatenations(){
        // string concatenation ...||... or concat(...,...)        
        toString("cat.name || kitten.name", concat(cat.name, kitten.name));
    }
    
    // coalesce() and nullif()

    @Test
    public void testStringConversionOperations(){
        // str() for converting numeric or temporal values to a readable string
    }
    
    @Test
    public void testStringOperationsInFunctionalWay(){
        toString("cat.name || cust.name.firstName", concat(cat.name,cust.name().firstName));
        toString("cat.name like :a1",cat.name.like("A%"));
        toString("lower(cat.name)",cat.name.lower());
    }
    
    @Test
    public void testToString(){
        toString("cat", cat);
        toString("cat.alive", cat.alive);
        toString("cat.bodyWeight",cat.bodyWeight);
        toString("cat.name",cat.name);
        
        toString("cust.name",cust.name());                     
        toString("cust.name.firstName = :a1", cust.name().firstName.eq("Martin"));
        
        toString("cat.kittens as kitten", cat.kittens.as(kitten));
        
        toString("(cat.bodyWeight + :a1)", add(cat.bodyWeight,10));
        toString("(cat.bodyWeight - :a1)", sub(cat.bodyWeight,10));
        toString("(cat.bodyWeight * :a1)", mult(cat.bodyWeight,10));
        toString("(cat.bodyWeight / :a1)", div(cat.bodyWeight,10));        
        
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
        visitor = new HqlSerializer();
    }
    
    public static final class BookmarkDTO{
        
    }
    
    public static final class _BookmarkDTO extends Constructor<BookmarkDTO>{
        public _BookmarkDTO(){
            super(BookmarkDTO.class);
        }
        public _BookmarkDTO(Expr<java.lang.String> address){
            super(BookmarkDTO.class,address);
        }
    }
    
}
