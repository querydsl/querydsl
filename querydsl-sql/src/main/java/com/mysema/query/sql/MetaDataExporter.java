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

import com.mysema.codegen.CodeWriter;
import com.mysema.codegen.JavaWriter;
import com.mysema.codegen.ScalaWriter;
import com.mysema.codegen.model.ClassType;
import com.mysema.codegen.model.SimpleType;
import com.mysema.codegen.model.Type;
import com.mysema.codegen.model.TypeCategory;
import com.mysema.commons.lang.Assert;
import com.mysema.query.codegen.CodegenModule;
import com.mysema.query.codegen.EntityType;
import com.mysema.query.codegen.Property;
import com.mysema.query.codegen.QueryTypeFactory;
import com.mysema.query.codegen.Serializer;
import com.mysema.query.codegen.SimpleSerializerConfig;
import com.mysema.query.codegen.TypeMappings;
import com.mysema.query.sql.support.ForeignKeyData;
import com.mysema.query.sql.support.InverseForeignKeyData;
import com.mysema.query.sql.support.NotNullImpl;
import com.mysema.query.sql.support.PrimaryKeyData;
import com.mysema.query.sql.support.SizeImpl;

/**
 * MetadataExporter exports JDBC metadata to Querydsl query types
 *
 * @author tiwe
 */
public class MetaDataExporter {

    private static final Logger logger = LoggerFactory.getLogger(MetaDataExporter.class);

    private static final int COLUMN_NAME = 4;

    private static final int COLUMN_TYPE = 5;

    private static final int COLUMN_SIZE = 7;

    private static final int COLUMN_NULLABLE = 11;

    private static final int SCHEMA_NAME = 2;

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

    private final SQLCodegenModule module = new SQLCodegenModule();

    private final Set<String> classes = new HashSet<String>();

    private File targetFolder;

    @Nullable
    private String beanPackageName;

    @Nullable
    private String schemaPattern, tableNamePattern;

    @Nullable
    private Serializer beanSerializer;

    private boolean createScalaSources = false;

    private final Map<EntityType, Type> entityToWrapped = new HashMap<EntityType, Type>();

    private Serializer serializer;

    private TypeMappings typeMappings;

    private QueryTypeFactory queryTypeFactory;

    private NamingStrategy namingStrategy;

    private Configuration configuration;

    private KeyDataFactory keyDataFactory;

    private boolean validationAnnotations = true;

    public MetaDataExporter(){}

