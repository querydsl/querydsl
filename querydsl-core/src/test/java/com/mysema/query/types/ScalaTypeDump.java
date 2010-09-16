/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.mysema.codegen.ScalaWriter;
import com.mysema.codegen.model.ClassType;
import com.mysema.codegen.model.Parameter;
import com.mysema.codegen.model.Type;
import com.mysema.query.types.expr.*;

public class ScalaTypeDump {
    
    @Test
    @Ignore
    public void test() throws IOException{
        List<Class<?>> classes = new ArrayList<Class<?>>();
        classes.add(SimpleExpression.class);
        classes.add(ComparableExpression.class);
        classes.add(BooleanExpression.class);
        classes.add(StringExpression.class);
        classes.add(TemporalExpression.class);
        classes.add(TimeExpression.class);
        classes.add(DateTimeExpression.class);
        classes.add(DateExpression.class);
        classes.add(EnumExpression.class);
        classes.add(NumberExpression.class);
        
        StringWriter w = new StringWriter();
        ScalaWriter writer = new ScalaWriter(w);
        writer.packageDecl("com.mysema.query.scala");
        writer.imports(Expression.class.getPackage());
        for (Class<?> cl : classes){
            Type type = new ClassType(cl);
            Type superClass = new ClassType(cl.getSuperclass());
            writer.beginClass(type, superClass);
            for (Method m : cl.getDeclaredMethods()){
                List<Parameter> params = new ArrayList<Parameter>();
                for (Class<?> paramType : m.getParameterTypes()){
                    params.add(new Parameter("arg"+params.size(), new ClassType(paramType)));
                }
                Type returnType = new ClassType(m.getReturnType());
                writer.beginPublicMethod(returnType, ":"+m.getName(), params.toArray(new Parameter[params.size()]));
                writer.end();
            }
            writer.end();
        }
        
        System.out.println(w);
    }

}
