/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import java.io.IOException;
import java.io.Writer;

import net.jcip.annotations.Immutable;

@Immutable
public class EntitySerializer extends AbstractSerializer{
    
    private final boolean embeddable;
    
    public EntitySerializer(boolean embeddable){
        this.embeddable = embeddable;
    }
    
    public void serialize(ClassModel model, Writer writer) throws IOException{
        // intro
        intro(model, writer);
        
        // fields
        for (FieldModel field : model.getStringFields()){
            stringField(field, writer);
        }
        for (FieldModel field : model.getBooleanFields()){
            booleanField(field, writer);
        }
        for (FieldModel field : model.getSimpleFields()){
            simpleField(field, writer);
        }
        for (FieldModel field : model.getComparableFields()){
            comparableField(field, writer);
        }
        for (FieldModel field : model.getDateFields()){
            dateField(field, writer);
        }
        for (FieldModel field : model.getDateTimeFields()){
            dateTimeField(field, writer);
        }
        for (FieldModel field : model.getTimeFields()){
            timeField(field, writer);
        }
        for (FieldModel field : model.getNumericFields()){
            numericField(field, writer);
        }
        for (FieldModel field : model.getSimpleCollections()){
            simpleCollection(field, writer);
        }
        for (FieldModel field : model.getEntityCollections()){
            entityCollection(field, writer);
        }
        for (FieldModel field : model.getSimpleMaps()){
            simpleMap(field, writer);
        }
        for (FieldModel field : model.getSimpleLists()){
            simpleList(field, writer);
        }
        for (FieldModel field : model.getEntityMaps()){
            entityMap(field, writer);
        }
        for (FieldModel field : model.getEntityLists()){
            entityList(field, writer);
        }
        for (FieldModel field : model.getEntityFields()){
            entityField(field, writer);
        }
        
        // constructors
        constructors(model, writer);
        
        // accessors
        for (FieldModel field : model.getSimpleMaps()){
            simpleMapAccessor(field, writer);
        }
        for (FieldModel field : model.getSimpleLists()){
            simpleListAccessor(field, writer);
        }
        for (FieldModel field : model.getEntityMaps()){
            entityMapAccessor(field, writer);
        }
        for (FieldModel field : model.getEntityLists()){
            entityListAccessor(field, writer);
        }
        for (FieldModel field : model.getEntityFields()){
            entityFieldAccessor(field, writer);
        }
        
        // outro
        outro(model, writer);
    }

    protected void serialize(FieldModel field, String type, Writer writer, String factoryMethod, String... args) throws IOException{
        // construct value
        StringBuilder value = new StringBuilder();
        value.append(factoryMethod + "(\"" + field.getName() + "\"");
        for (String arg : args){
            value.append(", " + arg);
        }        
        value.append(")");
        
        // serialize it
        StringBuilder builder = new StringBuilder();
        if (field.getDocString() != null){
            builder.append("    /** "  + field.getDocString() + " */\n");    
        }        
        builder.append("    public final " + type + " " + field.getEscapedName() + " = " + value + ";\n\n");
        writer.append(builder.toString());
    }
    
    protected void serialize(FieldModel field, String type, Writer writer) throws IOException {
        StringBuilder builder = new StringBuilder();
        if (field.getDocString() != null){
            builder.append("    /** "  + field.getDocString() + " */\n");    
        }        
        builder.append("    public " + type + " " + field.getEscapedName()+";\n\n");
        writer.append(builder.toString());        
    }
    
    
    protected void booleanField(FieldModel field, Writer writer) throws IOException {
        serialize(field, "PBoolean", writer, "_boolean");
    }
    
    protected void comparableField(FieldModel field, Writer writer) throws IOException {        
        serialize(field, "PComparable<" + field.getTypeName() + ">", writer, "_comparable", field.getTypeName() + ".class");
    }

    protected void constructors(ClassModel model, Writer writer) throws IOException {
        final String simpleName = model.getSimpleName();
        final String queryType = model.getPrefix() + simpleName;
        final String localName = model.getLocalName();
        
        StringBuilder builder = new StringBuilder();
        if (!embeddable){
            builder.append("    public " + queryType + "(@NotEmpty String variable) {\n");
            builder.append("        this(" + localName + ".class, variable);\n");
            builder.append("    }\n\n");
            
            builder.append("    public " + queryType + "(Class<? extends " + localName + "> cl, @NotEmpty String variable) {\n");
            builder.append("        super(cl, \"" + simpleName + "\", variable);\n");
            for (FieldModel entityField : model.getEntityFields()){
                builder.append("        _" + entityField.getName()+"();\n"); 
            }
            builder.append("    }\n\n");    
        }        

        builder.append("    public " + queryType + "(PEntity<? extends "+localName+"> entity) {\n");
        builder.append("        super(entity.getType(), entity.getEntityName(), entity.getMetadata());\n");
        builder.append("    }\n\n");        
        builder.append("    public " + queryType + "(PathMetadata<?> metadata) {\n");
        builder.append("        super("+ localName + ".class, \"" + simpleName + "\", metadata);\n");
        builder.append("    }\n\n");
        writer.append(builder.toString());
    }

