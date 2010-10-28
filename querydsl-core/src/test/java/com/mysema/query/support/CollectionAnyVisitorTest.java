package com.mysema.query.support;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.query.domain.QCat;
import com.mysema.query.types.ConstantImpl;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.TemplateExpressionImpl;


public class CollectionAnyVisitorTest {

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
    public void Very_Long_Path(){
        assertEquals("cat_kittens_kittens.name", serialize(cat.kittens.any().kittens.any().name));
    }
    
    @Test
    public void Simple_BooleanOperation(){        
        Predicate predicate = cat.kittens.any().name.eq("Ruth123");        
        assertEquals("cat_kittens.name = Ruth123", serialize(predicate));
    }
    
    @Test
    public void Simple_StringOperation(){        
        Predicate predicate = cat.kittens.any().name.substring(1).eq("uth123");        
        assertEquals("substring(cat_kittens.name,1) = uth123", serialize(predicate));
    }
    
    @Test
    public void And_Operation(){
        // TODO : the subqueries should be merged
        Predicate predicate = cat.kittens.any().name.eq("Ruth123").and(cat.kittens.any().bodyWeight.gt(10.0));
        assertEquals("cat_kittens.name = Ruth123 && cat_kittens.bodyWeight > 10.0", serialize(predicate));
    }
    
    @Test
    public void Template(){
        Expression<Boolean> templateExpr = TemplateExpressionImpl.create(Boolean.class, "{0} = {1}", 
                cat.kittens.any().name, ConstantImpl.create("Ruth123"));
        assertEquals("cat_kittens.name = Ruth123", serialize(templateExpr));
    }
    
    private String serialize(Expression<?> expression){
        CollectionAnyVisitor visitor = new CollectionAnyVisitor(){
            @Override
            protected Predicate exists(Context c, Predicate condition) {
                return condition;
            }            
        };
        Expression<?> transformed = expression.accept(visitor, new CollectionAnyVisitor.Context());
        return transformed.toString();
    }
    
}
