/*
 * Copyright 2011, Mysema Ltd
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

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import com.google.common.io.Files;
import com.mysema.codegen.CodeWriter;
import com.mysema.codegen.JavaWriter;
import com.mysema.codegen.ScalaWriter;
import com.mysema.codegen.model.ClassType;
import com.mysema.codegen.model.SimpleType;
import com.mysema.codegen.model.Type;
import com.mysema.codegen.model.TypeCategory;
import com.querydsl.codegen.*;
import com.querydsl.sql.*;
import com.querydsl.sql.codegen.support.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MetadataExporter exports JDBC metadata to Querydsl querydsl types
 *
 * <p>Example</p>
 *
 * <pre>
 * MetaDataExporter exporter = new MetaDataExporter();
 * exporter.setPackageName("com.example.domain");
 * exporter.setTargetFolder(new File("target/generated-sources/java"));
 * exporter.export(connection.getMetaData());
 * </pre>
 *
 * @author tiwe
 */
public class MetaDataExporter {

    private static final Logger logger = LoggerFactory.getLogger(MetaDataExporter.class);

    private final SQLTemplatesRegistry sqlTemplatesRegistry = new SQLTemplatesRegistry();

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

    private boolean columnAnnotations = false;

    private boolean validationAnnotations = false;

    private boolean schemaToPackage = false;

    private String sourceEncoding = "UTF-8";

    private boolean lowerCase = false;

    private boolean exportTables = true;

    private boolean exportViews = true;

    private boolean exportAll = false;

    private boolean exportPrimaryKeys = true;

    private boolean exportForeignKeys = true;

    private boolean spatial = false;
    
    @Nullable
    private String tableTypesToExport; 

    public MetaDataExporter() {}

    protected EntityType createEntityType(@Nullable String schemaName, String tableName,
            final String className) {
        EntityType classModel;

        if (beanSerializer == null) {
            String packageName = normalizePackage(module.getPackageName(), schemaName);
            String simpleName = module.getPrefix() + className + module.getSuffix();
            Type classTypeModel = new SimpleType(TypeCategory.ENTITY,
                    packageName + "." + simpleName,  packageName, simpleName, false, false);
            classModel = new EntityType(classTypeModel);
            typeMappings.register(classModel, classModel);

        } else {
            String beanPackage = normalizePackage(beanPackageName, schemaName);
            String simpleName = module.getBeanPrefix() + className + module.getBeanSuffix();
            Type classTypeModel = new SimpleType(TypeCategory.ENTITY,
                    beanPackage + "." + simpleName, beanPackage, simpleName, false, false);
            classModel = new EntityType(classTypeModel);

            Type mappedType = queryTypeFactory.create(classModel);
            entityToWrapped.put(classModel, mappedType);
            typeMappings.register(classModel, mappedType);
        }

        classModel.getData().put("schema", schemaName);
        classModel.getData().put("table", tableName);
        return classModel;
    }


    private String normalizePackage(String packageName, @Nullable String schemaName) {
        if (schemaToPackage && schemaName != null) {
            return namingStrategy.appendSchema(packageName, schemaName);
        } else {
            return packageName;
        }
    }

    protected Property createProperty(EntityType classModel, String normalizedColumnName,
            String propertyName, Type typeModel) {
        return new Property(
                classModel,
                propertyName,
                propertyName,
                typeModel,
                Collections.<String>emptyList(),
                false);
    }

