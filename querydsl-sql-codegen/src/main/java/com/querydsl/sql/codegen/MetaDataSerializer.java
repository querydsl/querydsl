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

import com.querydsl.codegen.*;
import com.querydsl.codegen.utils.CodeWriter;
import com.querydsl.codegen.utils.model.ClassType;
import com.querydsl.codegen.utils.model.Parameter;
import com.querydsl.codegen.utils.model.SimpleType;
import com.querydsl.codegen.utils.model.Type;
import com.querydsl.codegen.utils.model.TypeCategory;
import com.querydsl.codegen.utils.model.Types;
import com.querydsl.sql.ColumnMetadata;
import com.querydsl.sql.ForeignKey;
import com.querydsl.sql.PrimaryKey;
import com.querydsl.sql.codegen.support.ForeignKeyData;
import com.querydsl.sql.codegen.support.InverseForeignKeyData;
import com.querydsl.sql.codegen.support.KeyData;
import com.querydsl.sql.codegen.support.PrimaryKeyData;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.querydsl.codegen.utils.Symbols.COMMA;
import static com.querydsl.codegen.utils.Symbols.NEW;
import static com.querydsl.codegen.utils.Symbols.SUPER;

/**
 * {@code MetaDataSerializer} defines the Query type serialization logic for {@link MetaDataExporter}.
 * Extend this class for customization.
 *
 * @author tiwe
 *
 */
public class MetaDataSerializer extends DefaultEntitySerializer {

    private static final Map<Integer, String> typeConstants = new HashMap<>();

    static {
        try {
            for (Field field : java.sql.Types.class.getDeclaredFields()) {
                if (field.getType().equals(Integer.TYPE)) {
                    typeConstants.put(field.getInt(null), field.getName());
                }
            }
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }

    }

    private final NamingStrategy namingStrategy;

    private final boolean innerClassesForKeys;

    private final Set<String> imports;

    private final Comparator<Property> columnComparator;

    private final Class<?> entityPathType;

    /**
     * Create a new {@code MetaDataSerializer} instance
     *
     * @param namingStrategy           naming strategy for table to class and column to property conversion
     * @param innerClassesForKeys      wrap key properties into inner classes (default: false)
     * @param imports                  java user imports
     * @param generatedAnnotationClass the fully qualified class name of the <em>Single-Element Annotation</em> (with {@code String} element) to be used on the generated classes.
     * @see <a href="https://docs.oracle.com/javase/specs/jls/se8/html/jls-9.html#jls-9.7.3">Single-Element Annotation</a>
     */
    @Inject
    public MetaDataSerializer(
            TypeMappings typeMappings,
            NamingStrategy namingStrategy,
            @Named(SQLCodegenModule.INNER_CLASSES_FOR_KEYS) boolean innerClassesForKeys,
            @Named(SQLCodegenModule.IMPORTS) Set<String> imports,
            @Named(SQLCodegenModule.COLUMN_COMPARATOR) Comparator<Property> columnComparator,
            @Named(SQLCodegenModule.ENTITYPATH_TYPE) Class<?> entityPathType,
            @Named(SQLCodegenModule.GENERATED_ANNOTATION_CLASS) GeneratedAnnotationClass generatedAnnotationClass) {
        super(typeMappings, Collections.<String>emptyList(), generatedAnnotationClass);
        this.namingStrategy = namingStrategy;
        this.innerClassesForKeys = innerClassesForKeys;
        this.imports = new HashSet<String>(imports);
        this.columnComparator = columnComparator;
        this.entityPathType = entityPathType;
    }

    /**
     * Create a new {@code MetaDataSerializer} instance
     *
     * @param namingStrategy      naming strategy for table to class and column to property conversion
     * @param innerClassesForKeys wrap key properties into inner classes (default: false)
     * @param imports             java user imports
     */
    public MetaDataSerializer(
            TypeMappings typeMappings,
            NamingStrategy namingStrategy,
            boolean innerClassesForKeys,
            Set<String> imports,
            Comparator<Property> columnComparator,
            Class<?> entityPathType) {
        this(typeMappings, namingStrategy, innerClassesForKeys, imports, columnComparator, entityPathType, GeneratedAnnotationResolver.resolveDefault());
    }

