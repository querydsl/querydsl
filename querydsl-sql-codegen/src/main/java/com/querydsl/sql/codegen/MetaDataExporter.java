/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
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

import com.querydsl.codegen.CodegenModule;
import com.querydsl.codegen.EntityType;
import com.querydsl.codegen.GeneratedAnnotationResolver;
import com.querydsl.codegen.Property;
import com.querydsl.codegen.QueryTypeFactory;
import com.querydsl.codegen.Serializer;
import com.querydsl.codegen.SimpleSerializerConfig;
import com.querydsl.codegen.TypeMappings;
import com.querydsl.codegen.utils.CodeWriter;
import com.querydsl.codegen.utils.JavaWriter;
import com.querydsl.codegen.utils.ScalaWriter;
import com.querydsl.codegen.utils.model.ClassType;
import com.querydsl.codegen.utils.model.SimpleType;
import com.querydsl.codegen.utils.model.Type;
import com.querydsl.codegen.utils.model.TypeCategory;
import com.querydsl.sql.ColumnImpl;
import com.querydsl.sql.ColumnMetadata;
import com.querydsl.sql.Configuration;
import com.querydsl.sql.SQLTemplates;
import com.querydsl.sql.SQLTemplatesRegistry;
import com.querydsl.sql.SchemaAndTable;
import com.querydsl.sql.codegen.support.ForeignKeyData;
import com.querydsl.sql.codegen.support.InverseForeignKeyData;
import com.querydsl.sql.codegen.support.NotNullImpl;
import com.querydsl.sql.codegen.support.PrimaryKeyData;
import com.querydsl.sql.codegen.support.SizeImpl;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.logging.Logger;