    /**
     * Export the tables based on the given database metadata
     *
     * @param md
     * @throws SQLException
     */
    public void export(DatabaseMetaData md) throws SQLException {
        if (beanPackageName == null) {
            beanPackageName =  module.getPackageName();
        }
        module.bind(SQLCodegenModule.BEAN_PACKAGE_NAME, beanPackageName);

        if (spatial) {
            SpatialSupport.addSupport(module);
        }

        typeMappings = module.get(TypeMappings.class);
        queryTypeFactory = module.get(QueryTypeFactory.class);
        serializer = module.get(Serializer.class);
        beanSerializer = module.get(Serializer.class, SQLCodegenModule.BEAN_SERIALIZER);
        namingStrategy = module.get(NamingStrategy.class);
        configuration = module.get(Configuration.class);

        SQLTemplates templates = sqlTemplatesRegistry.getTemplates(md);
        if (templates != null) {
            configuration.setTemplates(templates);
        } else {
            logger.info("Found no specific dialect for " + md.getDatabaseProductName());
        }

        if (beanSerializer == null) {
            keyDataFactory = new KeyDataFactory(namingStrategy,  module.getPackageName(),
                    module.getPrefix(), module.getSuffix(), schemaToPackage);
        } else {
            keyDataFactory = new KeyDataFactory(namingStrategy, beanPackageName,
                    module.getBeanPrefix(), module.getBeanSuffix(), schemaToPackage);
        }

        String[] typesArray = null;

        if (tableTypesToExport != null && !tableTypesToExport.isEmpty()) {
            List<String> types = new ArrayList<String>();
            for (String tableType : tableTypesToExport.split(",")) {
                types.add(tableType.trim());
            }
            typesArray = types.toArray(new String[types.size()]);
        } else if (!exportAll) {
            List<String> types = new ArrayList<String>(2);
            if (exportTables) {
                types.add("TABLE");
            }
            if (exportViews) {
                types.add("VIEW");
            }
            typesArray = types.toArray(new String[types.size()]);
        }

        if (tableNamePattern != null && tableNamePattern.contains(",")) {
            for (String table : tableNamePattern.split(",")) {
                ResultSet tables = md.getTables(null, schemaPattern, table.trim(), typesArray);
                try{
                    while (tables.next()) {
                        handleTable(md, tables);
                    }
                }finally{
                    tables.close();
                }
            }
        } else {
            ResultSet tables = md.getTables(null, schemaPattern, tableNamePattern, typesArray);
            try{
                while (tables.next()) {
                    handleTable(md, tables);
                }
            }finally{
                tables.close();
            }
        }

    }

    Set<String> getClasses() {
        return classes;
    }

    private void handleColumn(EntityType classModel, String tableName, ResultSet columns) throws SQLException {
        String columnName = normalize(columns.getString("COLUMN_NAME"));
        String normalizedColumnName = namingStrategy.normalizeColumnName(columnName);
        int columnType = columns.getInt("DATA_TYPE");
        String typeName = columns.getString("TYPE_NAME");
        Number columnSize = (Number) columns.getObject("COLUMN_SIZE");
        Number columnDigits = (Number) columns.getObject("DECIMAL_DIGITS");
        int columnIndex = columns.getInt("ORDINAL_POSITION");
        int nullable = columns.getInt("NULLABLE");

        String propertyName = namingStrategy.getPropertyName(normalizedColumnName, classModel);
        Class<?> clazz = configuration.getJavaType(columnType,
                typeName,
                columnSize != null ? columnSize.intValue() : 0,
                columnDigits != null ? columnDigits.intValue() : 0,
                tableName, columnName);
        if (clazz == null) {
            throw new IllegalStateException("Found no mapping for " + columnType + " (" + tableName + "." + columnName + " " + typeName + ")");
        }
        TypeCategory fieldType = TypeCategory.get(clazz.getName());
        if (Number.class.isAssignableFrom(clazz)) {
            fieldType = TypeCategory.NUMERIC;
        } else if (Enum.class.isAssignableFrom(clazz)) {
            fieldType = TypeCategory.ENUM;
        }
        Type typeModel = new ClassType(fieldType, clazz);
        Property property = createProperty(classModel, normalizedColumnName, propertyName, typeModel);
        ColumnMetadata column = ColumnMetadata.named(normalizedColumnName).ofType(columnType).withIndex(columnIndex);
        if (nullable == DatabaseMetaData.columnNoNulls) {
            column = column.notNull();
        }
        if (columnSize != null) {
            column = column.withSize(columnSize.intValue());
        }
        if (columnDigits != null) {
            column = column.withDigits(columnDigits.intValue());
        }
        property.getData().put("COLUMN", column);

        if (columnAnnotations) {
            property.addAnnotation(new ColumnImpl(normalizedColumnName));
        }
        if (validationAnnotations) {
            if (nullable == DatabaseMetaData.columnNoNulls) {
                property.addAnnotation(new NotNullImpl());
            }
            int size = columns.getInt("COLUMN_SIZE");
            if (size > 0 && clazz.equals(String.class)) {
                property.addAnnotation(new SizeImpl(0, size));
            }
        }
        classModel.addProperty(property);
    }

