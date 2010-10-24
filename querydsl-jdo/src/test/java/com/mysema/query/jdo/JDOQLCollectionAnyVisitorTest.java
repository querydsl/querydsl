package com.mysema.query.jdo;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.query.jdo.test.domain.QStore;
import com.mysema.query.support.CollectionAnyVisitor;
import com.mysema.query.types.ConstantImpl;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.TemplateExpressionImpl;


public class JDOQLCollectionAnyVisitorTest {

    private QStore store = QStore.store;
    
    @Test
    public void Path(){
        assertEquals("store_products", serialize(store.products.any()));
    }
    
    @Test
    public void Longer_Path(){
        assertEquals("store_products.name", serialize(store.products.any().name));
    }
    
    @Test
    public void Simple_BooleanOperation(){        
        Predicate predicate = store.products.any().name.eq("Product123");        
        assertEquals("store_products.name == a1", serialize(predicate));
    }
    
    @Test
    public void Simple_StringOperation(){        
        Predicate predicate = store.products.any().name.substring(1).eq("roduct123");        
        assertEquals("store_products.name.substring(a1) == a2", serialize(predicate));
    }
    
    @Test
    public void And_Operation(){
        Predicate predicate = store.products.any().name.eq("Product123").and(store.products.any().price.lt(10.0));
        assertEquals("store_products.name == a1 && store_products.price < a2", serialize(predicate));
    }
    
    @Test
    public void Template(){
        Expression<Boolean> templateExpr = TemplateExpressionImpl.create(Boolean.class, "{0} = {1}", 
                store.products.any().name, ConstantImpl.create("Product123"));
        assertEquals("store_products.name = a1", serialize(templateExpr));
    }
    
    private String serialize(Expression<?> expression){
        Expression<?> transformed = expression.accept(JDOQLCollectionAnyVisitor.DEFAULT, new CollectionAnyVisitor.Context());
        JDOQLSerializer serializer = new JDOQLSerializer(new JDOQLTemplates(), store);
        serializer.handle(transformed);
        return serializer.toString();
    }
    
}