    @Override
    protected void constructorsForVariables(CodeWriter writer, EntityType model) throws IOException {
        super.constructorsForVariables(writer, model);

        String localName = writer.getRawName(model);
        String genericName = writer.getGenericName(true, model);

        if (!localName.equals(genericName)) {
            writer.suppressWarnings("all");
        }
        writer.beginConstructor(new Parameter("variable", Types.STRING),
                                new Parameter("schema", Types.STRING),
                                new Parameter("table", Types.STRING));
        writer.line(SUPER,"(", writer.getClassConstant(localName) + COMMA
                + "forVariable(variable), schema, table);");
        constructorContent(writer, model);
        writer.end();

        writer.beginConstructor(new Parameter("variable", Types.STRING),
                                new Parameter("schema", Types.STRING));
        writer.line(SUPER, "(", writer.getClassConstant(localName), COMMA,
                "forVariable(variable), schema, \"", model.getData().get("table").toString(), "\");");
        constructorContent(writer, model);
        writer.end();
    }

    @Override
    protected void constructorContent(CodeWriter writer, EntityType model) throws IOException {
        writer.line("addMetadata();");
    }

    @Override
    protected void introClassHeader(CodeWriter writer, EntityType model) throws IOException {
        Type queryType = typeMappings.getPathType(model, model, true);

        writer.line("@", generatedAnnotationClass.buildAnnotation(GeneratedAnnotationClass.Context.of(getClass().getName())));

        TypeCategory category = model.getOriginalCategory();
        // serialize annotations only, if no bean types are used
        if (model.equals(queryType)) {
            for (Annotation annotation : model.getAnnotations()) {
                writer.annotation(annotation);
            }
        }
        writer.beginClass(queryType, new ClassType(category, entityPathType, model));
        writer.privateStaticFinal(Types.LONG_P, "serialVersionUID", String.valueOf(model.hashCode()));
    }

    @Override
    protected String getAdditionalConstructorParameter(EntityType model) {
        StringBuilder builder = new StringBuilder();
        if (model.getData().containsKey("schema")) {
            builder.append(", \"").append(model.getData().get("schema")).append("\"");
        } else {
            builder.append(", null");
        }
        builder.append(", \"").append(model.getData().get("table")).append("\"");
        return builder.toString();
    }

