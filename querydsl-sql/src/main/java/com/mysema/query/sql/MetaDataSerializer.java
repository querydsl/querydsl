/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql;

import static com.mysema.codegen.Symbols.NEW;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

import com.mysema.codegen.CodeWriter;
import com.mysema.commons.lang.Pair;
import com.mysema.query.codegen.EntitySerializer;
import com.mysema.query.codegen.EntityType;
import com.mysema.query.codegen.SerializerConfig;
import com.mysema.query.codegen.TypeMappings;
import com.mysema.query.sql.support.ForeignKeyData;
import com.mysema.query.sql.support.PrimaryKeyData;

/**
 * MetaDataSerializer defines the Query type serialization logic for MetaDataExporter.
 * Subclass this class for customization.
 *
 * @author tiwe
 *
 */
public class MetaDataSerializer extends EntitySerializer {

    private final String namePrefix;

    private final NamingStrategy namingStrategy;

    public MetaDataSerializer(String namePrefix, NamingStrategy namingStrategy) {
    // TODO : supply reserved SQL keywords
        super(new TypeMappings(),Collections.<String>emptyList());
        this.namePrefix = namePrefix;
        this.namingStrategy = namingStrategy;
    }

    @Override
    protected void introDefaultInstance(CodeWriter writer, EntityType entityType) throws IOException {
        String variableName = namingStrategy.getDefaultVariableName(namePrefix, entityType);
        String alias = namingStrategy.getDefaultAlias(namePrefix, entityType);
        String queryType = typeMappings.getPathType(entityType, entityType, true);
        writer.publicStaticFinal(queryType, variableName, NEW + queryType + "(\"" + alias + "\")");
    }

    @Override
    protected void introImports(CodeWriter writer, SerializerConfig config, EntityType model) throws IOException {
        super.introImports(writer, config, model);
        writer.imports(Table.class.getPackage());
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void serializeProperties(EntityType model,  SerializerConfig config, CodeWriter writer) throws IOException {
        super.serializeProperties(model, config, writer);

        // primary keys
        Collection<PrimaryKeyData> primaryKeys = (Collection<PrimaryKeyData>) model.getData().get(PrimaryKeyData.class);
        if (primaryKeys != null){
            serializePrimaryKeys(model, writer, primaryKeys);
        }

        // foreign keys
        Collection<ForeignKeyData> foreignKeys = (Collection<ForeignKeyData>) model.getData().get(ForeignKeyData.class);
        if (foreignKeys != null){
            serializeForeignKeys(model, writer, foreignKeys);
        }
    }

    protected void serializePrimaryKeys(EntityType model, CodeWriter writer,
            Collection<PrimaryKeyData> primaryKeys) throws IOException {
        String queryType = typeMappings.getPathType(model, model, true);
        for (PrimaryKeyData primaryKey : primaryKeys){
            String fieldName = namingStrategy.getPropertyNameForPrimaryKey(primaryKey.getName(), model);
            StringBuilder value = new StringBuilder("new PrimaryKey<"+queryType+">(this, ");
            boolean first = true;
            for (String column : primaryKey.getColumns()){
                if (!first){
                    value.append(", ");
                }
                value.append(namingStrategy.getPropertyNameForPrimaryKey(column, model));
                first = false;
            }
            value.append(")");
            writer.publicFinal("PrimaryKey<"+queryType+">", fieldName, value.toString());
        }

    }

    protected void serializeForeignKeys(EntityType model, CodeWriter writer,
            Collection<ForeignKeyData> foreignKeys) throws IOException {
        for (ForeignKeyData foreignKey : foreignKeys){
            String fieldName = namingStrategy.getPropertyNameForForeignKey(foreignKey.getName(), model);
            String foreignType = namingStrategy.getClassName(namePrefix, foreignKey.getTable());
            StringBuilder value = new StringBuilder();
            value.append("new ForeignKey<"+foreignType+">(this, ");
            if (foreignKey.getColumns().size() == 1){
                Pair<String,String> pair = foreignKey.getColumns().get(0);
                value.append(namingStrategy.getPropertyName(pair.getFirst(), namePrefix, model));
                value.append(", \"" + pair.getSecond() + "\"");
            }else{
                StringBuilder local = new StringBuilder();
                StringBuilder foreign = new StringBuilder();
                for (Pair<String,String> pair : foreignKey.getColumns()){
                    if (local.length() > 0){
                        local.append(", ");
                        foreign.append(", ");
                    }
                    local.append(namingStrategy.getPropertyName(pair.getFirst(), namePrefix, model));
                    foreign.append("\"" + pair.getSecond() + "\"");
                }
                value.append("Arrays.asList("+local+"), Arrays.asList("+foreign+")");
            }
            value.append(")");
            writer.publicFinal("ForeignKey<"+foreignType+">", fieldName, value.toString());
        }
    }
}
