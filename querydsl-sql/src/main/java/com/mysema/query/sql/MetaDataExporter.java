/*
 * Copyright (c) 2009 Mysema Ltd.
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

import org.apache.commons.lang.StringUtils;

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
    
    private Set<String> classes = new HashSet<String>();
    
    private final String namePrefix, targetFolder, packageName;

    private final NamingStrategy namingStrategy;
    
    @Nullable
    private final String schemaPattern, tableNamePattern;

    private final SQLTypeMapping typeMapping = new SQLTypeMapping();
    
    private final TypeMappings typeMappings = new TypeMappings();
    
    private final Serializer serializer = new EntitySerializer(typeMappings){
        @Override 
        protected void introDefaultInstance(CodeWriter writer, EntityType model) throws IOException {
            String simpleName = model.getUncapSimpleName();
            if (namePrefix.length() > 0){
                // TODO : improve this
                simpleName = StringUtils.uncapitalize(model.getUncapSimpleName().substring(namePrefix.length()));
            }
            String queryType = typeMappings.getPathType(model, model, true);            
            writer.publicStaticFinal(queryType, simpleName, NEW + queryType + "(\"" + simpleName + "\")");
        }
    };

    public MetaDataExporter(
            String namePrefix, 
            String packageName,
            @Nullable String schemaPattern, 
            @Nullable String tableNamePattern,
            String targetFolder) {
        this(namePrefix, packageName, schemaPattern, tableNamePattern,
            targetFolder, new DefaultNamingStrategy());
    }

    /**
     * Create a new MetaDataExporter instance
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
            String targetFolder,
            NamingStrategy namingStrategy){
        this.namePrefix = namePrefix;
        this.packageName = packageName;
        this.schemaPattern = schemaPattern;
        this.tableNamePattern = tableNamePattern;
        this.targetFolder = targetFolder;       
        this.namingStrategy = namingStrategy;
    }

    public void export(DatabaseMetaData md) throws SQLException {
        Assert.notNull(targetFolder, "targetFolder needs to be set");
        Assert.notNull(packageName, "packageName needs to be set");

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
        String columnName = columns.getString(4);
        String propertyName = namingStrategy.toPropertyName(columnName);
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
    
    private void handleTable(DatabaseMetaData md, ResultSet tables) throws SQLException {
        String tableName = tables.getString(3);
        String className = namingStrategy.toClassName(namePrefix, tableName);
        Type classTypeModel = new SimpleType(
                TypeCategory.ENTITY, 
                packageName + "." + className, 
                packageName, 
                className, 
                false);   
        EntityType classModel = new EntityType("", classTypeModel);
        Method wildcard = new Method(classModel, "all", "{0}.*", Types.OBJECTS);
        classModel.addMethod(wildcard);
        classModel.addAnnotation(new TableImpl(tableName));
        ResultSet columns = md.getColumns(null, schemaPattern, tables.getString(3), null);
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
