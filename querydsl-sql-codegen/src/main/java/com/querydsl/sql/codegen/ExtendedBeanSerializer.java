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
package com.querydsl.sql.codegen;

import com.querydsl.codegen.BeanSerializer;
import com.querydsl.codegen.EntityType;
import com.querydsl.codegen.GeneratedAnnotationClass;
import com.querydsl.codegen.Property;
import com.querydsl.codegen.utils.CodeWriter;
import com.querydsl.codegen.utils.model.Parameter;
import com.querydsl.codegen.utils.model.Types;
import com.querydsl.sql.Column;
import com.querydsl.sql.codegen.support.PrimaryKeyData;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@code ExtendedBeanSerializer} outputs primary key based {@code equals}, {@code hashCode} and
 * {@code toString} implementations
 *
 * Requires column annotation generation to be enabled
 *
 * @author tiwe
 *
 */
public class ExtendedBeanSerializer extends BeanSerializer {

    private static final Parameter o = new Parameter("o", Types.OBJECT);

    public ExtendedBeanSerializer() {
    }

    /**
     * Create a new ExtendedBeanSerializer with the given javadoc suffix and generatedAnnotationClass
     *
     * @param javadocSuffix suffix to be used after the simple name in class level javadoc
     * @param generatedAnnotationClass the fully qualified class name of the <em>Single-Element Annotation</em> (with {@code String} element) to be used on the generated classes.
     * @see <a href="https://docs.oracle.com/javase/specs/jls/se8/html/jls-9.html#jls-9.7.3">Single-Element Annotation</a>
     */
    @Inject
    public ExtendedBeanSerializer(
            @Named(SQLCodegenModule.JAVADOC_SUFFIX) String javadocSuffix,
            @Named(SQLCodegenModule.GENERATED_ANNOTATION_CLASS) GeneratedAnnotationClass generatedAnnotationClass) {
        super(javadocSuffix, generatedAnnotationClass);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void bodyEnd(EntityType model, CodeWriter writer) throws IOException {
        Collection<PrimaryKeyData> primaryKeys = (Collection<PrimaryKeyData>) model.getData().get(PrimaryKeyData.class);

        if (primaryKeys == null || primaryKeys.isEmpty()) {
            return;
        }

        Map<String, Property> columnToProperty = new HashMap<String, Property>();
        for (Property property : model.getProperties()) {
            columnToProperty.put(property.getAnnotation(Column.class).value(), property);
        }

        StringBuilder anyColumnIsNull = new StringBuilder();
        StringBuilder columnEquals = new StringBuilder();
        List<String> properties = new ArrayList<String>();
        for (PrimaryKeyData pk : primaryKeys) {
            for (String column : pk.getColumns()) {
                Property property = columnToProperty.get(column);
                String propName = property.getEscapedName();
                if (anyColumnIsNull.length() > 0) {
                    anyColumnIsNull.append(" || ");
                    columnEquals.append(" && ");
                }
                anyColumnIsNull.append(propName).append(" == null");
                columnEquals.append(propName).append(".equals(obj.").append(propName).append(")");
                properties.add(propName);
            }
        }

        // equals
        writer.annotation(Override.class);
        writer.beginPublicMethod(Types.BOOLEAN_P, "equals", o);
        writer.line("if (", anyColumnIsNull + ") {");
        writer.line("    return super.equals(o);");
        writer.line("}");
        writer.line("if (!(o instanceof ", model.getSimpleName(), ")) {");
        writer.line("    return false;");
        writer.line("}");
        writer.line(model.getSimpleName(), " obj = (", model.getSimpleName(), ") o;");
        writer.line("return ", columnEquals + ";");
        writer.end();

        // hashCode
        writer.annotation(Override.class);
        writer.beginPublicMethod(Types.INT, "hashCode");
        writer.line("if (", anyColumnIsNull + ") {");
        writer.line("    return super.hashCode();");
        writer.line("}");
        writer.line("final int prime = 31;");
        writer.line("int result = 1;");
        for (String property : properties) {
            writer.line("result = prime * result + ", property, ".hashCode();");
        }
        writer.line("return result;");
        writer.end();


    }

    @Override
    protected void addToString(EntityType model, CodeWriter writer) throws IOException {
        Collection<PrimaryKeyData> primaryKeys = (Collection<PrimaryKeyData>) model.getData().get(PrimaryKeyData.class);

        if (primaryKeys == null || primaryKeys.isEmpty()) {
            super.addToString(model, writer);
            return;
        }

        StringBuilder toString = new StringBuilder();
        Map<String, Property> columnToProperty = new HashMap<String, Property>();
        for (Property property : model.getProperties()) {
            columnToProperty.put(property.getAnnotation(Column.class).value(), property);
        }

        for (PrimaryKeyData pk : primaryKeys) {
            for (String column : pk.getColumns()) {
                Property property = columnToProperty.get(column);
                String propName = property.getEscapedName();
                if (toString.length() > 0) {
                    toString.append("+ \";\" + ");
                } else {
                    toString.append("\"" + model.getSimpleName() + "#\" + ");
                }
                toString.append(propName);
            }
        }

        // toString
        writer.annotation(Override.class);
        writer.beginPublicMethod(Types.STRING, "toString");
//        writer.line("if (", anyColumnIsNull + ") {");
//        writer.line("    return super.toString();");
//        writer.line("}");
        writer.line("return ", toString + ";");
        writer.end();
    }

}
