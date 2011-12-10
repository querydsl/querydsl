/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.query.types.ConstantImpl;
import com.mysema.query.types.Operation;
import com.mysema.query.types.OperationImpl;
import com.mysema.query.types.Path;
import com.mysema.query.types.expr.NumberExpression;
import com.mysema.query.types.path.SimplePath;
import com.mysema.query.types.template.NumberTemplate;


public class SQLServerTemplatesTest extends AbstractSQLTemplatesTest{
    
    @Override
    @Test
    public void NoFrom(){
        query.getMetadata().addProjection(NumberTemplate.ONE);
        assertEquals("select 1", query.toString());
    }

    @Override
    protected SQLTemplates createTemplates() {
        return new SQLServerTemplates();
    }

    @SuppressWarnings("unchecked")
    @Test
    @Override
    public void Union(){        
        NumberExpression<Integer> one = NumberTemplate.ONE;
        NumberExpression<Integer> two = NumberTemplate.TWO;
        NumberExpression<Integer> three = NumberTemplate.THREE;
        Path<Integer> col1 = new SimplePath<Integer>(Integer.class,"col1");
        Union union = query.union(
            sq().unique(one.as(col1)),
            sq().unique(two),
            sq().unique(three));
        assertEquals(
                "(select 1 as col1) " +
                "union " +
                "(select 2) " +
                "union " +
                "(select 3)", union.toString());
    }
 
    @Test
    public void Modifiers(){
        query.from(survey1).limit(5).offset(3);
        query.getMetadata().addProjection(survey1.id);        
        assertEquals("with inner_query as  (   " +
        		"select survey1.ID, row_number() over () as row_number from SURVEY survey1 ) " +
        		"select *  from inner_query where row_number > ? and row_number <= ?", query.toString());
    }
    
    @Test
    public void NextVal() {
        Operation<String> nextval = new OperationImpl<String>(String.class, SQLTemplates.NEXTVAL, ConstantImpl.create("myseq"));
        assertEquals("myseq.nextval", new SQLSerializer(new SQLServerTemplates()).handle(nextval).toString());        
    }

}