    private void handleTable(DatabaseMetaData md, ResultSet tables) throws SQLException {
        String catalog = tables.getString("TABLE_CAT");
        String schema = tables.getString("TABLE_SCHEM");
        String schemaName = normalize(tables.getString("TABLE_SCHEM"));
        String tableName = normalize(tables.getString("TABLE_NAME"));
        String normalizedTableName = namingStrategy.normalizeTableName(tableName);
        String className = namingStrategy.getClassName(normalizedTableName);
        EntityType classModel = createEntityType(schemaName, normalizedTableName, className);

        if (exportPrimaryKeys) {
            // collect primary keys
            Map<String,PrimaryKeyData> primaryKeyData = keyDataFactory
                    .getPrimaryKeys(md, catalog, schema, tableName);
            if (!primaryKeyData.isEmpty()) {
                classModel.getData().put(PrimaryKeyData.class, primaryKeyData.values());
            }
        }

        if (exportForeignKeys) {
            // collect foreign keys
            Map<String,ForeignKeyData> foreignKeyData = keyDataFactory
                    .getImportedKeys(md, catalog, schema, tableName);
            if (!foreignKeyData.isEmpty()) {
                classModel.getData().put(ForeignKeyData.class, foreignKeyData.values());
            }

            // collect inverse foreign keys
            Map<String,InverseForeignKeyData> inverseForeignKeyData = keyDataFactory
                    .getExportedKeys(md, catalog, schema, tableName);
            if (!inverseForeignKeyData.isEmpty()) {
                classModel.getData().put(InverseForeignKeyData.class, inverseForeignKeyData.values());
            }
        }

        // collect columns
        ResultSet columns = md.getColumns(catalog, schema, tableName.replace("/", "//"), null);
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

    private String normalize(String str) {
        if (lowerCase && str != null) {
            return str.toLowerCase();
        } else {
            return str;
        }
    }

    private void serialize(EntityType type) {
        try {
            String fileSuffix = createScalaSources ? ".scala" : ".java";

            if (beanSerializer != null) {
                String packageName = normalizePackage(beanPackageName, (String)type.getData().get("schema"));
                String path = packageName.replace('.', '/') + "/" + type.getSimpleName() + fileSuffix;
                write(beanSerializer, path, type);

                String otherPath = entityToWrapped.get(type).getFullName().replace('.', '/') + fileSuffix;
                write(serializer, otherPath, type);
            } else {
                String packageName = normalizePackage(module.getPackageName(), (String)type.getData().get("schema"));
                String path =  packageName.replace('.', '/') + "/" + type.getSimpleName() + fileSuffix;
                write(serializer, path, type);
            }

        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private void write(Serializer serializer, String path, EntityType type) throws IOException {
        File targetFile = new File(targetFolder, path);
        classes.add(targetFile.getPath());
        StringWriter w = new StringWriter();
        CodeWriter writer = createScalaSources ? new ScalaWriter(w) : new JavaWriter(w);
        serializer.serialize(type, SimpleSerializerConfig.DEFAULT, writer);

        // conditional creation
        boolean generate = true;
        byte[] bytes = w.toString().getBytes(sourceEncoding);
        if (targetFile.exists() && targetFile.length() == bytes.length) {
            String str = Files.toString(targetFile, Charset.forName(sourceEncoding));
            if (str.equals(w.toString())) {
                generate = false;
            }
        } else {
            targetFile.getParentFile().mkdirs();
        }

        if (generate) {
            Files.write(bytes, targetFile);
        }
    }


    /**
     * Set the schema pattern filter to be used
     *
     * @param schemaPattern a schema name pattern; must match the schema name
     *        as it is stored in the database; "" retrieves those without a schema;
     *        {@code null} means that the schema name should not be used to narrow
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
     * @param targetFolder target source folder to create the sources into
     *        (e.g. target/generated-sources/java)
     */
    public void setTargetFolder(File targetFolder) {
        this.targetFolder = targetFolder;
    }

    /**
     * Set the package name
     *
     * @param packageName package name for sources
     */
    public void setPackageName(String packageName) {
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
     * @param namePrefix name prefix for querydsl-types (default: Q)
     */
    public void setNamePrefix(String namePrefix) {
        module.bind(CodegenModule.PREFIX, namePrefix);
    }

    /**
     * Override the name suffix for the classes (default: "")
     *
     * @param nameSuffix name suffix for querydsl-types (default: "")
     */
    public void setNameSuffix(String nameSuffix) {
        module.bind(CodegenModule.SUFFIX, nameSuffix);
    }

    /**
     * Override the bean prefix for the classes (default: "")
     *
     * @param beanPrefix bean prefix for bean-types (default: "")
     */
    public void setBeanPrefix(String beanPrefix) {
        module.bind(SQLCodegenModule.BEAN_PREFIX, beanPrefix);
    }

    /**
     * Override the bean suffix for the classes (default: "")
     *
     * @param beanSuffix bean suffix for bean-types (default: "")
     */
    public void setBeanSuffix(String beanSuffix) {
        module.bind(SQLCodegenModule.BEAN_SUFFIX, beanSuffix);
    }

    /**
     * Override the NamingStrategy (default: new DefaultNamingStrategy())
     *
     * @param namingStrategy namingstrategy to override (default: new DefaultNamingStrategy())
     */
    public void setNamingStrategy(NamingStrategy namingStrategy) {
        module.bind(NamingStrategy.class, namingStrategy);
    }

    /**
     * Set the Bean serializer to create bean types as well
     *
     * @param beanSerializer serializer for JavaBeans (default: null)
     */
    public void setBeanSerializer(@Nullable Serializer beanSerializer) {
        module.bind(SQLCodegenModule.BEAN_SERIALIZER, beanSerializer);
    }

    /**
     * Set the Bean serializer class to create bean types as well
     *
     * @param beanSerializerClass serializer for JavaBeans (default: null)
     */
    public void setBeanSerializerClass(Class<? extends Serializer> beanSerializerClass) {
        module.bind(SQLCodegenModule.BEAN_SERIALIZER, beanSerializerClass);
    }

    /**
     * @param innerClassesForKeys
     */
    public void setInnerClassesForKeys(boolean innerClassesForKeys) {
        module.bind(SQLCodegenModule.INNER_CLASSES_FOR_KEYS, innerClassesForKeys);
    }

    /**
     * @param columnComparatorClass
     */
    public void setColumnComparatorClass(Class<? extends Comparator<Property>> columnComparatorClass) {
        module.bind(SQLCodegenModule.COLUMN_COMPARATOR, columnComparatorClass);
    }

    /**
     * @param serializerClass
     */
    public void setSerializerClass(Class<? extends Serializer> serializerClass) {
        module.bind(Serializer.class, serializerClass);
    }

    /**
     * @param typeMappings
     */
    public void setTypeMappings(TypeMappings typeMappings) {
        module.bind(TypeMappings.class, typeMappings);
    }

    /**
     * @param columnAnnotations
     */
    public void setColumnAnnotations(boolean columnAnnotations) {
        this.columnAnnotations = columnAnnotations;
    }

    /**
     * @param validationAnnotations
     */
    public void setValidationAnnotations(boolean validationAnnotations) {
        this.validationAnnotations = validationAnnotations;
    }

    /**
     * @param sourceEncoding
     */
    public void setSourceEncoding(String sourceEncoding) {
        this.sourceEncoding = sourceEncoding;
    }

    /**
     * @param schemaToPackage
     */
    public void setSchemaToPackage(boolean schemaToPackage) {
        this.schemaToPackage = schemaToPackage;
        module.bind(SQLCodegenModule.SCHEMA_TO_PACKAGE, schemaToPackage);
    }

    /**
     * @param lowerCase
     */
    public void setLowerCase(boolean lowerCase) {
        this.lowerCase = lowerCase;
    }

    /**
     * @param exportTables
     */
    public void setExportTables(boolean exportTables) {
        this.exportTables = exportTables;
    }

    /**
     * @param exportViews
     */
    public void setExportViews(boolean exportViews) {
        this.exportViews = exportViews;
    }

    /**
     * @param exportAll
     */
    public void setExportAll(boolean exportAll) {
        this.exportAll = exportAll;
    }

    /**
     * @param exportPrimaryKeys
     */
    public void setExportPrimaryKeys(boolean exportPrimaryKeys) {
        this.exportPrimaryKeys = exportPrimaryKeys;
    }

    /**
     * @param exportForeignKeys
     */
    public void setExportForeignKeys(boolean exportForeignKeys) {
        this.exportForeignKeys = exportForeignKeys;
    }

    /**
     * Set the java imports
     *
     * @param imports
     *            java imports array
     */
    public void setImports(String[] imports) {
        module.bind(CodegenModule.IMPORTS, new HashSet<String>(Arrays.asList(imports)));
    }

    /**
     * @param spatial
     */
    public void setSpatial(boolean spatial) {
        this.spatial = spatial;
    }

    /**
     * @param tableTypesToExport
     */
    public void setTableTypesToExport(String tableTypesToExport) {
        this.tableTypesToExport = tableTypesToExport;
    }

}
