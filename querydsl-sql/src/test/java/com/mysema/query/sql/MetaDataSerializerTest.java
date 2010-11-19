/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.tools.JavaCompiler;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.mysema.codegen.CodeWriter;
import com.mysema.codegen.SimpleCompiler;
import com.mysema.codegen.model.ClassType;
import com.mysema.codegen.model.SimpleType;
import com.mysema.codegen.model.Type;
import com.mysema.codegen.model.Types;
import com.mysema.query.AbstractJDBCTest;
import com.mysema.query.codegen.EntityType;
import com.mysema.query.codegen.Property;
import com.mysema.query.codegen.SerializerConfig;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Path;

public class MetaDataSerializerTest extends AbstractJDBCTest{

    @Override
    @Before
    public void setUp() throws SQLException, ClassNotFoundException{
        super.setUp();
        statement.execute("drop table employee if exists");
        statement.execute("drop table survey if exists");
        statement.execute("drop table date_test if exists");
        statement.execute("drop table date_time_test if exists");

        statement.execute("create table survey (id int, name varchar(30), "
                + "CONSTRAINT PK_survey PRIMARY KEY (id, name))");
        statement.execute("create table date_test (d date)");
        statement.execute("create table date_time_test (dt datetime)");
        statement.execute("create table employee("
                + "id INT, "
                + "firstname VARCHAR(50), "
                + "lastname VARCHAR(50), "
                + "salary DECIMAL(10, 2), "
                + "datefield DATE, "
                + "timefield TIME, "
                + "superior_id int, "
                + "survey_id int, "
                + "survey_name varchar(30), "
                + "CONSTRAINT PK_employee PRIMARY KEY (id), "
                + "CONSTRAINT FK_survey FOREIGN KEY (survey_id, survey_name) REFERENCES survey(id,name), "
                + "CONSTRAINT FK_superior FOREIGN KEY (superior_id) REFERENCES employee(id))");
    }

    @Test
    public void Normal_serialization() throws SQLException{
        String namePrefix = "Q";
        NamingStrategy namingStrategy = new DefaultNamingStrategy();
        // customization of serialization
        MetaDataSerializer serializer = new MetaDataSerializer(namePrefix, namingStrategy);
        MetaDataExporter exporter = new MetaDataExporter();
        exporter.setNamePrefix(namePrefix);
        exporter.setPackageName("test");
        exporter.setTargetFolder(new File("target/cust1"));
        exporter.setNamingStrategy(namingStrategy);
        exporter.setSerializer(serializer);
        exporter.export(connection.getMetaData());

        compile(exporter);
    }

    @Test
    public void Custom_serialization() throws Exception {
        String namePrefix = "Q";
        NamingStrategy namingStrategy = new DefaultNamingStrategy();
        // customization of serialization
        MetaDataSerializer serializer = new MetaDataSerializer(namePrefix, namingStrategy){

            @Override
            protected void introImports(CodeWriter writer, SerializerConfig config, EntityType model) throws IOException {
                super.introImports(writer, config, model);
                // adds additional imports
                writer.imports(List.class, Arrays.class);
            }

            @Override
            protected void serializeProperties(EntityType model,  SerializerConfig config, CodeWriter writer) throws IOException {
                super.serializeProperties(model, config, writer);
                StringBuilder paths = new StringBuilder();
                for (Property property : model.getProperties()){
                    if (paths.length() > 0){
                        paths.append(", ");
                    }
                    paths.append(property.getEscapedName());
                }
                // adds accessors for all fields
                writer.publicFinal(new SimpleType(Types.LIST, new ClassType(Expression.class, (Type)null)), "exprs", "Arrays.<Expression<?>>asList(" + paths.toString() + ")");
                writer.publicFinal(new SimpleType(Types.LIST, new ClassType(Path.class, (Type)null)), "paths", "Arrays.<Path<?>>asList(" + paths.toString() + ")");
            }

        };
        MetaDataExporter exporter = new MetaDataExporter();
        exporter.setNamePrefix(namePrefix);
        exporter.setPackageName("test");
        exporter.setTargetFolder(new File("target/cust2"));
        exporter.setNamingStrategy(namingStrategy);
        exporter.setSerializer(serializer);
        exporter.export(connection.getMetaData());

        compile(exporter);
    }

    private void compile(MetaDataExporter exporter) {
        JavaCompiler compiler = new SimpleCompiler();
        Set<String> classes = exporter.getClasses();
        int compilationResult = compiler.run(null, null, null, classes.toArray(new String[classes.size()]));
        if(compilationResult == 0){
            System.out.println("Compilation is successful");
        }else{
            Assert.fail("Compilation Failed");
        }
    }

}
