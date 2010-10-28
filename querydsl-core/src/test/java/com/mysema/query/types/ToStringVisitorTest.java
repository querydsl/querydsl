package com.mysema.query.types;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.query.domain.QCat;


public class ToStringVisitorTest {
    
    private Templates templates = new Templates(){
        {
            add(PathType.PROPERTY, "{0}_{1}");
            add(PathType.COLLECTION_ANY, "{0}");
        }};
    
    @Test
    public void Operation(){
        assertEquals("cat_name is not null", QCat.cat.name.isNotNull().accept(ToStringVisitor.DEFAULT, templates));
    }
        
    @Test
    public void Template(){
        Expression<Boolean> template = TemplateExpressionImpl.create(Boolean.class, "{0} is not null", QCat.cat.name);
        assertEquals("cat_name is not null", template.accept(ToStringVisitor.DEFAULT, templates));
    }
    
    @Test
    public void Path(){
        assertEquals("cat_kittens_kittens_name", QCat.cat.kittens.any().kittens.any().name.accept(ToStringVisitor.DEFAULT, templates));
    }

}
