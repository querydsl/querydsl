/*
 * Copyright 2011, Mysema Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.sql;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.querydsl.core.QueryFlag;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Operation;
import com.querydsl.core.types.OperationImpl;
import com.querydsl.core.types.expr.SimpleExpression;
import com.querydsl.core.types.path.NumberPath;
import com.querydsl.core.types.template.SimpleTemplate;



public class OracleTemplatesTest extends AbstractSQLTemplatesTest{

    @Override
    protected SQLTemplates createTemplates() {
        return new OracleTemplates();
    }

    @Override
    @SuppressWarnings("unchecked")
    @Test
    public void Union() {
        SimpleExpression<Integer> one = SimpleTemplate.create(Integer.class,"1");
        SimpleExpression<Integer> two = SimpleTemplate.create(Integer.class,"2");
        SimpleExpression<Integer> three = SimpleTemplate.create(Integer.class,"3");
        NumberPath<Integer> col1 = new NumberPath<Integer>(Integer.class,"col1");
        Union union = query.union(
            sq().unique(one.as(col1)),
            sq().unique(two),
            sq().unique(three));
        assertEquals(
                "(select 1 col1 from dual)\n" +
                "union\n" +
                "(select 2 from dual)\n" +
                "union\n" +
                "(select 3 from dual)", union.toString());
    }

    @Test
    public void Modifiers() {
        query.from(survey1).limit(5).offset(3);
        query.getMetadata().addProjection(survey1.id);
        assertEquals("select * from (  " +
                "select a.*, rownum rn from (   " +
                "select survey1.ID from SURVEY survey1  ) " +
                "a) " +
                "where rn > 3 and rownum <= 5", query.toString());
    }

    @Test
    public void Modifiers2() {
        query.from(survey1).limit(5).offset(3);
        query.getMetadata().addProjection(survey1.id);
        query.getMetadata().addFlag(new QueryFlag(QueryFlag.Position.AFTER_PROJECTION, ", count(*) over() "));

        assertEquals("select * from (  " +
        	"select a.*, rownum rn from (   " +
        	"select survey1.ID, count(*) over()  from SURVEY survey1  ) " +
        	"a) " +
        	"where rn > 3 and rownum <= 5", query.toString());
    }

    @Test
    public void NextVal() {
        Operation<String> nextval = OperationImpl.create(String.class, SQLOps.NEXTVAL, ConstantImpl.create("myseq"));
        assertEquals("myseq.nextval", new SQLSerializer(new Configuration(new OracleTemplates())).handle(nextval).toString());
    }

}
