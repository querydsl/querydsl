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
import com.mysema.query.codegen.Property;
import com.mysema.query.codegen.SerializerConfig;
import com.mysema.query.codegen.TypeMappings;
import com.mysema.query.sql.support.ForeignKeyData;
import com.mysema.query.sql.support.InverseForeignKeyData;
import com.mysema.query.sql.support.KeyData;
import com.mysema.query.sql.support.PrimaryKeyData;
import com.mysema.query.types.Expr;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.BeanPath;

/**
 * MetaDataSerializer defines the Query type serialization logic for MetaDataExporter.
 * Subclass this class for customization.
 *
 * @author tiwe
 *
 */
public class MetaDataSerializer extends EntitySerializer {
    
    private static final Type FOREIGNKEY_TYPE = new ClassType(ForeignKey.class, (Type)null);

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
        Class<? extends Path> pathType = BeanPath.class;

        for (Annotation annotation : model.getAnnotations()){
            writer.annotation(annotation);
        }        
        if (category == TypeCategory.BOOLEAN || category == TypeCategory.STRING){
            writer.beginClass(queryType, new ClassType(pathType), 
                    new ClassType(RelationalPath.class, model));
        }else{
            writer.beginClass(queryType, new ClassType(category,pathType, model),
                    new ClassType(RelationalPath.class, model));    
        }
        
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
        
        // wildcard
        Type type = new ClassType(Expr.class, (Type)null).asArrayType();
        writer.privateField(type, "_all");
        
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
    
    @SuppressWarnings("unchecked")
    @Override
    protected void outro(EntityType model, CodeWriter writer) throws IOException {        
        // wildcard
        StringBuilder paths = new StringBuilder();
        for (Property property : model.getProperties()){
            if (paths.length() > 0){
                paths.append(", ");
            }
            paths.append(property.getEscapedName());
        }
        
        Type type = new ClassType(Expr.class, (Type)null).asArrayType();
        writer.beginPublicMethod(type, "all");
        writer.line("if (_all == null) {");
        writer.line("    _all = ", "new Expr[]{", paths.toString(), "};");
        writer.line("}");
        writer.line("return _all;");
        writer.end();

        // primary keys
        Collection<PrimaryKeyData> primaryKeys = (Collection<PrimaryKeyData>) model.getData().get(PrimaryKeyData.class);
        if (primaryKeys != null && !primaryKeys.isEmpty()){
            PrimaryKeyData primaryKey = primaryKeys.iterator().next();
            writer.beginPublicMethod(new ClassType(PrimaryKey.class, model), "getPrimaryKey");
            writer.line("return ", namingStrategy.getPropertyNameForPrimaryKey(primaryKey.getName(), model), ";");
            writer.end();
        }else{
            writer.beginPublicMethod(new ClassType(PrimaryKey.class, model), "getPrimaryKey");
            writer.line("return null;");
            writer.end();
        }

        // foreign keys
        Collection<ForeignKeyData> foreignKeys = (Collection<ForeignKeyData>) model.getData().get(ForeignKeyData.class);
        writer.beginPublicMethod(new SimpleType(Types.LIST, FOREIGNKEY_TYPE), "getForeignKeys");
        if (foreignKeys != null && !foreignKeys.isEmpty()){
            StringBuilder builder = new StringBuilder();
            for (ForeignKeyData key : foreignKeys){
                if (builder.length() > 0){
                    builder.append(", ");
                }
                builder.append(namingStrategy.getPropertyNameForForeignKey(key.getName(), model));
            }
            writer.line("return Arrays.<ForeignKey<?>>asList(",builder.toString(),");");
        }else{
            writer.line("return Collections.<ForeignKey<?>>emptyList();");
        }
        writer.end();
        
        // inverse foreign keys
        Collection<InverseForeignKeyData> inverseForeignKeys = (Collection<InverseForeignKeyData>)
            model.getData().get(InverseForeignKeyData.class);
        writer.beginPublicMethod(new SimpleType(Types.LIST, FOREIGNKEY_TYPE), "getInverseForeignKeys");
        if (inverseForeignKeys != null && !inverseForeignKeys.isEmpty()){
            StringBuilder builder = new StringBuilder();
            for (InverseForeignKeyData key : inverseForeignKeys){
                if (builder.length() > 0){
                    builder.append(", ");
                }
                builder.append(namingStrategy.getPropertyNameForInverseForeignKey(key.getName(), model));
            }
            writer.line("return Arrays.<ForeignKey<?>>asList(",builder.toString(),");");
        }else{
            writer.line("return Collections.<ForeignKey<?>>emptyList();");
        }
        writer.end();
        
        super.outro(model, writer);
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
            Type type = new ClassType(ForeignKey.class, 
                    new SimpleType(model.getPackageName()+"."+foreignType,model.getPackageName(),foreignType));
            writer.publicFinal(type, fieldName, value.toString());
        }
    }
}