    protected void dateField(FieldModel field, Writer writer) throws IOException {
        serialize(field, "PDate<" + field.getTypeName() + ">", writer, "_date", field.getTypeName()+".class");
    }

    protected void dateTimeField(FieldModel field, Writer writer) throws IOException {
        serialize(field, "PDateTime<" + field.getTypeName() + ">", writer, "_dateTime", field.getTypeName()+".class");        
    }

    protected void entityCollection(FieldModel field, Writer writer) throws IOException {
        serialize(field, "PEntityCollection<" + field.getTypeName()+">", writer, "_entitycol", field.getTypeName()+".class", "\"" + field.getSimpleTypeName()+"\"");        
    }

    protected void entityField(FieldModel field, Writer writer) throws IOException {
        serialize(field, field.getQueryTypeName(), writer);
        
    }

    protected void entityFieldAccessor(FieldModel field, Writer writer) throws IOException {        
        final String fieldName = field.getName();
        final String escapedName = field.getEscapedName();
        final String queryType = field.getQueryTypeName();               
        StringBuilder builder = new StringBuilder();
        builder.append("    public " + queryType + " _" + fieldName + "() {\n");
        builder.append("        if (" + escapedName + " == null){\n");
        builder.append("            " + escapedName + " = new " + queryType + "(PathMetadata.forProperty(this,\"" + fieldName + "\"));\n");
        builder.append("        }\n");
        builder.append("        return " + escapedName + ";\n");
        builder.append("    }\n\n");
        writer.append(builder.toString());
    }

    protected void entityList(FieldModel field, Writer writer) throws IOException {
        serialize(field, "PEntityList<" + field.getTypeName()+ ">", writer, "_entitylist", field.getTypeName()+".class", "\"" + field.getSimpleTypeName()+"\"");        
    }

    protected void entityListAccessor(FieldModel field, Writer writer) throws IOException {
//        final String fieldName = field.getName();
        final String escapedName = field.getEscapedName();
        final String queryType = field.getQueryTypeName();               
        StringBuilder builder = new StringBuilder();
        builder.append("    public " + queryType + " " + escapedName + "(int index) {\n");
        builder.append("        return new " + queryType + "(PathMetadata.forListAccess(" + escapedName+", index));\n");
        builder.append("    };\n\n");
        builder.append("    public " + queryType + " " + escapedName + "(com.mysema.query.types.expr.Expr<Integer> index) {\n");
        builder.append("        return new " + queryType + "(PathMetadata.forListAccess(" + escapedName+", index));\n");
        builder.append("    };\n\n");
        writer.append(builder.toString());
    }

    protected void entityMap(FieldModel field, Writer writer) throws IOException{
        final String keyType = field.getKeyTypeName();
        final String valueType = field.getValueTypeName();
        final String simpleName = field.getSimpleTypeName();
        serialize(field, "PEntityMap<"+keyType+","+valueType+">",
                writer, "_entitymap", keyType+".class", valueType+".class", "\""+simpleName+"\"");
        
    }

    protected void entityMapAccessor(FieldModel field, Writer writer) throws IOException {
//        final String fieldName = field.getName();
        final String escapedName = field.getEscapedName();
        final String queryType = field.getQueryTypeName();
        final String keyType = field.getKeyTypeName();
        StringBuilder builder = new StringBuilder();
        builder.append("    public " + queryType + " " + escapedName + "(" + keyType+ " key) {\n");
        builder.append("        return new " + queryType + "(PathMetadata.forMapAccess(" + escapedName+", key));\n");
        builder.append("    };\n\n");        
        builder.append("    public " + queryType + " " + escapedName + "(com.mysema.query.types.expr.Expr<"+keyType+"> key) {\n");
        builder.append("        return new " + queryType + "(PathMetadata.forMapAccess(" + escapedName+", key));\n");
        builder.append("    };\n\n");
        writer.append(builder.toString());
        
    }

