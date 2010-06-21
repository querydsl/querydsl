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
import java.util.HashMap;
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
import com.mysema.query.sql.support.PrimaryKeyData;

/**
 * MetadataExporter exports JDBC metadata to Querydsl query types
 *
 * @author tiwe
 * @version $Id$
 */
public class MetaDataExporter {

    private static final Logger logger = LoggerFactory.getLogger(MetaDataExporter.class);

    private static final int FK_PARENT_TABLE_NAME = 3;

    private static final int FK_PARENT_COLUMN_NAME = 4;

    private static final int FK_FOREIGN_COLUMN_NAME = 8;

    private static final int PK_NAME = 6;

    private static final int PK_COLUMN_NAME = 4;

    private static final int FK_NAME = 12;

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
        ResultSet primaryKeys = md.getPrimaryKeys(null, schemaPattern, tableName);
        Map<String,PrimaryKeyData> primaryKeyData = new HashMap<String,PrimaryKeyData>();
        try{
            while (primaryKeys.next()){
                String name = primaryKeys.getString(PK_NAME);
                String columnName = primaryKeys.getString(PK_COLUMN_NAME);

                PrimaryKeyData data = primaryKeyData.get(name);
                if (data == null){
                    data = new PrimaryKeyData(name);
                    primaryKeyData.put(name, data);
                }
                data.add(columnName);
            }
        }finally{
            primaryKeys.close();
        }

        classModel.getData().put(PrimaryKeyData.class, primaryKeyData.values());

        // collect foreign keys
        ResultSet foreignKeys = md.getCrossReference(null, schemaPattern, null, null, schemaPattern, tableName);
        Map<String,ForeignKeyData> foreignKeyData = new HashMap<String,ForeignKeyData>();
        try{
            while (foreignKeys.next()){
                String name = foreignKeys.getString(FK_NAME);
                String parentTableName = foreignKeys.getString(FK_PARENT_TABLE_NAME);
                String parentColumnName = foreignKeys.getString(FK_PARENT_COLUMN_NAME);
                String foreignColumn = foreignKeys.getString(FK_FOREIGN_COLUMN_NAME);
                ForeignKeyData data = foreignKeyData.get(name);
                if (data == null){
                    data = new ForeignKeyData(name, parentTableName);
                    foreignKeyData.put(name, data);
                }
                data.add(foreignColumn, parentColumnName);
            }
        }finally{
            foreignKeys.close();
        }

        classModel.getData().put(ForeignKeyData.class, foreignKeyData.values());

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
