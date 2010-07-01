/*
 * Copyright (c) 2010 Mysema Ltd.
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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysema.codegen.JavaWriter;
import com.mysema.commons.lang.Assert;
import com.mysema.query.codegen.*;
import com.mysema.query.sql.support.ForeignKeyData;
import com.mysema.query.sql.support.InverseForeignKeyData;
import com.mysema.query.sql.support.KeyDataFactory;
import com.mysema.query.sql.support.PrimaryKeyData;

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

    private final File targetFolder;

    private final String packageName;

    private final String namePrefix;

    private final NamingStrategy namingStrategy;

    @Nullable
    private final String schemaPattern, tableNamePattern;

    private final Serializer serializer;

    private final SQLTypeMapping sqlTypeMapping = new SQLTypeMapping();
    
    private final KeyDataFactory keyDataFactory = new KeyDataFactory();

    public MetaDataExporter(
            String namePrefix,
            String packageName,
            @Nullable String schemaPattern,
            @Nullable String tableNamePattern,
            File targetFolder) {
    this.namePrefix = Assert.notNull(namePrefix,"namePrefix");
        this.packageName = Assert.notNull(packageName,"packageName");
        this.schemaPattern = schemaPattern;
        this.tableNamePattern = tableNamePattern;
        this.targetFolder = Assert.notNull(targetFolder,"targetFolder");
        this.namingStrategy = new DefaultNamingStrategy();
        this.serializer = new MetaDataSerializer(namePrefix, namingStrategy);
    }

    public MetaDataExporter(String namePrefix,
            String packageName,
            @Nullable String schemaPattern,
            @Nullable String tableNamePattern,
            File targetFolder,
            NamingStrategy namingStrategy,
            MetaDataSerializer serializer){
        this.namePrefix = Assert.notNull(namePrefix,"namePrefix");
        this.packageName = Assert.notNull(packageName,"packageName");
        this.schemaPattern = schemaPattern;
        this.tableNamePattern = tableNamePattern;
        this.targetFolder = Assert.notNull(targetFolder,"targetFolder");
        this.namingStrategy = Assert.notNull(namingStrategy,"namingStrategy");
        this.serializer = Assert.notNull(serializer, "serializer");
    }

    protected EntityType createEntityType(String tableName, String className) {
    Type classTypeModel = new SimpleType(
                TypeCategory.ENTITY,
                packageName + "." + className,
                packageName,
                className,
                false);
        EntityType classModel = new EntityType("", classTypeModel);
        Method wildcard = new Method(classModel, "all", "{0}.*", Types.OBJECTS);
        classModel.addMethod(wildcard);
        classModel.addAnnotation(new TableImpl(namingStrategy.normalizeTableName(tableName)));
    return classModel;
    }

    protected Property createProperty(EntityType classModel, String columnName,
            String propertyName, Type typeModel) {
    return new Property(
                classModel,
                namingStrategy.normalizeColumnName(columnName),
                propertyName,
                typeModel,
                new String[0],
                false);
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

    private void handleColumn(EntityType classModel, ResultSet columns) throws SQLException {
        String columnName = columns.getString(COLUMN_NAME);
        String propertyName = namingStrategy.getPropertyName(columnName, namePrefix, classModel);
        Class<?> clazz = sqlTypeMapping.get(columns.getInt(COLUMN_TYPE));
        if (clazz == null){
            throw new RuntimeException("No java type for " + columns.getString(6));
        }
        TypeCategory fieldType = TypeCategory.get(clazz.getName());
        if (Number.class.isAssignableFrom(clazz)){
            fieldType = TypeCategory.NUMERIC;
        }
        Type typeModel = new ClassType(fieldType, clazz);
        classModel.addProperty(createProperty(classModel, columnName, propertyName, typeModel));
    }

    private void handleTable(DatabaseMetaData md, ResultSet tables) throws SQLException {
        String tableName = tables.getString(TABLE_NAME);
        String className = namingStrategy.getClassName(namePrefix, tableName);
        EntityType classModel = createEntityType(tableName, className);

        // collect primary keys
        Map<String,PrimaryKeyData> primaryKeyData = keyDataFactory.getPrimaryKeys(md, schemaPattern, tableName);
        classModel.getData().put(PrimaryKeyData.class, primaryKeyData.values());
        
        // collect foreign keys
        Map<String,ForeignKeyData> foreignKeyData = keyDataFactory.getImportedKeys(md, schemaPattern, tableName);
        classModel.getData().put(ForeignKeyData.class, foreignKeyData.values());
        
        // collect inverse foreign keys
        Map<String,InverseForeignKeyData> inverseForeignKeyData = keyDataFactory.getExportedKeys(md, schemaPattern, tableName);
        classModel.getData().put(InverseForeignKeyData.class, inverseForeignKeyData.values());

        // collect columns
        ResultSet columns = md.getColumns(null, schemaPattern, tableName, null);
        try{
            while (columns.next()) {
                handleColumn(classModel, columns);
            }
        }finally{
            columns.close();
        }

        // serialize model
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
