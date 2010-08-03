/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql;

import static com.mysema.codegen.Symbols.NEW;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import com.mysema.codegen.CodeWriter;
import com.mysema.query.codegen.EntitySerializer;
import com.mysema.query.codegen.EntityType;
import com.mysema.query.codegen.SerializerConfig;
import com.mysema.query.codegen.TypeMappings;
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

    @SuppressWarnings("unchecked")
    @Override
    protected void introImports(CodeWriter writer, SerializerConfig config, EntityType model) throws IOException {
        super.introImports(writer, config, model);
        writer.imports(Table.class.getPackage());
        
        boolean multiColumns = false;
        multiColumns |= hasMultiColumnKeys((Collection<ForeignKeyData>) model.getData().get(ForeignKeyData.class));
        multiColumns |= hasMultiColumnKeys((Collection<InverseForeignKeyData>) model.getData().get(InverseForeignKeyData.class));
        if (multiColumns){
            writer.imports(Arrays.class);
        }
    }
        
    private boolean hasMultiColumnKeys(Collection<? extends KeyData> foreignKeys){
        if (foreignKeys != null){
            for (KeyData foreignKey : foreignKeys){
                if (foreignKey.getForeignColumns().size() > 1){
                    return true;
                }
            }
        }
        return false;
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
            serializeForeignKeys(model, writer, foreignKeys, false);
        }
        
        // inverse foreign keys
        Collection<InverseForeignKeyData> inverseForeignKeys = (Collection<InverseForeignKeyData>) model.getData().get(InverseForeignKeyData.class);
        if (inverseForeignKeys != null){
            serializeForeignKeys(model, writer, inverseForeignKeys, true);
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
                value.append(namingStrategy.getPropertyName(column, namePrefix, model));
                first = false;
            }
            value.append(")");
            writer.publicFinal("PrimaryKey<"+queryType+">", fieldName, value.toString());
        }

    }

    protected void serializeForeignKeys(EntityType model, CodeWriter writer,
            Collection<? extends KeyData> foreignKeys, boolean inverse) throws IOException {
        for (KeyData foreignKey : foreignKeys){
            String fieldName;
            if (inverse){
                fieldName = namingStrategy.getPropertyNameForInverseForeignKey(foreignKey.getName(), model);
            }else{
                fieldName = namingStrategy.getPropertyNameForForeignKey(foreignKey.getName(), model);
            }
            String foreignType = namingStrategy.getClassName(namePrefix, foreignKey.getTable());
            StringBuilder value = new StringBuilder();
            value.append("new ForeignKey<"+foreignType+">(this, ");
            if (foreignKey.getForeignColumns().size() == 1){
                value.append(namingStrategy.getPropertyName(foreignKey.getForeignColumns().get(0), namePrefix, model));
                value.append(", \"" + foreignKey.getParentColumns().get(0) + "\"");
            }else{
                StringBuilder local = new StringBuilder();
                StringBuilder foreign = new StringBuilder();
                for (int i = 0; i < foreignKey.getForeignColumns().size(); i++){
                    if (i > 0){
                        local.append(", ");
                        foreign.append(", ");
                    }
                    local.append(namingStrategy.getPropertyName(foreignKey.getForeignColumns().get(0), namePrefix, model));
                    foreign.append("\"" +foreignKey.getParentColumns().get(0) + "\"");
                }
                value.append("Arrays.asList("+local+"), Arrays.asList("+foreign+")");
            }
            value.append(")");
            writer.publicFinal("ForeignKey<"+foreignType+">", fieldName, value.toString());
        }
    }
}
