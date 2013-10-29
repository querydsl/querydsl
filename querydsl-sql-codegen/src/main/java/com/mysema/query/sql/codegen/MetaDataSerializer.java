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
package com.mysema.query.sql.codegen;

import static com.mysema.codegen.Symbols.NEW;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import com.mysema.codegen.CodeWriter;
import com.mysema.codegen.model.ClassType;
import com.mysema.codegen.model.SimpleType;
import com.mysema.codegen.model.Type;
import com.mysema.codegen.model.TypeCategory;
import com.mysema.codegen.model.Types;
import com.mysema.query.codegen.EntitySerializer;
import com.mysema.query.codegen.EntityType;
import com.mysema.query.codegen.Property;
import com.mysema.query.codegen.SerializerConfig;
import com.mysema.query.codegen.TypeMappings;
import com.mysema.query.sql.ColumnMetadata;
import com.mysema.query.sql.ForeignKey;
import com.mysema.query.sql.PrimaryKey;
import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.sql.support.ForeignKeyData;
import com.mysema.query.sql.support.InverseForeignKeyData;
import com.mysema.query.sql.support.KeyData;
import com.mysema.query.sql.support.PrimaryKeyData;

/**
 * MetaDataSerializer defines the Query type serialization logic for MetaDataExporter.
 * Subclass this class for customization.
 *
 * @author tiwe
 *
 */
public class MetaDataSerializer extends EntitySerializer {

    private final NamingStrategy namingStrategy;

    private final boolean innerClassesForKeys;

    /**
     * Create a new MetaDataSerializer instance
     *
     * @param namingStrategy naming strategy for table to class and column to property conversion
     * @param innerClassesForKeys wrap key properties into inner classes (default: false)
     * @param schemaToPackage if schema name is appended to package or not
     */
    @Inject
    public MetaDataSerializer(
            TypeMappings typeMappings,
            NamingStrategy namingStrategy,
            @Named(SQLCodegenModule.INNER_CLASSES_FOR_KEYS) boolean innerClassesForKeys) {
        super(typeMappings,Collections.<String>emptyList());
        this.namingStrategy = namingStrategy;
        this.innerClassesForKeys = innerClassesForKeys;
    }

    @Override
    protected void constructorContent(CodeWriter writer, EntityType model) throws IOException {
        writer.line("addMetadata();");
    }

    @Override
    protected void introClassHeader(CodeWriter writer, EntityType model) throws IOException {
        Type queryType = typeMappings.getPathType(model, model, true);

        writer.line("@Generated(\"", getClass().getName(), "\")");

        TypeCategory category = model.getOriginalCategory();
        // serialize annotations only, if no bean types are used
        if (model.equals(queryType)) {
            for (Annotation annotation : model.getAnnotations()) {
                writer.annotation(annotation);
            }
        }
        writer.beginClass(queryType, new ClassType(category, RelationalPathBase.class, model));
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
                }
            }
        }
        if (inverseForeignKeys != null) {
            for (InverseForeignKeyData keyData : inverseForeignKeys) {
                if (keyData.getForeignColumns().size() > 1) {
                    addJavaUtilImport = true;
                }
            }
        }

        if (addJavaUtilImport) {
            writer.imports(List.class.getPackage());
        }

        writer.imports(ColumnMetadata.class);
    }

    @Override
    protected void outro(EntityType model, CodeWriter writer) throws IOException {
        writer.beginPublicMethod(Types.VOID,"addMetadata");
        for (Property property : model.getProperties()) {
            String name = property.getName();
            ColumnMetadata metadata = (ColumnMetadata) property.getData().get("COLUMN");
            StringBuilder columnMeta = new StringBuilder();
            columnMeta.append("ColumnMetadata");
            columnMeta.append(".named(\"" + metadata.getName() + "\")");
            columnMeta.append(".ofType(" + metadata.getJdbcType() + ")");
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
            (Collection<InverseForeignKeyData>)model.getData().get(InverseForeignKeyData.class);

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
                        "new "+primaryKeyType.getSimpleName()+"()");
            }
            if (foreignKeys != null || inverseForeignKeys != null) {
                writer.publicFinal(
                        foreignKeysType,
                        namingStrategy.getForeignKeysVariable(model),
                        "new "+foreignKeysType.getSimpleName()+"()");
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
                value.append(", \"" + foreignKey.getParentColumns().get(0) + "\"");
            } else {
                StringBuilder local = new StringBuilder();
                StringBuilder foreign = new StringBuilder();
                for (int i = 0; i < foreignKey.getForeignColumns().size(); i++) {
                    if (i > 0) {
                        local.append(", ");
                        foreign.append(", ");
                    }
                    local.append(namingStrategy.getPropertyName(foreignKey.getForeignColumns().get(i), model));
                    foreign.append("\"" +foreignKey.getParentColumns().get(i) + "\"");
                }
                value.append("Arrays.asList("+local+"), Arrays.asList("+foreign+")");
            }
            value.append(")");
            Type type = new ClassType(ForeignKey.class, foreignKey.getType());
            writer.publicFinal(type, fieldName, value.toString());
        }
    }

}