    protected void intro(ClassModel model, Writer writer) throws IOException {
        final String simpleName = model.getSimpleName();
        final String queryType = model.getPrefix() + simpleName;
        final String localName = model.getLocalName();
        
        StringBuilder builder = new StringBuilder();        
        // package
        builder.append("package " + model.getPackageName() + ";\n\n");
        
        // imports
        builder.append("import com.mysema.query.util.*;\n");
        builder.append("import com.mysema.query.types.path.*;\n\n");
        
        // javadoc
        builder.append("/**\n");
        builder.append(" * " + queryType + " is a Querydsl query type for " + simpleName + "\n");
        builder.append(" * \n");
        builder.append(" */ \n");
        
        // class header
        builder.append("@SuppressWarnings(\"all\")\n");
        builder.append("public class " + queryType + " extends PEntity<" + localName + "> {\n\n");
        
        defaultInstance(model, builder);
        
        writer.append(builder.toString());
    }

    protected void defaultInstance(ClassModel model, StringBuilder builder) {
        final String simpleName = model.getSimpleName();
        final String unscapSimpleName = model.getUncapSimpleName();
        final String queryType = model.getPrefix() + simpleName;
        if (!embeddable){
            // default variable
            builder.append("    public static final " + queryType + " " + unscapSimpleName + " = new " + queryType + "(\"" + unscapSimpleName + "\");\n\n");            
        }
    }
    
    protected void numericField(FieldModel field, Writer writer) throws IOException {
        serialize(field, "PNumber<" + field.getTypeName() + ">", writer, "_number", field.getTypeName() +".class");        
    }

    protected void outro(ClassModel model, Writer writer) throws IOException {
        writer.write("}\n");        
    }

    protected void simpleCollection(FieldModel field, Writer writer) throws IOException {
        serialize(field, "PComponentCollection<" + field.getTypeName()+">", writer, "_simplecol", field.getTypeName()+".class");        
    }

    protected void simpleField(FieldModel field, Writer writer) throws IOException {
        serialize(field, "PSimple<" + field.getTypeName()+">", writer, "_simple", field.getTypeName()+".class");        
    }

    protected void simpleList(FieldModel field, Writer writer) throws IOException {
        serialize(field, "PComponentList<" + field.getTypeName()+">", writer, "_simplelist", field.getTypeName()+".class");        
    }

    protected void simpleListAccessor(FieldModel field, Writer writer) throws IOException {
//        final String fieldName = field.getName();      
        final String escapedName = field.getEscapedName();
        final String valueType = field.getValueTypeName();
        StringBuilder builder = new StringBuilder();
        builder.append("    public PSimple<" + valueType + "> " + escapedName + "(int index) {\n");
        builder.append("        return new PSimple<" + valueType + ">("+valueType+".class, PathMetadata.forListAccess(" + escapedName+", index));\n");
        builder.append("    };\n\n");
        builder.append("    public PSimple<" + valueType + "> " + escapedName + "(com.mysema.query.types.expr.Expr<Integer> index) {\n");
        builder.append("        return new PSimple<" + valueType + ">("+valueType+".class, PathMetadata.forListAccess(" + escapedName+", index));\n");
        builder.append("    };\n\n");
        writer.append(builder.toString());
        
    }

    protected void simpleMap(FieldModel field, Writer writer) throws IOException {               
        final String keyType = field.getKeyTypeName();
        final String valueType = field.getValueTypeName();
        serialize(field, "PComponentMap<"+keyType+","+valueType+">", writer, "_simplemap", keyType+".class", valueType+".class");
        
    }

    protected void simpleMapAccessor(FieldModel field, Writer writer) throws IOException {
//        final String fieldName = field.getName();     
        final String escapedName = field.getEscapedName();
        final String keyType = field.getKeyTypeName();
        final String valueType = field.getValueTypeName();
        StringBuilder builder = new StringBuilder();
        builder.append("    public PSimple<" + valueType + "> " + escapedName + "(" + keyType + " key) {\n");
        builder.append("        return new PSimple<" + valueType + ">("+valueType+".class, PathMetadata.forMapAccess(" + escapedName+", key));\n");
        builder.append("    };\n\n");
        builder.append("    public PSimple<" + valueType + "> " + escapedName + "(com.mysema.query.types.expr.Expr<"+keyType+"> key) {\n");
        builder.append("        return new PSimple<" + valueType + ">("+valueType+".class, PathMetadata.forMapAccess(" + escapedName+", key));\n");
        builder.append("    };\n\n");
        writer.append(builder.toString());
        
    }

    protected void stringField(FieldModel field, Writer writer) throws IOException {
        serialize(field, "PString", writer, "_string");        
    }

    protected void timeField(FieldModel field, Writer writer) throws IOException {
        serialize(field, "PTime<" + field.getTypeName() + ">", writer, "_time", field.getTypeName()+".class");        
    }



}
