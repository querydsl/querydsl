package com.mysema.query.jpa;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.query.jpa.domain.QCat;
import com.mysema.query.support.CollectionAnyVisitor;
import com.mysema.query.types.ConstantImpl;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.TemplateExpressionImpl;


public class JPQLCollectionAnyVisitorTest {

    private QCat cat = QCat.cat;
    
    @Test
    public void Path(){
        assertEquals("cat_kittens", serialize(cat.kittens.any()));
    }
    
    @Test
    public void Longer_Path(){
        assertEquals("cat_kittens.name", serialize(cat.kittens.any().name));
    }
    
    @Test
    public void Simple_BooleanOperation(){        
        Predicate predicate = cat.kittens.any().name.eq("Ruth123");        
        assertEquals("exists (select 1\n" +
        	"from Cat cat_kittens\n" +
        	"where cat_kittens in elements(cat.kittens) and cat_kittens.name = :a1)", serialize(predicate));
    }
    
    @Test
    public void Simple_StringOperation(){        
        Predicate predicate = cat.kittens.any().name.substring(1).eq("uth123");        
        assertEquals("exists (select 1\n" +
        	"from Cat cat_kittens\n" +
        	"where cat_kittens in elements(cat.kittens) and substring(cat_kittens.name,:a1+1) = :a2)", serialize(predicate));
    }
    
    @Test
    public void And_Operation(){
        // TODO : the subqueries should be merged
        Predicate predicate = cat.kittens.any().name.eq("Ruth123").and(cat.kittens.any().bodyWeight.gt(10.0));
        assertEquals("exists (select 1\n" +
                "from Cat cat_kittens\n" +
                "where cat_kittens in elements(cat.kittens) and cat_kittens.name = :a1) and exists (select 1\n" +
                "from Cat cat_kittens\n" +
                "where cat_kittens in elements(cat.kittens) and cat_kittens.bodyWeight > :a2)", serialize(predicate));
    }
    
    @Test
    public void Template(){
        Expression<Boolean> templateExpr = TemplateExpressionImpl.create(Boolean.class, "{0} = {1}", 
                cat.kittens.any().name, ConstantImpl.create("Ruth123"));
        assertEquals("exists (select 1\n" +
                "from Cat cat_kittens\n" +
                "where cat_kittens in elements(cat.kittens) and cat_kittens.name = :a1)", serialize(templateExpr));
    }
    
    private String serialize(Expression<?> expression){
        Expression<?> transformed = expression.accept(JPQLCollectionAnyVisitor.DEFAULT, new CollectionAnyVisitor.Context());
        JPQLSerializer serializer = new JPQLSerializer(new HQLTemplates());
        serializer.handle(transformed);
        return serializer.toString();
    }
    
}
