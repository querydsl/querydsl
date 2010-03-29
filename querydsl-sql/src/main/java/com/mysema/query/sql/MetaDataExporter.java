/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql;

import static com.mysema.util.Symbols.NEW;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysema.commons.lang.Assert;
import com.mysema.query.codegen.ClassType;
import com.mysema.query.codegen.EntitySerializer;
import com.mysema.query.codegen.EntityType;
import com.mysema.query.codegen.Method;
import com.mysema.query.codegen.Property;
import com.mysema.query.codegen.Serializer;
import com.mysema.query.codegen.SimpleSerializerConfig;
import com.mysema.query.codegen.SimpleType;
import com.mysema.query.codegen.Type;
import com.mysema.query.codegen.TypeCategory;
import com.mysema.query.codegen.TypeMappings;
import com.mysema.query.codegen.Types;
import com.mysema.util.CodeWriter;
import com.mysema.util.JavaWriter;

/**
 * MetadataExporter exports JDBC metadata to Querydsl query types
 * 
 * @author tiwe
 * @version $Id$
 */
public class MetaDataExporter {
    
    private static final Logger logger = LoggerFactory.getLogger(MetaDataExporter.class);
    
    private static final int COLUMN_NAME = 4;
    
    private static final int COLUMN_TYPE = 5;
    
    private static final int TABLE_NAME = 3;

    private static Writer writerFor(File file) {
        if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
            logger.error("Folder " + file.getParent() + " could not be created");
        }
        try {
            return new OutputStreamWriter(new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    private Set<String> classes = new HashSet<String>();

    private final String namePrefix, targetFolder, packageName;
    
    private final NamingStrategy namingStrategy;
    
    @Nullable
    private final String schemaPattern, tableNamePattern;

    private final TypeMappings typeMappings = new TypeMappings();
    
    private final Serializer serializer = new EntitySerializer(typeMappings){
        @Override 
        protected void introDefaultInstance(CodeWriter writer, EntityType entityType) throws IOException {
            String variableName = namingStrategy.getDefaultVariableName(namePrefix, entityType);
            String alias = namingStrategy.getDefaultAlias(namePrefix, entityType);
            String queryType = typeMappings.getPathType(entityType, entityType, true);            
            writer.publicStaticFinal(queryType, variableName, NEW + queryType + "(\"" + alias + "\")");
        }
    };

    private final SQLTypeMapping sqlTypeMapping = new SQLTypeMapping();

    public MetaDataExporter(
            String namePrefix, 
            String packageName,
            @Nullable String schemaPattern, 
            @Nullable String tableNamePattern,
            String targetFolder) {
        this(namePrefix, packageName, schemaPattern, tableNamePattern,
            targetFolder, new DefaultNamingStrategy());
    }
    
    public MetaDataExporter(String namePrefix, 
            String packageName, 
            @Nullable String schemaPattern, 
            @Nullable String tableNamePattern, 
            String targetFolder,
            NamingStrategy namingStrategy){
        this.namePrefix = Assert.notNull(namePrefix,"namePrefix");
        this.packageName = Assert.notNull(packageName,"packageName");
        this.schemaPattern = schemaPattern;
        this.tableNamePattern = tableNamePattern;
        this.targetFolder = Assert.notNull(targetFolder,"targetFolder");       
        this.namingStrategy = Assert.notNull(namingStrategy,"namingStrategy");
    }

    public void export(DatabaseMetaData md) throws SQLException {
        ResultSet tables = md.getTables(null, schemaPattern, tableNamePattern, null);
        try{
            while (tables.next()) {
                handleTable(md, tables);
            }
        }finally{
            tables.close();    
        }
    }
    
    public Set<String> getClasses() {
        return classes;
    }
    
    private void handleColumn(EntityType classModel, ResultSet columns)
            throws SQLException {
        String columnName = columns.getString(COLUMN_NAME);
        String propertyName = namingStrategy.getPropertyName(columnName, namePrefix, classModel);
        Class<?> clazz = sqlTypeMapping.get(columns.getInt(COLUMN_TYPE));
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
                namingStrategy.getColumnName(columnName), 
                propertyName, 
                typeModel, 
                new String[0], 
                false));
    }
    
    private void handleTable(DatabaseMetaData md, ResultSet tables) throws SQLException {
        String tableName = tables.getString(TABLE_NAME);
        String className = namingStrategy.getClassName(namePrefix, tableName);
        Type classTypeModel = new SimpleType(
                TypeCategory.ENTITY, 
                packageName + "." + className, 
                packageName, 
                className, 
                false);   
        EntityType classModel = new EntityType("", classTypeModel);
        Method wildcard = new Method(classModel, "all", "{0}.*", Types.OBJECTS);
        classModel.addMethod(wildcard);
        classModel.addAnnotation(new TableImpl(namingStrategy.getTableName(tableName)));
        ResultSet columns = md.getColumns(null, schemaPattern, tableName, null);
        try{
            while (columns.next()) {
                handleColumn(classModel, columns);
            }
        }finally{
            columns.close();
        }
        serialize(classModel);
    }

    private void serialize(EntityType type) {
        String path = packageName.replace('.', '/') + "/" + type.getSimpleName() + ".java";
        try {                        
            File targetFile = new File(targetFolder, path);
            classes.add(targetFile.getPath());
            Writer writer = writerFor(targetFile);
            try{                
                serializer.serialize(type, SimpleSerializerConfig.DEFAULT, new JavaWriter(writer));    
            }finally{
                writer.close();
            }            
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }    
    }

}
