/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql;

import static com.mysema.codegen.Symbols.NEW;
import static com.mysema.codegen.Symbols.UNCHECKED;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.mysema.codegen.CodeWriter;
import com.mysema.codegen.model.ClassType;
import com.mysema.codegen.model.SimpleType;
import com.mysema.codegen.model.Type;
import com.mysema.codegen.model.TypeCategory;
import com.mysema.codegen.model.Types;
import com.mysema.query.codegen.EntitySerializer;
import com.mysema.query.codegen.EntityType;
import com.mysema.query.codegen.SerializerConfig;
import com.mysema.query.codegen.TypeMappings;
import com.mysema.query.sql.support.ForeignKeyData;
import com.mysema.query.sql.support.InverseForeignKeyData;
import com.mysema.query.sql.support.KeyData;
import com.mysema.query.sql.support.PrimaryKeyData;
import com.mysema.query.types.Path;

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

    @SuppressWarnings(UNCHECKED)
    protected void introClassHeader(CodeWriter writer, EntityType model) throws IOException {
        Type queryType = typeMappings.getPathType(model, model, true);

        TypeCategory category = model.getOriginalCategory();
        Class<? extends Path> pathType = RelationalPathBase.class;

        for (Annotation annotation : model.getAnnotations()){
            writer.annotation(annotation);
        }        
        writer.beginClass(queryType, new ClassType(category, pathType, model));        
        writer.privateStaticFinal(Types.LONG_P, "serialVersionUID", String.valueOf(model.hashCode()));
    }
    
    @Override
    protected void introDefaultInstance(CodeWriter writer, EntityType entityType) throws IOException {
        String variableName = namingStrategy.getDefaultVariableName(namePrefix, entityType);
        String alias = namingStrategy.getDefaultAlias(namePrefix, entityType);
        Type queryType = typeMappings.getPathType(entityType, entityType, true);
        writer.publicStaticFinal(queryType, variableName, NEW + queryType.getSimpleName() + "(\"" + alias + "\")");
    }

    @Override
    protected void introImports(CodeWriter writer, SerializerConfig config, EntityType model) throws IOException {
        super.introImports(writer, config, model);
        writer.imports(Table.class.getPackage(), List.class.getPackage());        
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
        Collection<InverseForeignKeyData> inverseForeignKeys = (Collection<InverseForeignKeyData>)
            model.getData().get(InverseForeignKeyData.class);
        if (inverseForeignKeys != null){
            serializeForeignKeys(model, writer, inverseForeignKeys, true);
        }
    }
    
    protected void serializePrimaryKeys(EntityType model, CodeWriter writer,
            Collection<PrimaryKeyData> primaryKeys) throws IOException {
        for (PrimaryKeyData primaryKey : primaryKeys){
            String fieldName = namingStrategy.getPropertyNameForPrimaryKey(primaryKey.getName(), model);
            StringBuilder value = new StringBuilder("createPrimaryKey(");
            boolean first = true;
            for (String column : primaryKey.getColumns()){
                if (!first){
                    value.append(", ");
                }
                value.append(namingStrategy.getPropertyName(column, namePrefix, model));
                first = false;
            }
            value.append(")");
            Type type = new ClassType(PrimaryKey.class, model);
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
            if (!model.getPrefix().isEmpty()){
                foreignType = foreignType.substring(namePrefix.length());
            }
            StringBuilder value = new StringBuilder();
            if (inverse){
                value.append("createInvForeignKey(");
            }else{
                value.append("createForeignKey(");    
            }            
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
            Type type = new ClassType(ForeignKey.class, 
                    new SimpleType(model.getPackageName()+"."+foreignType, model.getPackageName(), foreignType));
            writer.publicFinal(type, fieldName, value.toString());
        }
    }
}
