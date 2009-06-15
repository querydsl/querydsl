package com.mysema.query.jdoql;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.query.jdoql.testdomain.Book;
import com.mysema.query.jdoql.testdomain.QProduct;
import com.mysema.query.types.Grammar;
import com.mysema.query.types.SubQuery;
import com.mysema.query.types.expr.Expr;

public class QuerySerializationTest {
    
    private QProduct product = QProduct.product;
    
    private QProduct other = new QProduct("other");
    
    @Test
    public void selectFromWhereOrder(){
        assertEquals(
            "SELECT UNIQUE this.name " +
            "FROM com.mysema.query.jdoql.testdomain.Product " +
            "WHERE this.name == a1 " +
            "PARAMETERS java.lang.String a1 " +
            "ORDER BY this.name ASC", 
            
            serialize(query().from(product)
              .where(product.name.eq("Test"))
              .orderBy(product.name.asc())
              .uniqueExpr(product.name)));
    }
    
    @Test
    public void selectFromWhereGroupBy(){
        assertEquals(
            "SELECT this.name " +
            "FROM com.mysema.query.jdoql.testdomain.Product " +
            "WHERE this.name.startsWith(a1) || this.name.endsWith(a2) " +
            "PARAMETERS java.lang.String a1, java.lang.String a2 " +
            "GROUP BY this.price", 
            
            serialize(query().from(product)
              .where(product.name.startsWith("A").or(product.name.endsWith("B")))
              .groupBy(product.price)
              .listExpr(product.name)));
    }
    
    @Test
    public void selectFrom2Sources(){
        assertEquals(
            "SELECT this.name " +
            "FROM com.mysema.query.jdoql.testdomain.Product " +
            "WHERE this.name == other.name " +
            "VARIABLES com.mysema.query.jdoql.testdomain.Product other", 
                
            serialize(query().from(product, other)
              .where(product.name.eq(other.name))
              .listExpr(product.name)));
    }
    
    @Test
    public void withSubQuery(){
        assertEquals(
            "SELECT this.price " +
            "FROM com.mysema.query.jdoql.testdomain.Product " +
            "WHERE this.price < (SELECT UNIQUE avg(other.price) FROM com.mysema.query.jdoql.testdomain.Product other)", 
                
            serialize(query().from(product)
              .where(product.price.lt(query().from(other).uniqueExpr(Grammar.avg(other.price))))
              .listExpr(product.price)));
    }

    @Test
    public void withSubQuery2(){
        // FIXME : how to model this ?!?
        assertEquals(
            "SELECT this.name " +
            "FROM com.mysema.query.jdoql.testdomain.Product " +
            "WHERE (SELECT other.price FROM com.mysema.query.jdoql.testdomain.Product other WHERE other.name == a1).contains(this.price) " +
            "PARAMETERS java.lang.String a1",

            serialize(query().from(product)
              .where(product.price.in(query().from(other).where(other.name.eq("Some name")).listExpr(other.price)))
              .listExpr(product.name)));
    }
    
    @Test
    public void  instanceofQuery(){
        assertEquals(
            "SELECT this " +
            "FROM com.mysema.query.jdoql.testdomain.Product " +
            "WHERE this instanceof com.mysema.query.jdoql.testdomain.Book",
              
            serialize(query().from(product)
              .where(product.instanceOf(Book.class))
              .listExpr(product)));
    }

    private JDOQLQuery query(){
        // creates detached query
        return new JDOQLQueryImpl(null);
    }

    private String serialize(SubQuery expr) {
        Expr<?> source = expr.getMetadata().getJoins().get(0).getTarget();
        JDOQLSerializer serializer = new JDOQLSerializer(JDOQLPatterns.DEFAULT, source);
        serializer.serialize(expr.getMetadata(), false, false);
        String rv = serializer.toString().replace('\n', ' ');
//        System.out.println(rv);
        return rv;
    }

}
