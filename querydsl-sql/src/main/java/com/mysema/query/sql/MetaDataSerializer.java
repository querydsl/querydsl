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
import com.mysema.codegen.model.ClassType;
import com.mysema.codegen.model.SimpleType;
import com.mysema.codegen.model.Type;
import com.mysema.query.codegen.EntitySerializer;
import com.mysema.query.codegen.EntityType;
import com.mysema.query.codegen.Property;
import com.mysema.query.codegen.SerializerConfig;
import com.mysema.query.codegen.TypeMappings;
import com.mysema.query.sql.support.ForeignKeyData;
import com.mysema.query.sql.support.InverseForeignKeyData;
import com.mysema.query.sql.support.KeyData;
import com.mysema.query.sql.support.PrimaryKeyData;
import com.mysema.query.types.Expr;

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
        Type queryType = typeMappings.getPathType(entityType, entityType, true);
        writer.publicStaticFinal(queryType, variableName, NEW + queryType.getSimpleName() + "(\"" + alias + "\")");
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
        
        // wildcard
        StringBuilder paths = new StringBuilder();
        for (Property property : model.getProperties()){
            if (paths.length() > 0){
                paths.append(", ");
            }
            paths.append(property.getEscapedName());
        }

        Type type = new ClassType(Expr.class, (Type)null).asArrayType();
        writer.privateFinal(type, "all", "new Expr[]{" + paths.toString() + "}");
        writer.beginPublicMethod(type, "all");
        writer.line("return all;");
        writer.end();

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
        Type queryType = typeMappings.getPathType(model, model, true);
        for (PrimaryKeyData primaryKey : primaryKeys){
            String fieldName = namingStrategy.getPropertyNameForPrimaryKey(primaryKey.getName(), model);
            StringBuilder value = new StringBuilder("new PrimaryKey<"+queryType.getSimpleName()+">(this, ");
            boolean first = true;
            for (String column : primaryKey.getColumns()){
                if (!first){
                    value.append(", ");
                }
                value.append(namingStrategy.getPropertyName(column, namePrefix, model));
                first = false;
            }
            value.append(")");
            Type type = new ClassType(PrimaryKey.class, queryType);
            writer.publicFinal(type, fieldName, value.toString());
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
            Type type = new ClassType(ForeignKey.class, new SimpleType(model.getPackageName()+"."+foreignType,model.getPackageName(),foreignType));
            writer.publicFinal(type, fieldName, value.toString());
        }
    }
}
