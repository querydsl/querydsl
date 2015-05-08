/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
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
package com.mysema.query.types.expr;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Method;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.mysema.query.types.OperationImpl;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathImpl;
import com.mysema.query.types.path.ArrayPath;
import com.mysema.query.types.path.BeanPath;
import com.mysema.query.types.path.BooleanPath;
import com.mysema.query.types.path.CollectionPath;
import com.mysema.query.types.path.ComparablePath;
import com.mysema.query.types.path.DatePath;
import com.mysema.query.types.path.DateTimePath;
import com.mysema.query.types.path.EnumPath;
import com.mysema.query.types.path.ListPath;
import com.mysema.query.types.path.MapPath;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.SetPath;
import com.mysema.query.types.path.SimplePath;
import com.mysema.query.types.path.StringPath;
import com.mysema.query.types.path.TimePath;

public class SimpleExpressionTest {

    enum ExampleEnum {A,B}

    @Test
    public void As_usage() {
        SimpleExpression<String> str = new StringPath("str");
        assertEquals("str as alias", str.as("alias").toString());
        assertEquals("str as alias", str.as(new StringPath("alias")).toString());
    }
    
    @Test
    public void Case() {
        SimpleExpression<String> str = new StringPath("str");
        // nullif(str, 'xxx')
        str.when("xxx").thenNull().otherwise(str);
    }

    @Test
    public void Subclasses_Override_As() throws SecurityException, NoSuchMethodException{
        List<Class<?>> classes = Arrays.<Class<?>>asList(
                BooleanExpression.class,
                ComparableExpression.class,
                DateExpression.class,
                DateTimeExpression.class,
                EnumExpression.class,
                NumberExpression.class,
                SimpleExpression.class,
                StringExpression.class,
                TimeExpression.class);

        for (Class<?> cl : classes) {
            Method asPath = cl.getDeclaredMethod("as", Path.class);
            assertEquals(cl, asPath.getReturnType());

            Method asString = cl.getDeclaredMethod("as", String.class);
            assertEquals(cl, asString.getReturnType());
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void Various() {
        List<DslExpression<?>> paths = new ArrayList<DslExpression<?>>();
        paths.add(new ArrayPath(String[].class, "p"));
        paths.add(new BeanPath(Object.class, "p"));
        paths.add(new BooleanPath("p"));
        paths.add(new CollectionPath(String.class, StringPath.class, "p"));
        paths.add(new ComparablePath(String.class,"p"));
        paths.add(new DatePath(Date.class,"p"));
        paths.add(new DateTimePath(Date.class,"p"));
        paths.add(new EnumPath(ExampleEnum.class,"p"));
        paths.add(new ListPath(String.class, StringPath.class, "p"));
        paths.add(new MapPath(String.class, String.class, StringPath.class, "p"));
        paths.add(new NumberPath(Integer.class,"p"));
        paths.add(new SetPath(String.class, StringPath.class, "p"));
        paths.add(new SimplePath(String.class,"p"));
        paths.add(new StringPath("p"));
        paths.add(new TimePath(Time.class,"p"));

        for (DslExpression<?> expr : paths) {
            Path o = new PathImpl(expr.getType(), "o");
            assertEquals(OperationImpl.create(expr.getType(), Ops.ALIAS, expr, o), expr.as("o"));
            Path p = new PathImpl(expr.getType(), "p");
            assertEquals(OperationImpl.create(expr.getType(), Ops.ALIAS, expr, p), expr.as(p));
        }
    }

    @Test(expected=IllegalArgumentException.class)
    public void Eq_Null() {
        new SimplePath<Object>(Object.class, "path").eq((Object)null);
    }
}
