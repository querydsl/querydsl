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



public class MySQLTemplatesTest extends AbstractSQLTemplatesTest {

    @Override
    protected SQLTemplates createTemplates() {
        return new MySQLTemplates();
    }

    @Test
    public void test() {
        SQLTemplates templates = MySQLTemplates.builder()
                .printSchema()
                .build();
        Configuration conf = new Configuration(templates);
        System.out.println(new SQLQuery(conf).from(survey1).toString());
    }

    @Test
    public void Order_NullsFirst() {
        query.from(survey1).orderBy(survey1.name.asc().nullsFirst());
        assertEquals("from SURVEY survey1 order by (case when survey1.NAME is null then 0 else 1 end), survey1.NAME asc", query.toString());
    }

    @Test
    public void Order_NullsLast() {
        query.from(survey1).orderBy(survey1.name.asc().nullsLast());
        assertEquals("from SURVEY survey1 order by (case when survey1.NAME is null then 1 else 0 end), survey1.NAME asc", query.toString());
    }



}
