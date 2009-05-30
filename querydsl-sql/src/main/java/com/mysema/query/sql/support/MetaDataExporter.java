/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql.support;

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
import com.mysema.query.codegen.FieldModel;
import com.mysema.query.codegen.FieldType;
import com.mysema.query.codegen.Serializer;
import com.mysema.query.codegen.Serializers;
import com.mysema.query.codegen.SimpleTypeModel;
import com.mysema.query.codegen.TypeModel;
import com.mysema.query.util.FileUtils;

/**
 * MetadataExporter exports JDBC metadata to Querydsl query types
 * 
 * @author tiwe
 * @version $Id$
 */
public class MetaDataExporter {

    private Map<Integer, Class<?>> sqlToJavaType = new HashMap<Integer, Class<?>>();

    {
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

    private String namePrefix = "", targetFolder, packageName;

    private String schemaPattern, tableNamePattern;

    private Serializer serializer = Serializers.DOMAIN;

    public void export(DatabaseMetaData md) throws SQLException {
        if (targetFolder == null)
            throw new IllegalArgumentException("targetFolder needs to be set");
        if (packageName == null)
            throw new IllegalArgumentException("packageName needs to be set");

        ResultSet tables = md.getTables(null, schemaPattern, tableNamePattern, null);
        while (tables.next()) {
            String tableName = tables.getString(3);
            // if (camelCase){
            // tableName = toCamelCase(tableName, true);
            // }
            ClassModel type = new ClassModel(null, "java.lang", "java.lang.Object",
                    tableName);
            ResultSet columns = md.getColumns(null, schemaPattern, tables
                    .getString(3), null);
            while (columns.next()) {
                String _name = columns.getString(4);
                Class<?> _class = sqlToJavaType.get(columns.getInt(5));
                if (_class == null){
                    throw new RuntimeException("No java type for " + columns.getString(6));
                }                    
                FieldType _type;
                if (_class.equals(Boolean.class) || _class.equals(boolean.class)) {
                    _type = FieldType.BOOLEAN;
                } else if (_class.equals(String.class)) {
                    _type = FieldType.STRING;
                } else {
                    _type = FieldType.COMPARABLE;
                }

                TypeModel typeModel = new SimpleTypeModel(_type, 
                        _class.getName(),
                        _class.getPackage().getName(),
                        _class.getSimpleName(), null, null);
                type.addField(new FieldModel(_name, typeModel));
            }
            columns.close();
            serialize(type);
        }
        tables.close();
    }

    private void serialize(ClassModel type) {
        // populate model
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("pre", namePrefix);
        model.put("package", packageName);
        model.put("type", type);
        model.put("classSimpleName", type.getSimpleName());

        // serialize it
        try {
            String path = packageName.replace('.', '/') + "/" + namePrefix + type.getSimpleName() + ".java";
            serializer.serialize(model, FileUtils.writerFor(new File(targetFolder, path)));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void setNamePrefix(String namePrefix) {
        this.namePrefix = namePrefix;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void setSchemaPattern(String schemaPattern) {
        this.schemaPattern = schemaPattern;
    }

    public void setTableNamePattern(String tableNamePattern) {
        this.tableNamePattern = tableNamePattern;
    }

    public void setTargetFolder(String targetFolder) {
        this.targetFolder = targetFolder;
    }

}