/**
 * {@code MetadataExporter} exports JDBC metadata to Querydsl query types
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

    private static final Logger logger = Logger.getLogger(MetaDataExporter.class.getName());

    private final SQLTemplatesRegistry sqlTemplatesRegistry = new SQLTemplatesRegistry();

    private final SQLCodegenModule module = new SQLCodegenModule();

    private final Set<String> classes = new HashSet<String>();

    private File targetFolder;

    private File beansTargetFolder;

    @Nullable
    private String beanPackageName;

    @Nullable
    private String schemaPattern, tableNamePattern, catalogPattern;

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

    private boolean exportDirectForeignKeys = true;

    private boolean exportInverseForeignKeys = true;

    private boolean spatial = false;

    @Nullable
    private String tableTypesToExport;

    public MetaDataExporter() { }

    protected EntityType createEntityType(SchemaAndTable schemaAndTable,
            final String className) {
        EntityType classModel;

        if (beanSerializer == null) {
            String packageName = normalizePackage(module.getPackageName(), schemaAndTable);
            String simpleName = module.getPrefix() + className + module.getSuffix();
            Type classTypeModel = new SimpleType(TypeCategory.ENTITY,
                    packageName + "." + simpleName,  packageName, simpleName, false, false);
            classModel = new EntityType(classTypeModel, module.get(Function.class, CodegenModule.VARIABLE_NAME_FUNCTION_CLASS));
            typeMappings.register(classModel, classModel);

        } else {
            String beanPackage = normalizePackage(beanPackageName, schemaAndTable);
            String simpleName = module.getBeanPrefix() + className + module.getBeanSuffix();
            Type classTypeModel = new SimpleType(TypeCategory.ENTITY,
                    beanPackage + "." + simpleName, beanPackage, simpleName, false, false);
            classModel = new EntityType(classTypeModel, module.get(Function.class, CodegenModule.VARIABLE_NAME_FUNCTION_CLASS));

            Type mappedType = queryTypeFactory.create(classModel);
            entityToWrapped.put(classModel, mappedType);
            typeMappings.register(classModel, mappedType);
        }

        classModel.getData().put("schema", schemaAndTable.getSchema());
        classModel.getData().put("table", schemaAndTable.getTable());
        return classModel;
    }


    private String normalizePackage(String packageName, SchemaAndTable schemaAndTable) {
        String rval = packageName;
        if (schemaToPackage) {
            rval = namingStrategy.getPackage(rval, schemaAndTable);
        }
        return rval;
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
     * @param md database metadata
     * @throws SQLException
     */
    public void export(DatabaseMetaData md) throws SQLException {
        if (beanPackageName == null) {
            beanPackageName =  module.getPackageName();
        }
        if (beansTargetFolder == null) {
            beansTargetFolder = targetFolder;
        }
        module.bind(SQLCodegenModule.BEAN_PACKAGE_NAME, beanPackageName);
        module.loadExtensions();

        classes.clear();
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
            typesArray = types.toArray(new String[0]);
        } else if (!exportAll) {
            List<String> types = new ArrayList<String>(2);
            if (exportTables) {
                types.add("TABLE");
            }
            if (exportViews) {
                types.add("VIEW");
            }
            typesArray = types.toArray(new String[0]);
        }

        List<String> catalogs = patternAsList(catalogPattern);
        List<String> schemas = patternAsList(schemaPattern);
        List<String> tables = patternAsList(tableNamePattern);

        for (String catalog : catalogs) {
            catalog = trimIfNonNull(catalog);
            for (String schema : schemas) {
                schema = trimIfNonNull(schema);
                for (String table : tables) {
                    table = trimIfNonNull(table);
                    handleTables(md, catalog, schema, table, typesArray);
                }
            }
        }
    }

    private String trimIfNonNull(String input) {
        return input != null ? input.trim() : null;
    }

    /**
     * Splits the input on ',' if non-null and a ',' is present.
     * Returns a singletonList of null if null
     */
    private List<String> patternAsList(@Nullable String input) {
        if (input != null && input.contains(",")) {
            return Arrays.asList(input.split(","));
        } else {
            return Collections.singletonList(input);
        }
    }

    private void handleTables(DatabaseMetaData md, String catalogPattern, String schemaPattern, String tablePattern, String[] types) throws SQLException {
        try (ResultSet tables = md.getTables(catalogPattern, schemaPattern, tablePattern, types)) {
            while (tables.next()) {
                handleTable(md, tables);
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
        String columnDefaultValue = columns.getString("COLUMN_DEF");

        String propertyName = namingStrategy.getPropertyName(normalizedColumnName, classModel);
        Class<?> clazz = configuration.getJavaType(columnType,
                typeName,
                columnSize != null ? columnSize.intValue() : 0,
                columnDigits != null ? columnDigits.intValue() : 0,
                tableName, columnName);
        if (clazz == null) {
            clazz = Object.class;
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
            if (nullable == DatabaseMetaData.columnNoNulls && columnDefaultValue == null) {
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

        String normalizedSchemaName = namingStrategy.normalizeSchemaName(schemaName);
        String normalizedTableName = namingStrategy.normalizeTableName(tableName);

        SchemaAndTable schemaAndTable = new SchemaAndTable(
            normalizedSchemaName, normalizedTableName);

        if (!namingStrategy.shouldGenerateClass(schemaAndTable)) {
            return;
        }

        String className = namingStrategy.getClassName(schemaAndTable);
        EntityType classModel = createEntityType(schemaAndTable, className);

        if (exportPrimaryKeys) {
            // collect primary keys
            Map<String,PrimaryKeyData> primaryKeyData = keyDataFactory
                    .getPrimaryKeys(md, catalog, schema, tableName);
            if (!primaryKeyData.isEmpty()) {
                classModel.getData().put(PrimaryKeyData.class, primaryKeyData.values());
            }
        }

        if (exportForeignKeys) {
            if (exportDirectForeignKeys) {
                // collect foreign keys
                Map<String,ForeignKeyData> foreignKeyData = keyDataFactory
                        .getImportedKeys(md, catalog, schema, tableName);
                if (!foreignKeyData.isEmpty()) {
                    Collection<ForeignKeyData> foreignKeysToGenerate = new LinkedHashSet<ForeignKeyData>();
                    for (ForeignKeyData fkd : foreignKeyData.values()) {
                        if (namingStrategy.shouldGenerateForeignKey(schemaAndTable, fkd)) {
                            foreignKeysToGenerate.add(fkd);
                        }
                    }

                    if (!foreignKeysToGenerate.isEmpty()) {
                        classModel.getData().put(ForeignKeyData.class, foreignKeysToGenerate);
                    }
                }
            }

            if (exportInverseForeignKeys) {
                // collect inverse foreign keys
                Map<String,InverseForeignKeyData> inverseForeignKeyData = keyDataFactory
                        .getExportedKeys(md, catalog, schema, tableName);
                if (!inverseForeignKeyData.isEmpty()) {
                    classModel.getData().put(InverseForeignKeyData.class, inverseForeignKeyData.values());
                }
            }
        }

        // collect columns
        try (ResultSet columns = md.getColumns(catalog, schema, tableName.replace("/", "//"), null)) {
            while (columns.next()) {
                handleColumn(classModel, tableName, columns);
            }
        }

        // serialize model
        serialize(classModel, schemaAndTable);

        logger.info("Exported " + tableName + " successfully");
    }

    private String normalize(String str) {
        if (lowerCase && str != null) {
            return str.toLowerCase();
        } else {
            return str;
        }
    }

    private void serialize(EntityType type, SchemaAndTable schemaAndTable) {
        try {
            String fileSuffix = createScalaSources ? ".scala" : ".java";

            if (beanSerializer != null) {
                String packageName = normalizePackage(beanPackageName, schemaAndTable);
                String path = packageName.replace('.', '/') + "/" + type.getSimpleName() + fileSuffix;
                write(beanSerializer, new File(beansTargetFolder, path), type);

                String otherPath = entityToWrapped.get(type).getFullName().replace('.', '/') + fileSuffix;
                write(serializer, new File(targetFolder, otherPath), type);
            } else {
                String packageName = normalizePackage(module.getPackageName(), schemaAndTable);
                String path =  packageName.replace('.', '/') + "/" + type.getSimpleName() + fileSuffix;
                write(serializer, new File(targetFolder, path), type);
            }

        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private void write(Serializer serializer, File targetFile, EntityType type) throws IOException {
        if (!classes.add(targetFile.getPath())) {
            throw new IllegalStateException("Attempted to write multiple times to " +
                    targetFile.getPath() + ", please check your configuration");
        }
        StringWriter w = new StringWriter();
        CodeWriter writer = createScalaSources ? new ScalaWriter(w) : new JavaWriter(w);
        serializer.serialize(type, SimpleSerializerConfig.DEFAULT, writer);

        // conditional creation
        boolean generate = true;
        byte[] bytes = w.toString().getBytes(sourceEncoding);
        if (targetFile.exists() && targetFile.length() == bytes.length) {
            String str = new String(Files.readAllBytes(targetFile.toPath()), Charset.forName(sourceEncoding));
            if (str.equals(w.toString())) {
                generate = false;
            }
        } else {
            targetFile.getParentFile().mkdirs();
        }

        if (generate) {
            Files.write(targetFile.toPath(), bytes);
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
     * a catalog name; must match the catalog name as it
     *      is stored in the database; "" retrieves those without a catalog;
     *      <code>null</code> means that the catalog name should not be used to narrow
     *      the search
     */
    public void setCatalogPattern(@Nullable String catalogPattern) {
        this.catalogPattern = catalogPattern;
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
     * Set the target folder for beans
     *
     * <p>defaults to the targetFolder value</p>
     *
     * @param targetFolder target source folder to create the bean sources into
     */
    public void setBeansTargetFolder(File targetFolder) {
        this.beansTargetFolder = targetFolder;
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
     * @param beanPackageName package name for bean sources
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
     * @param namingStrategy naming strategy to override (default: new DefaultNamingStrategy())
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
     * Set whether inner classes should be created for keys
     *
     * @param innerClassesForKeys
     */
    public void setInnerClassesForKeys(boolean innerClassesForKeys) {
        module.bind(SQLCodegenModule.INNER_CLASSES_FOR_KEYS, innerClassesForKeys);
    }

    /**
     * Set the column comparator class
     *
     * @param columnComparatorClass
     */
    public void setColumnComparatorClass(Class<? extends Comparator<Property>> columnComparatorClass) {
        module.bind(SQLCodegenModule.COLUMN_COMPARATOR, columnComparatorClass);
    }

    /**
     * Set the serializer class
     *
     * @param serializerClass
     */
    public void setSerializerClass(Class<? extends Serializer> serializerClass) {
        module.bind(Serializer.class, serializerClass);
    }

    /**
     * Set the type mappings to use
     *
     * @param typeMappings
     */
    public void setTypeMappings(TypeMappings typeMappings) {
        module.bind(TypeMappings.class, typeMappings);
    }

    /**
     * Set whether column annotations should be created
     *
     * @param columnAnnotations
     */
    public void setColumnAnnotations(boolean columnAnnotations) {
        this.columnAnnotations = columnAnnotations;
    }

    /**
     * Set whether validation annotations should be created
     *
     * @param validationAnnotations
     */
    public void setValidationAnnotations(boolean validationAnnotations) {
        this.validationAnnotations = validationAnnotations;
    }

    /**
     * Set the source encoding
     *
     * @param sourceEncoding
     */
    public void setSourceEncoding(String sourceEncoding) {
        this.sourceEncoding = sourceEncoding;
    }

    /**
     * Set whether schema names should be appended to the package name.
     *
     * <p><b>!!! Important !!!</b><i> {@link NamingStrategy#getPackage(String, SchemaAndTable)}
     * will be invoked only if <code>schemaToPackage</code> is set to <code>true</code>.</i></p>
     *
     * @deprecated This flag will not be necessary in the future because the generated package name
     * can be controlled in method {@link NamingStrategy#getPackage(String, SchemaAndTable)}.
     *
     * @param schemaToPackage
     */
    @Deprecated
    public void setSchemaToPackage(boolean schemaToPackage) {
        this.schemaToPackage = schemaToPackage;
        module.bind(SQLCodegenModule.SCHEMA_TO_PACKAGE, schemaToPackage);
    }

    /**
     * Set whether names should be normalized to lowercase
     *
     * @param lowerCase
     */
    public void setLowerCase(boolean lowerCase) {
        this.lowerCase = lowerCase;
    }

    /**
     * Set whether tables should be exported
     *
     * @param exportTables
     */
    public void setExportTables(boolean exportTables) {
        this.exportTables = exportTables;
    }

    /**
     * Set whether views should be exported
     *
     * @param exportViews
     */
    public void setExportViews(boolean exportViews) {
        this.exportViews = exportViews;
    }

    /**
     * Set whether all table types should be exported
     *
     * @param exportAll
     */
    public void setExportAll(boolean exportAll) {
        this.exportAll = exportAll;
    }

    /**
     * Set whether primary keys should be exported
     *
     * @param exportPrimaryKeys
     */
    public void setExportPrimaryKeys(boolean exportPrimaryKeys) {
        this.exportPrimaryKeys = exportPrimaryKeys;
    }

    /**
     * Set whether foreign keys should be exported
     *
     * @param exportForeignKeys
     */
    public void setExportForeignKeys(boolean exportForeignKeys) {
        this.exportForeignKeys = exportForeignKeys;
    }

    /**
     * Set whether direct foreign keys should be exported
     *
     * @param exportDirectForeignKeys
     */
    public void setExportDirectForeignKeys(boolean exportDirectForeignKeys) {
        this.exportDirectForeignKeys = exportDirectForeignKeys;
    }

    /**
     * Set whether inverse foreign keys should be exported
     *
     * @param exportInverseForeignKeys
     */
    public void setExportInverseForeignKeys(boolean exportInverseForeignKeys) {
        this.exportInverseForeignKeys = exportInverseForeignKeys;
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
     * Set whether spatial type support should be used
     *
     * @param spatial
     */
    public void setSpatial(boolean spatial) {
        this.spatial = spatial;
    }

    /**
     * Set the table types to export as a comma separated string
     *
     * @param tableTypesToExport
     */
    public void setTableTypesToExport(String tableTypesToExport) {
        this.tableTypesToExport = tableTypesToExport;
    }

    /**
     * Set the fully qualified class name of the "generated" annotation added ot the generated sources
     *
     * @param generatedAnnotationClass the fully qualified class name of the <em>Single-Element Annotation</em> (with {@code String} element) to be used on
     *                                 the generated sources, or {@code null} (defaulting to {@code javax.annotation.Generated} or
     *                                {@code javax.annotation.processing.Generated} depending on the java version).
     * @see <a href="https://docs.oracle.com/javase/specs/jls/se8/html/jls-9.html#jls-9.7.3">Single-Element Annotation</a>
     */
    public void setGeneratedAnnotationClass(@Nullable String generatedAnnotationClass) {
        module.bind(CodegenModule.GENERATED_ANNOTATION_CLASS, GeneratedAnnotationResolver.resolve(generatedAnnotationClass));
    }

}
