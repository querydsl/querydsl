package com.mysema.query.grammar.hql;

import static com.mysema.query.grammar.Grammar.*;
import static com.mysema.query.grammar.HqlGrammar.*;
import static com.mysema.query.grammar.hql.domain.Domain.*;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.query.grammar.HqlQueryBase;
import com.mysema.query.grammar.HqlSerializer;
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
        add(cat.bodyWeight,10);
        sub(cat.bodyWeight,10);
        mult(cat.bodyWeight,10);
        div(cat.bodyWeight,10);
        
        add(cat.bodyWeight,10).lt(10);
        sub(cat.bodyWeight,10).lt(10);
        mult(cat.bodyWeight,10).lt(10);
        div(cat.bodyWeight,10).lt(10);
    }
    
    @Test
    public void testBasicOperations(){
        cat.bodyWeight.eq(kitten.bodyWeight);
        cat.bodyWeight.ne(kitten.bodyWeight);
        
        add(cat.bodyWeight,kitten.bodyWeight).eq(kitten.bodyWeight);
    }
    
    @Test
    public void testBinaryComparisonOperations(){
        // binary comparison operators =, >=, <=, <>, !=, like        
        cat.bodyWeight.eq(kitten.bodyWeight);
        cat.bodyWeight.goe(kitten.bodyWeight); 
        cat.bodyWeight.gt(kitten.bodyWeight); 
        cat.bodyWeight.loe(kitten.bodyWeight);
        cat.bodyWeight.lt(kitten.bodyWeight); 
        cat.bodyWeight.ne(kitten.bodyWeight);
        cat.name.like("Kitty");        
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
        cat.name.trim();
        cat.name.lower();
        cat.name.upper();
//        cat.name.length();
    }
    
    @Test
    public void testEqualsAndNotEqualsForAllExpressions(){
        cat.name.eq(cust.name().firstName);        
        cat.name.ne(cust.name().firstName);
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
        cat.bodyWeight.isnull();
    }
    
    // Parentheses ( ), indicating grouping

    @Test
    public void testLogicalOperations(){
        // logical operations and, or, not        
        cat.eq(kitten).or(kitten.eq(cat));
        cat.eq(kitten).and(kitten.eq(cat));
    }
    
    // "Simple" case, case ... when ... then ... else ... end, and "searched" case, case when ... then ... else ... end

    @Test
    public void testMathematicalOperations(){
        // mathematical operators +, -, *, /    
        add(cat.bodyWeight, kitten.bodyWeight);
        sub(cat.bodyWeight, kitten.bodyWeight);
        mult(cat.bodyWeight, kitten.bodyWeight);
        div(cat.bodyWeight, kitten.bodyWeight);
    }
    
    @Test
    public void testOrderExpressionInFunctionalWay(){
        cat.bodyWeight.asc();
        add(cat.bodyWeight,kitten.bodyWeight).asc();
    }
    
    @Test
    public void testPublicStaticFinalConstants(){
        // Java public static final constants eg.Color.TABBY    
    }
    
    @Test
    public void testSimpleAliasForNonEntityPaths(){
        cat.bodyWeight.as("catbodyWeight");
        count().as("numPosts");
        add(cat.bodyWeight,kitten.bodyWeight).as("abc");
    }
    
    // coalesce() and nullif()

    @Test
    public void testSQLScalarOperations(){
        // Any database-supported SQL scalar function like sign(), trunc(), rtrim(), sin()    
    }
    
    @Test
    public void testStringConcatenations(){
        // string concatenation ...||... or concat(...,...)        
        toString("cat.name || kitten.name", concat(cat.name, kitten.name));
    }
    
    @Test
    public void testStringConversionOperations(){
        // str() for converting numeric or temporal values to a readable string
    }
       
    @Test
    public void testStringOperationsInFunctionalWay(){
        concat(cat.name,cust.name().firstName);
        cat.name.like("A%");
        cat.name.lower();
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
    
    // JDBC-style positional parameters ?
    
    // named parameters :name, :start_date, :x1
    
    // SQL literals 'foo', 69, 6.66E+2, '1970-01-01 10:00:01.0'
     
    private void toString(String expected, Expr<?> expr) {
        assertEquals(expected, visitor.handle(expr).toString());
//        visitor.clear();
        visitor = new HqlSerializer();
    }
}
