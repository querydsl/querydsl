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
import com.mysema.query.types.expr.SimpleExpression;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.template.SimpleTemplate;



public class OracleTemplatesTest extends AbstractSQLTemplatesTest{

    @Override
    protected SQLTemplates createTemplates() {
        return new OracleTemplates();
    }    
    
    @SuppressWarnings("unchecked")
    @Test
    public void Union(){        
        SimpleExpression<Integer> one = SimpleTemplate.create(Integer.class,"1");
        SimpleExpression<Integer> two = SimpleTemplate.create(Integer.class,"2");
        SimpleExpression<Integer> three = SimpleTemplate.create(Integer.class,"3");
        NumberPath<Integer> col1 = new NumberPath<Integer>(Integer.class,"col1");
        Union union = query.union(
            sq().unique(one.as(col1)),
            sq().unique(two),
            sq().unique(three));
        assertEquals(
                "(select 1 col1 from dual) " +
                "union " +
                "(select 2 from dual) " +
                "union " +
                "(select 3 from dual)", union.toString());
    }

    @Test
    public void Modifiers(){
        query.from(survey1).limit(5).offset(3);
        query.getMetadata().addProjection(survey1.id);
        assertEquals("select * from (  " +
        		"select a.*, rownum rn from (   " +
        		"select survey1.ID from SURVEY survey1  ) " +
        		"a) " +
        		"where rn > 3 and rn <= 8", query.toString());
    }
    
    @Test
    public void NextVal() {
        Operation<String> nextval = new OperationImpl<String>(String.class, SQLTemplates.NEXTVAL, ConstantImpl.create("myseq"));
        assertEquals("myseq.nextval", new SQLSerializer(new OracleTemplates()).handle(nextval).toString());        
    }

}
