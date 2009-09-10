/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql;

import java.io.File;
import java.math.BigDecimal;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import com.mysema.query.codegen.ClassModel;
import com.mysema.query.codegen.ClassModelFactory;
import com.mysema.query.codegen.FieldModel;
import com.mysema.query.codegen.Serializer;
import com.mysema.query.codegen.Serializers;
import com.mysema.query.codegen.TypeCategory;
import com.mysema.query.codegen.TypeModel;
import com.mysema.query.codegen.TypeModelFactory;
import com.mysema.query.util.FileUtils;

/**
 * MetadataExporter exports JDBC metadata to Querydsl query types
 * 
 * @author tiwe
 * @version $Id$
 */
public class MetaDataExporter {

    private final Map<Integer, Class<?>> sqlToJavaType = new HashMap<Integer, Class<?>>();

    private final String namePrefix, targetFolder, packageName;

    private final String schemaPattern, tableNamePattern;

    private static final Serializer serializer = Serializers.ENTITY;
    
    public MetaDataExporter(String namePrefix, String packageName, String schemaPattern, String tableNamePattern, String targetFolder){
        this.namePrefix = namePrefix;
        this.packageName = packageName;
        this.schemaPattern = schemaPattern;
        this.tableNamePattern = tableNamePattern;
        this.targetFolder = targetFolder;
        
        // BOOLEAN
        sqlToJavaType.put(Types.BIT, Boolean.class);
        sqlToJavaType.put(Types.BOOLEAN, Boolean.class);

        // NUMERIC
        sqlToJavaType.put(Types.BIGINT, Long.class);
        sqlToJavaType.put(Types.DOUBLE, Double.class);
        sqlToJavaType.put(Types.INTEGER, Integer.class);
        sqlToJavaType.put(Types.SMALLINT, Short.class);
        sqlToJavaType.put(Types.TINYINT, Byte.class);
        sqlToJavaType.put(Types.FLOAT, Float.class);
        sqlToJavaType.put(Types.REAL, Float.class);
        sqlToJavaType.put(Types.NUMERIC, BigDecimal.class);
        sqlToJavaType.put(Types.DECIMAL, BigDecimal.class);

        // DATE and TIME
        sqlToJavaType.put(Types.DATE, java.util.Date.class);
        sqlToJavaType.put(Types.TIME, Time.class);
        sqlToJavaType.put(Types.TIMESTAMP, java.util.Date.class);

        // TEXT
        sqlToJavaType.put(Types.CHAR, Character.class);
        sqlToJavaType.put(Types.CLOB, String.class);
        sqlToJavaType.put(Types.VARCHAR, String.class);
        sqlToJavaType.put(Types.LONGVARCHAR, String.class);

        // OTHER
        sqlToJavaType.put(Types.NULL, Object.class);
        sqlToJavaType.put(Types.OTHER, Object.class);
        sqlToJavaType.put(Types.REAL, Object.class);
        sqlToJavaType.put(Types.REF, Object.class);
        sqlToJavaType.put(Types.STRUCT, Object.class);
        sqlToJavaType.put(Types.JAVA_OBJECT, Object.class);
        sqlToJavaType.put(Types.BINARY, Object.class);
        sqlToJavaType.put(Types.LONGVARBINARY, Object.class);
        sqlToJavaType.put(Types.VARBINARY, Object.class);
        sqlToJavaType.put(Types.BLOB, Object.class);
    }


    public void export(DatabaseMetaData md) throws SQLException {
        if (targetFolder == null)
            throw new IllegalArgumentException("targetFolder needs to be set");
        if (packageName == null)
            throw new IllegalArgumentException("packageName needs to be set");

        ResultSet tables = md.getTables(null, schemaPattern, tableNamePattern, null);
        ClassModelFactory factory = new ClassModelFactory(new TypeModelFactory());
        while (tables.next()) {
            String tableName = tables.getString(3);
            ClassModel classModel = new ClassModel(factory, namePrefix, null, "java.lang", "java.lang.Object", tableName);
            ResultSet columns = md.getColumns(null, schemaPattern, tables.getString(3), null);
            while (columns.next()) {
                String name = columns.getString(4);
                Class<?> clazz = sqlToJavaType.get(columns.getInt(5));
                if (clazz == null){
                    throw new RuntimeException("No java type for " + columns.getString(6));
                }                    
                TypeCategory fieldType = TypeCategory.COMPARABLE;
                if (clazz.equals(Boolean.class) || clazz.equals(boolean.class)) {
                    fieldType = TypeCategory.BOOLEAN;
                } else if (clazz.equals(String.class)) {
                    fieldType = TypeCategory.STRING;
                }

                TypeModel typeModel = new TypeModel(
                        fieldType, 
                        clazz.getName(),
                        clazz.getPackage().getName(),
                        clazz.getSimpleName(),
                        null, null);
                classModel.addField(new FieldModel(classModel, name, typeModel, name));
            }
            columns.close();
            serialize(classModel);
        }
        tables.close();
    }

    private void serialize(ClassModel type) {
        try {
            String path = packageName.replace('.', '/') + "/" + namePrefix + type.getSimpleName() + ".java";
            serializer.serialize(type, FileUtils.writerFor(new File(targetFolder, path)));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}
