/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.Nullable;

import net.jcip.annotations.Immutable;

import com.mysema.commons.lang.Assert;
import com.mysema.query.codegen.ClassType;
import com.mysema.query.codegen.EntitySerializer;
import com.mysema.query.codegen.EntityType;
import com.mysema.query.codegen.Method;
import com.mysema.query.codegen.Property;
import com.mysema.query.codegen.Serializer;
import com.mysema.query.codegen.SerializerConfig;
import com.mysema.query.codegen.SimpleSerializerConfig;
import com.mysema.query.codegen.SimpleType;
import com.mysema.query.codegen.Type;
import com.mysema.query.codegen.TypeCategory;
import com.mysema.query.codegen.TypeMappings;
import com.mysema.query.codegen.Types;
import com.mysema.util.JavaWriter;

/**
 * MetadataExporter exports JDBC metadata to Querydsl query types
 * 
 * @author tiwe
 * @version $Id$
 */
@Immutable
public class MetaDataExporter {

    private static final Serializer serializer = new EntitySerializer(new TypeMappings());
    
    private static final SQLTypeMapping typeMapping = new SQLTypeMapping();
    
    private static final SerializerConfig serializerConfig = new SimpleSerializerConfig(false, false, false, false);
    
    private final String namePrefix, targetFolder, packageName;

    @Nullable
    private final String schemaPattern, tableNamePattern;
    
    /**
     * Create a new MetaDataExport instance
     * 
     * @param namePrefix name prefix to use
     * @param packageName target package name for query types
     * @param schemaPattern schema pattern for DatabaseMetaData.getTables or null
     * @param tableNamePattern table name pattern for DatabaseMetaData.getTables or null
     * @param targetFolder target folder for serialization
     */
    public MetaDataExporter(String namePrefix, 
            String packageName, 
            @Nullable String schemaPattern, 
            @Nullable String tableNamePattern, 
            String targetFolder){
        this.namePrefix = namePrefix;
        this.packageName = packageName;
        this.schemaPattern = schemaPattern;
        this.tableNamePattern = tableNamePattern;
        this.targetFolder = targetFolder;       
    }

    public void export(DatabaseMetaData md) throws SQLException {
        Assert.notNull(targetFolder, "targetFolder needs to be set");
        Assert.notNull(packageName, "packageName needs to be set");

        ResultSet tables = md.getTables(null, schemaPattern, tableNamePattern, null);
        try{
            while (tables.next()) {
                String tableName = tables.getString(3);
                String simpleClassName = toClassName(tableName);
                Type classTypeModel = new SimpleType(
                        TypeCategory.ENTITY, 
                        packageName + "." + namePrefix + simpleClassName, 
                        packageName, 
                        namePrefix + simpleClassName, 
                        false);
                EntityType classModel = new EntityType("", classTypeModel);
                Method wildcard = new Method(classModel, "all", "{0}.*", Types.OBJECTS);
                classModel.addMethod(wildcard);
                classModel.addAnnotation(new TableImpl(tableName));
                ResultSet columns = md.getColumns(null, schemaPattern, tables.getString(3), null);
                try{
                    while (columns.next()) {
                        String columnName = columns.getString(4);
                        String propertyName = toPropertyName(columnName);
                        Class<?> clazz = typeMapping.get(columns.getInt(5));
                        if (clazz == null){
                            throw new RuntimeException("No java type for " + columns.getString(6));
                        }                    
                        TypeCategory fieldType = TypeCategory.SIMPLE;
                        if (clazz.equals(Boolean.class) || clazz.equals(boolean.class)) {
                            fieldType = TypeCategory.BOOLEAN;
                        } else if (clazz.equals(String.class)) {
                            fieldType = TypeCategory.STRING;
                        }else if (Number.class.isAssignableFrom(clazz)){
                            fieldType = TypeCategory.NUMERIC;
                        }else if (Comparable.class.isAssignableFrom(clazz)){
                            fieldType = TypeCategory.COMPARABLE;
                        }

                        Type typeModel = new ClassType(fieldType, clazz);
                        classModel.addProperty(new Property(
                                classModel, 
                                columnName, 
                                propertyName, 
                                typeModel, 
                                new String[0], 
                                false));
                    }
                }finally{
                    columns.close();
                }
                serialize(classModel);
            }
        }finally{
            tables.close();    
        }
    }

    private String toPropertyName(String columnName){
        return columnName.substring(0,1).toLowerCase() + toCamelCase(columnName.substring(1));
    }
    
    private String toClassName(String tableName) {
        return tableName.substring(0,1).toUpperCase() + toCamelCase(tableName.substring(1));
    }
    
    private String toCamelCase(String str){
        StringBuilder builder = new StringBuilder(str.length());
        for (int i = 0; i < str.length(); i++){
            if (str.charAt(i) == '_'){
                builder.append(Character.toUpperCase(str.charAt(i+1)));
                i += 1;
            }else{
                builder.append(Character.toLowerCase(str.charAt(i)));
            }
        }
        return builder.toString();
    }

    private void serialize(EntityType type) {
        String path = packageName.replace('.', '/') + "/" + type.getSimpleName() + ".java";
        try {                        
            Writer writer = writerFor(new File(targetFolder, path));
            try{                
                serializer.serialize(type, serializerConfig, new JavaWriter(writer));    
            }finally{
                writer.close();
            }            
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }    
    }
    
    private static Writer writerFor(File file) {
        if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
            System.err.println("Folder " + file.getParent() + " could not be created");
        }
        try {
            return new OutputStreamWriter(new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}
