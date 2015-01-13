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

import com.querydsl.core.support.Expressions;
import com.querydsl.core.types.*;
import com.querydsl.core.types.path.NumberPath;
import com.querydsl.core.types.template.SimpleTemplate;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.junit.Test;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SQLTemplatesTest {

    private static final String DATETIME = "\\(timestamp '\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}'\\)";
    private static final String TIME = "\\(time '\\d{2}:\\d{2}:\\d{2}'\\)";
    private static final String DATE = "\\(date '\\d{4}-\\d{2}-\\d{2}'\\)";

    private static void assertMatches(String regex, String str) {
        assertTrue(str, str.matches(regex));
    }

    @Test
    public void test() {
        Template template = TemplateFactory.DEFAULT.create("fetch first {0s} rows only");
        assertTrue(template.getElements().get(1) instanceof Template.AsString);

        SQLSerializer serializer = new SQLSerializer(new Configuration(new DerbyTemplates()));
        serializer.handle(SimpleTemplate.create(Object.class, template, ConstantImpl.create(5)));
        assertEquals("fetch first 5 rows only", serializer.toString());
    }

    @Test
    public void AsLiteral() {
        SQLTemplates templates = SQLTemplates.DEFAULT;
        Configuration conf = new Configuration(templates);
        assertMatches(DATE, conf.asLiteral(new Date(0)));
        assertMatches(TIME, conf.asLiteral(new Time(0)));
        assertMatches(DATETIME, conf.asLiteral(new Timestamp(0)));
    }

    @Test
    public void AsLiteral_JodaTime() {
        SQLTemplates templates = SQLTemplates.DEFAULT;
        Configuration conf = new Configuration(templates);
        assertMatches(DATE, conf.asLiteral(new LocalDate(0)));
        assertMatches(TIME, conf.asLiteral(new LocalTime(0)));
        assertMatches(DATETIME, conf.asLiteral(new DateTime(0)));
    }

    @Test
    public void Quote() {
        Pattern pattern = Pattern.compile("[a-zA-Z0-9_\\-]+");
        assertTrue(pattern.matcher("a1").matches());
        assertTrue(pattern.matcher("a").matches());
    }

    @Test
    public void Quoting_Performance() {
        // 385 -> 63
        SQLTemplates templates = new H2Templates();
        long start = System.currentTimeMillis();
        int iterations = 1000000;
        for (int i = 0; i < iterations; i++) {
            templates.quoteIdentifier("companies");
        }
        System.err.println(System.currentTimeMillis() - start);
    }

    @Test
    public void NextVal() {
        Operation<String> nextval = OperationImpl.create(String.class, SQLOps.NEXTVAL, ConstantImpl.create("myseq"));
        assertEquals("nextval('myseq')", new SQLSerializer(new Configuration(SQLTemplates.DEFAULT)).handle(nextval).toString());
        // Derby OK
        // H2 OK
        // HSQLDB OK
        // MSSQL OK
        // MySQL
        // Oracle OK
        // Postgres OK

    }

    @Test
    public void Numeric_Operations() {
        NumberPath<Integer> intPath = Expressions.numberPath(Integer.class, "intPath");
        NumberPath<Integer> intPath2 = Expressions.numberPath(Integer.class, "intPath2");
        SQLSerializer serializer = new SQLSerializer(new Configuration(SQLTemplates.DEFAULT));
        serializer.handle(intPath.subtract(intPath2.add(2)));
        assertEquals("intPath - (intPath2 + ?)", serializer.toString());
    }

}