    @Override
    protected void introDefaultInstance(CodeWriter writer, EntityType entityType, String defaultName) throws IOException {
        String variableName = !defaultName.isEmpty() ? defaultName : namingStrategy.getDefaultVariableName(entityType);
        String alias = namingStrategy.getDefaultAlias(entityType);
        Type queryType = typeMappings.getPathType(entityType, entityType, true);
        writer.publicStaticFinal(queryType, variableName, NEW + queryType.getSimpleName() + "(\"" + alias + "\")");
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void introImports(CodeWriter writer, SerializerConfig config, EntityType model) throws IOException {
        super.introImports(writer, config, model);

        Collection<ForeignKeyData> foreignKeys = (Collection<ForeignKeyData>)
                model.getData().get(ForeignKeyData.class);
        Collection<InverseForeignKeyData> inverseForeignKeys = (Collection<InverseForeignKeyData>)
                model.getData().get(InverseForeignKeyData.class);
        boolean addJavaUtilImport = false;
        if (foreignKeys != null) {
            for (ForeignKeyData keyData : foreignKeys) {
                if (keyData.getForeignColumns().size() > 1) {
                    addJavaUtilImport = true;
                    break;
                }
            }
        }
        if (inverseForeignKeys != null) {
            for (InverseForeignKeyData keyData : inverseForeignKeys) {
                if (keyData.getForeignColumns().size() > 1) {
                    addJavaUtilImport = true;
                    break;
                }
            }
        }

        if (addJavaUtilImport) {
            writer.imports(List.class.getPackage());
        }

        writer.imports(ColumnMetadata.class, java.sql.Types.class);

        if (!entityPathType.getPackage().equals(ColumnMetadata.class.getPackage())) {
            writer.imports(entityPathType);
        }

        writeUserImports(writer);
    }

    protected void writeUserImports(CodeWriter writer) throws IOException {
        Set<String> packages = new HashSet<String>();
        Set<String> classes = new HashSet<String>();

        for (String javaImport : imports) {
            //true if the character next to the dot is an upper case or if no dot is found (-1+1=0) the first character
            boolean isClass = Character.isUpperCase(javaImport.charAt(javaImport.lastIndexOf(".") + 1));
            if (isClass) {
                classes.add(javaImport);
            } else {
                packages.add(javaImport);
            }
        }

        String[] marker = new String[]{};
        writer.importPackages(packages.toArray(marker));
        writer.importClasses(classes.toArray(marker));
    }

    @Override
    protected void outro(EntityType model, CodeWriter writer) throws IOException {
        writer.beginPublicMethod(Types.VOID,"addMetadata");
        List<Property> properties = new ArrayList<>(model.getProperties());
        if (columnComparator != null) {
            properties.sort(columnComparator);
        }
        for (Property property : properties) {
            String name = property.getEscapedName();
            ColumnMetadata metadata = (ColumnMetadata) property.getData().get("COLUMN");
            StringBuilder columnMeta = new StringBuilder();
            columnMeta.append("ColumnMetadata");
            columnMeta.append(".named(\"").append(metadata.getName()).append("\")");
            columnMeta.append(".withIndex(").append(metadata.getIndex()).append(")");
            if (metadata.hasJdbcType()) {
                String type = String.valueOf(metadata.getJdbcType());
                if (typeConstants.containsKey(metadata.getJdbcType())) {
                    type = "Types." + typeConstants.get(metadata.getJdbcType());
                }
                columnMeta.append(".ofType(").append(type).append(")");
            }
            if (metadata.hasSize()) {
                columnMeta.append(".withSize(").append(metadata.getSize()).append(")");
            }
            if (metadata.getDigits() > 0) {
                columnMeta.append(".withDigits(").append(metadata.getDigits()).append(")");
            }
            if (!metadata.isNullable()) {
                columnMeta.append(".notNull()");
            }
            writer.line("addMetadata(", name, ", ", columnMeta.toString(), ");");
        }
        writer.end();

        super.outro(model, writer);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void serializeProperties(EntityType model,  SerializerConfig config,
            CodeWriter writer) throws IOException {
        Collection<PrimaryKeyData> primaryKeys =
            (Collection<PrimaryKeyData>) model.getData().get(PrimaryKeyData.class);
        Collection<ForeignKeyData> foreignKeys =
            (Collection<ForeignKeyData>) model.getData().get(ForeignKeyData.class);
        Collection<InverseForeignKeyData> inverseForeignKeys =
            (Collection<InverseForeignKeyData>) model.getData().get(InverseForeignKeyData.class);

        if (innerClassesForKeys) {
            Type primaryKeyType = new SimpleType(namingStrategy.getPrimaryKeysClassName());
            Type foreignKeysType = new SimpleType(namingStrategy.getForeignKeysClassName());

            // primary keys
            if (primaryKeys != null) {
                writer.beginClass(primaryKeyType);
                serializePrimaryKeys(model, writer, primaryKeys);
                writer.end();
            }

            // foreign keys
            if (foreignKeys != null || inverseForeignKeys != null) {
                writer.beginClass(foreignKeysType);
                if (foreignKeys != null) {
                    serializeForeignKeys(model, writer, foreignKeys, false);
                }
                // inverse foreign keys
                if (inverseForeignKeys != null) {
                    serializeForeignKeys(model, writer, inverseForeignKeys, true);
                }
                writer.end();
            }

            super.serializeProperties(model, config, writer);

            if (primaryKeys != null) {
                writer.publicFinal(
                        primaryKeyType,
                        namingStrategy.getPrimaryKeysVariable(model),
                        "new " + primaryKeyType.getSimpleName() + "()");
            }
            if (foreignKeys != null || inverseForeignKeys != null) {
                writer.publicFinal(
                        foreignKeysType,
                        namingStrategy.getForeignKeysVariable(model),
                        "new " + foreignKeysType.getSimpleName() + "()");
            }

        } else {

            super.serializeProperties(model, config, writer);

            // primary keys
            if (primaryKeys != null) {
                serializePrimaryKeys(model, writer, primaryKeys);
            }

            // foreign keys
            if (foreignKeys != null) {
                serializeForeignKeys(model, writer, foreignKeys, false);
            }

            // inverse foreign keys
            if (inverseForeignKeys != null) {
                serializeForeignKeys(model, writer, inverseForeignKeys, true);
            }
        }
    }

    @Override
    protected void customField(EntityType model, Property field, SerializerConfig config,
                               CodeWriter writer) throws IOException {
        Type queryType = typeMappings.getPathType(field.getType(), model, false);
        if (queryType.getPackageName().startsWith("com.querydsl")) {
            String localRawName = writer.getRawName(field.getType());
            serialize(model, field, queryType, writer, "create" + field.getType().getSimpleName(),
                    writer.getClassConstant(localRawName));
        } else {
            super.customField(model, field, config, writer);
        }
    }

    protected void serializePrimaryKeys(EntityType model, CodeWriter writer,
            Collection<PrimaryKeyData> primaryKeys) throws IOException {
        for (PrimaryKeyData primaryKey : primaryKeys) {
            String fieldName = namingStrategy.getPropertyNameForPrimaryKey(primaryKey.getName(), model);
            StringBuilder value = new StringBuilder("createPrimaryKey(");
            boolean first = true;
            for (String column : primaryKey.getColumns()) {
                if (!first) {
                    value.append(", ");
                }
                value.append(namingStrategy.getPropertyName(column, model));
                first = false;
            }
            value.append(")");
            Type type = new ClassType(PrimaryKey.class, model);
            writer.publicFinal(type, fieldName, value.toString());
        }

    }

    protected void serializeForeignKeys(EntityType model, CodeWriter writer,
            Collection<? extends KeyData> foreignKeys, boolean inverse) throws IOException {
        for (KeyData foreignKey : foreignKeys) {
            String fieldName;
            if (inverse) {
                fieldName = namingStrategy.getPropertyNameForInverseForeignKey(foreignKey.getName(), model);
            } else {
                fieldName = namingStrategy.getPropertyNameForForeignKey(foreignKey.getName(), model);
            }

            StringBuilder value = new StringBuilder();
            if (inverse) {
                value.append("createInvForeignKey(");
            } else {
                value.append("createForeignKey(");
            }
            if (foreignKey.getForeignColumns().size() == 1) {
                value.append(namingStrategy.getPropertyName(foreignKey.getForeignColumns().get(0), model));
                value.append(", \"").append(foreignKey.getParentColumns().get(0)).append("\"");
            } else {
                StringBuilder local = new StringBuilder();
                StringBuilder foreign = new StringBuilder();
                for (int i = 0; i < foreignKey.getForeignColumns().size(); i++) {
                    if (i > 0) {
                        local.append(", ");
                        foreign.append(", ");
                    }
                    local.append(namingStrategy.getPropertyName(foreignKey.getForeignColumns().get(i), model));
                    foreign.append("\"").append(foreignKey.getParentColumns().get(i)).append("\"");
                }
                value.append("Arrays.asList(").append(local).append("), Arrays.asList(").append(foreign).append(")");
            }
            value.append(")");
            Type type = new ClassType(ForeignKey.class, foreignKey.getType());
            writer.publicFinal(type, fieldName, value.toString());
        }
    }

}