    protected EntityType createEntityType(@Nullable String schemaName, String tableName, final String className) {
        EntityType classModel;

        if (beanSerializer == null){
            String simpleName = module.getPrefix() + className + module.getSuffix();
            Type classTypeModel = new SimpleType(TypeCategory.ENTITY, module.getPackageName() + "." + simpleName,  module.getPackageName(), simpleName, false, false);
            classModel = new EntityType(classTypeModel);
            typeMappings.register(classModel, classModel);

        }else{
            String simpleName = module.getBeanPrefix() + className + module.getBeanSuffix();
            Type classTypeModel = new SimpleType(TypeCategory.ENTITY, beanPackageName + "." + simpleName, beanPackageName, simpleName, false, false);
            classModel = new EntityType(classTypeModel);
            Type mappedType = queryTypeFactory.create(classModel);
            entityToWrapped.put(classModel, mappedType);
            typeMappings.register(classModel, mappedType);
        }

        if (schemaName != null){
            classModel.addAnnotation(new SchemaImpl(schemaName));
        }

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

    /**
     * Export the tables based on the given database metadata
     *
     * @param md
     * @throws SQLException
     */
    public void export(DatabaseMetaData md) throws SQLException {
        typeMappings = module.get(TypeMappings.class);
        queryTypeFactory = module.get(QueryTypeFactory.class);
        serializer = module.get(Serializer.class);
        namingStrategy = module.get(NamingStrategy.class);
        configuration = module.get(Configuration.class);

        if (beanPackageName == null){
            beanPackageName =  module.getPackageName();
        }

        if (beanSerializer == null){
            keyDataFactory = new KeyDataFactory(namingStrategy,  module.getPackageName(), module.getPrefix(), module.getSuffix());
        }else{
            keyDataFactory = new KeyDataFactory(namingStrategy, beanPackageName, module.getBeanPrefix(), module.getBeanSuffix());
        }

        ResultSet tables = md.getTables(null, schemaPattern, tableNamePattern, null);
        try{
            while (tables.next()) {
                handleTable(md, tables);
            }
        }finally{
            tables.close();
        }
    }

    Set<String> getClasses() {
        return classes;
    }

    private void handleColumn(EntityType classModel, String tableName, ResultSet columns) throws SQLException {
        String columnName = columns.getString(COLUMN_NAME);
        String propertyName = namingStrategy.getPropertyName(columnName, classModel);
        Class<?> clazz = configuration.getJavaType(columns.getInt(COLUMN_TYPE), tableName, columnName);
        TypeCategory fieldType = TypeCategory.get(clazz.getName());
        if (Number.class.isAssignableFrom(clazz)){
            fieldType = TypeCategory.NUMERIC;
        }else if (Enum.class.isAssignableFrom(clazz)){
            fieldType = TypeCategory.ENUM;
        }
        Type typeModel = new ClassType(fieldType, clazz);
        Property property = createProperty(classModel, columnName, propertyName, typeModel);
        property.addAnnotation(new ColumnImpl(namingStrategy.normalizeColumnName(columnName)));
        if (validationAnnotations){
            int nullable = columns.getInt(COLUMN_NULLABLE);
            if (nullable == DatabaseMetaData.columnNoNulls){
                property.addAnnotation(new NotNullImpl());
            }
            int size = columns.getInt(COLUMN_SIZE);
            if (size > 0 && clazz.equals(String.class)){
                property.addAnnotation(new SizeImpl(0, size));
            }
        }
        classModel.addProperty(property);
    }

    private void handleTable(DatabaseMetaData md, ResultSet tables) throws SQLException {
        String schemaName = tables.getString(SCHEMA_NAME);
        String tableName = tables.getString(TABLE_NAME);
        String className = namingStrategy.getClassName(tableName);
        EntityType classModel = createEntityType(schemaName, tableName, className);

        // collect primary keys
        Map<String,PrimaryKeyData> primaryKeyData = keyDataFactory.getPrimaryKeys(md, schemaPattern, tableName);
        if (!primaryKeyData.isEmpty()){
            classModel.getData().put(PrimaryKeyData.class, primaryKeyData.values());
        }

        // collect foreign keys
        Map<String,ForeignKeyData> foreignKeyData = keyDataFactory.getImportedKeys(md, schemaPattern, tableName);
        if (!foreignKeyData.isEmpty()){
            classModel.getData().put(ForeignKeyData.class, foreignKeyData.values());
        }

        // collect inverse foreign keys
        Map<String,InverseForeignKeyData> inverseForeignKeyData = keyDataFactory.getExportedKeys(md, schemaPattern, tableName);
        if (!inverseForeignKeyData.isEmpty()){
            classModel.getData().put(InverseForeignKeyData.class, inverseForeignKeyData.values());
        }

        // collect columns
        ResultSet columns = md.getColumns(null, schemaPattern, tableName, null);
        try{
            while (columns.next()) {
                handleColumn(classModel, tableName, columns);
            }
        }finally{
            columns.close();
        }

        // serialize model
        serialize(classModel);

        logger.info("Exported " + tableName + " successfully");
    }

    private void serialize(EntityType type) {
        try {
            String fileSuffix = createScalaSources ? ".scala" : ".java";

            if (beanSerializer != null){
                String path = beanPackageName.replace('.', '/') + "/" + type.getSimpleName() + fileSuffix;
                write(beanSerializer, path, type);

                String otherPath = entityToWrapped.get(type).getFullName().replace('.', '/') + fileSuffix;
                write(serializer, otherPath, type);
            }else{
                String path =  module.getPackageName().replace('.', '/') + "/" + type.getSimpleName() + fileSuffix;
                write(serializer, path, type);
            }

        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private void write(Serializer serializer, String path, EntityType type) throws IOException {
        File targetFile = new File(targetFolder, path);
        classes.add(targetFile.getPath());
        Writer w = writerFor(targetFile);
        try{
            CodeWriter writer = createScalaSources ? new ScalaWriter(w) : new JavaWriter(w);
            serializer.serialize(type, SimpleSerializerConfig.DEFAULT, writer);
        }finally{
            w.close();
        }
    }

    /**
     * Set the schema pattern filter to be used
     *
     * @param schemaPattern a schema name pattern; must match the schema name
     *        as it is stored in the database; "" retrieves those without a schema;
     *        <code>null</code> means that the schema name should not be used to narrow
     *        the search (default: null)
     */
    public void setSchemaPattern(@Nullable String schemaPattern) {
        this.schemaPattern = schemaPattern;
    }

    /**
     * Set the table name pattern filter to be used
     *
    * @param tableNamePattern a table name pattern; must match the
    *        table name as it is stored in the database (default: null)
    */
    public void setTableNamePattern(@Nullable String tableNamePattern) {
        this.tableNamePattern = tableNamePattern;
    }

    /**
     * Override the configuration
     *
     * @param configuration override configuration for custom type mappings etc
     */
    public void setConfiguration(Configuration configuration) {
        Assert.notNull(configuration, "configuration");
        module.bind(Configuration.class, configuration);
    }

    /**
     * Set true to create Scala sources instead of Java sources
     *
     * @param createScalaSources whether to create Scala sources (default: false)
     */
    public void setCreateScalaSources(boolean createScalaSources) {
        this.createScalaSources = createScalaSources;
    }

    /**
     * Set the target folder
     *
     * @param targetFolder target source folder to create the sources into (e.g. target/generated-sources/java)
     */
    public void setTargetFolder(File targetFolder) {
        Assert.notNull(targetFolder, "targetFolder");
        this.targetFolder = targetFolder;
    }

    /**
     * Set the package name
     *
     * @param packageName package name for sources
     */
    public void setPackageName(String packageName) {
        Assert.notNull(packageName, "packageName");
        module.bind(SQLCodegenModule.PACKAGE_NAME, packageName);
    }

    /**
     * Override the bean package name (default: packageName)
     *
     * @param beanPackageName
     */
    public void setBeanPackageName(@Nullable String beanPackageName) {
        this.beanPackageName = beanPackageName;
    }

    /**
     * Override the name prefix for the classes (default: Q)
     *
     * @param namePrefix name prefix for query-types (default: Q)
     */
    public void setNamePrefix(String namePrefix) {
        Assert.notNull(namePrefix, "namePrefix");
        module.bind(CodegenModule.PREFIX, namePrefix);
    }

    /**
     * Override the name suffix for the classes (default: "")
     *
     * @param nameSuffix name suffix for query-types (default: "")
     */
    public void setNameSuffix(String nameSuffix) {
        Assert.notNull(nameSuffix, "nameSuffix");
        module.bind(CodegenModule.SUFFIX, nameSuffix);
    }

    /**
     * Override the bean prefix for the classes (default: "")
     *
     * @param beanPrefix bean prefix for bean-types (default: "")
     */
    public void setBeanPrefix(String beanPrefix) {
        Assert.notNull(beanPrefix, "beanPrefix");
        module.bind(SQLCodegenModule.BEAN_PREFIX, beanPrefix);
    }

    /**
     * Override the bean suffix for the classes (default: "")
     *
     * @param beanSuffix bean suffix for bean-types (default: "")
     */
    public void setBeanSuffix(String beanSuffix) {
        Assert.notNull(beanSuffix, "beanSuffix");
        module.bind(SQLCodegenModule.BEAN_SUFFIX, beanSuffix);
    }

    /**
     * Override the NamingStrategy (default: new DefaultNamingStrategy())
     *
     * @param namingStrategy namingstrategy to override (default: new DefaultNamingStrategy())
     */
    public void setNamingStrategy(NamingStrategy namingStrategy) {
        Assert.notNull(namingStrategy, "namingStrategy");
        module.bind(NamingStrategy.class, namingStrategy);
    }

    /**
     * Set the Bean serializer to create bean types as well
     *
     * @param beanSerializer serializer for JavaBeans (default: null)
     */
    public void setBeanSerializer(@Nullable Serializer beanSerializer) {
        this.beanSerializer = beanSerializer;
    }

    /**
     * @param innerClassesForKeys
     */
    public void setInnerClassesForKeys(boolean innerClassesForKeys) {
        module.bind(SQLCodegenModule.INNER_CLASSES_FOR_KEYS, innerClassesForKeys);
    }

    /**
     * @param serializerClass
     */
    public void setSerializerClass(Class<? extends Serializer> serializerClass){
        Assert.notNull(serializerClass, "serializerClass");
        module.bind(Serializer.class, serializerClass);
    }

    /**
     * @param typeMappings
     */
    public void setTypeMappings(TypeMappings typeMappings){
        Assert.notNull(typeMappings, "typeMappings");
        module.bind(TypeMappings.class, typeMappings);
    }

    /**
     * @param validationAnnotations
     */
    public void setValidationAnnotations(boolean validationAnnotations){
        this.validationAnnotations = validationAnnotations;
    }

}
