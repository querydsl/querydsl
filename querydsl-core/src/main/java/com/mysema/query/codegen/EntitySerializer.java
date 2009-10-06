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
public class EntitySerializer implements Serializer{
    
    public void serialize(BeanModel model, Writer writer) throws IOException{
        // intro
        intro(model, writer);
        
        // factory methods
        factoryMethods(model, writer);        
        
        // fields
        for (PropertyModel field : model.getStringProperties()){
            stringField(field, writer);
        }
        for (PropertyModel field : model.getBooleanProperties()){
            booleanField(field, writer);
        }
        for (PropertyModel field : model.getSimpleProperties()){
            simpleField(field, writer);
        }
        for (PropertyModel field : model.getComparableProperties()){
            comparableField(field, writer);
        }
        for (PropertyModel field : model.getDateProperties()){
            dateField(field, writer);
        }
        for (PropertyModel field : model.getDateTimeProperties()){
            dateTimeField(field, writer);
        }
        for (PropertyModel field : model.getTimeProperties()){
            timeField(field, writer);
        }
        for (PropertyModel field : model.getNumericProperties()){
            numericField(field, writer);
        }
        for (PropertyModel field : model.getSimpleCollections()){
            collectionOfSimple(field, writer);
        }
        for (PropertyModel field : model.getEntityCollections()){
            collectionOfEntity(field, writer);
        }
        for (PropertyModel field : model.getSimpleMaps()){
            mapOfSimple(field, writer);
        }
        for (PropertyModel field : model.getEntityMaps()){
            mapOfEntity(field, writer);
        }
        for (PropertyModel field : model.getSimpleLists()){
            listSimple(field, writer);
        }
        for (PropertyModel field : model.getEntityLists()){
            listOfEntity(field, writer);
        }
        for (PropertyModel field : model.getEntityProperties()){
            entityField(field, writer);
        }
        
        // constructors
        constructors(model, writer);
        
        // accessors
        for (PropertyModel field : model.getSimpleLists()){
            listOfSimpleAccessor(field, writer);
        }
        for (PropertyModel field : model.getEntityLists()){
            listOfEntityAccessor(field, writer);
        }
        for (PropertyModel field : model.getSimpleMaps()){
            mapOfSimpleAccessor(field, writer);
        }
        for (PropertyModel field : model.getEntityMaps()){
            mapOfEntityAccessor(field, writer);
        }        
        for (PropertyModel field : model.getEntityProperties()){
            entityFieldAccessor(field, writer);
        }
        
        // outro
        outro(model, writer);
    }
        
    protected void factoryMethods(BeanModel model, Writer writer) throws IOException {
        final String localName = model.getLocalName();
        
        StringBuilder builder = new StringBuilder();
        for (ConstructorModel c : model.getConstructors()){
            // begin
            builder.append("    public static EConstructor<" + localName + "> create(");
            boolean first = true;
            for (ParameterModel p : c.getParameters()){
                if (!first) builder.append(", ");
                builder.append("Expr<" + p.getTypeName() + "> " + p.getName());
                first = false;
            }
            builder.append("){\n");
            
            // body
            builder.append("        return new EConstructor<" + localName + ">(" + localName + ".class");
            builder.append(", new Class[]{");
            first = true;
            for (ParameterModel p : c.getParameters()){
                if (!first) builder.append(", ");
                builder.append(p.getRealTypeName() + ".class");
                first = false;
            }
            builder.append("}");
            
            for (ParameterModel p : c.getParameters()){
                builder.append(", " + p.getName());
            }
            
            // end
            builder.append(");\n");
            builder.append("    }\n\n");
        }
        writer.append(builder.toString());
        
    }

    protected void booleanField(PropertyModel field, Writer writer) throws IOException {
        serialize(field, "PBoolean", writer, "createBoolean");
    }

    protected void collectionOfEntity(PropertyModel field, Writer writer) throws IOException {
        serialize(field, "PEntityCollection<" + field.getTypeName()+">", writer, "createEntityCollection", field.getTypeName()+".class", "\"" + field.getSimpleTypeName()+"\"");        
    }
    
    protected void collectionOfSimple(PropertyModel field, Writer writer) throws IOException {
        serialize(field, "PComponentCollection<" + field.getTypeName()+">", writer, "createSimpleCollection", field.getTypeName()+".class");        
    }
    
    
    protected void comparableField(PropertyModel field, Writer writer) throws IOException {        
        serialize(field, "PComparable<" + field.getTypeName() + ">", writer, "createComparable", field.getTypeName() + ".class");
    }
    
    protected void constructors(BeanModel model, Writer writer) throws IOException {
        final String simpleName = model.getSimpleName();
        final String queryType = model.getPrefix() + simpleName;
        final String localName = model.getLocalName();
        
        StringBuilder builder = new StringBuilder();
        constructorsForVariables(builder, model);    

        builder.append("    public " + queryType + "(PEntity<? extends "+localName+"> entity) {\n");
        builder.append("        super(entity.getType(), entity.getEntityName(), entity.getMetadata());\n");
        builder.append("    }\n\n");        
        builder.append("    public " + queryType + "(PathMetadata<?> metadata) {\n");
        builder.append("        super("+ localName + ".class, \"" + simpleName + "\", metadata);\n");
        builder.append("    }\n\n");
        writer.append(builder.toString());
    }
    
    protected void constructorsForVariables(StringBuilder builder, BeanModel model) {
        final String simpleName = model.getSimpleName();
        final String queryType = model.getPrefix() + simpleName;
        final String localName = model.getLocalName();
        builder.append("    public " + queryType + "(@NotEmpty String variable) {\n");
        builder.append("        this(" + localName + ".class, variable);\n");
        builder.append("    }\n\n");
        
        builder.append("    public " + queryType + "(Class<? extends " + localName + "> cl, @NotEmpty String variable) {\n");
        builder.append("        super(cl, \"" + simpleName + "\", variable);\n");
        for (PropertyModel entityField : model.getEntityProperties()){
            builder.append("        _" + entityField.getName()+"();\n"); 
        }
        builder.append("    }\n\n");
    }  

    protected void dateField(PropertyModel field, Writer writer) throws IOException {
        serialize(field, "PDate<" + field.getTypeName() + ">", writer, "createDate", field.getTypeName()+".class");
    }

    protected void dateTimeField(PropertyModel field, Writer writer) throws IOException {
        serialize(field, "PDateTime<" + field.getTypeName() + ">", writer, "createDateTime", field.getTypeName()+".class");        
    }

    protected void entityField(PropertyModel field, Writer writer) throws IOException {
        serialize(field, field.getQueryTypeName(), writer);        
    }

    protected void entityFieldAccessor(PropertyModel field, Writer writer) throws IOException {        
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

    protected void intro(BeanModel model, Writer writer) throws IOException {        
        StringBuilder builder = new StringBuilder();        
        introPackage(builder, model);        
        introImports(builder);        
        introJavadoc(builder, model);        
        introClassHeader(builder, model);        
        introDefaultInstance(builder, model);        
        introSuper(builder, model);
        writer.append(builder.toString());
    }

    private void introSuper(StringBuilder builder, BeanModel model) {
        if (model.getSuperModel() != null){
            BeanModel _super = model.getSuperModel();
            final String simpleName = _super.getSimpleName();
            final String queryType = _super.getPrefix() + simpleName;
            builder.append("    public final "+queryType+" _super = new " + queryType + "(this);\n\n");
        }        
    }

    protected void introClassHeader(StringBuilder builder, BeanModel model) {
        final String queryType = model.getPrefix() + model.getSimpleName();
        final String localName = model.getLocalName();
        builder.append("@SuppressWarnings(\"all\")\n");
        builder.append("public class " + queryType + " extends PEntity<" + localName + "> {\n\n");
    }

    protected void introDefaultInstance(StringBuilder builder, BeanModel model) {
        final String simpleName = model.getSimpleName();
        final String unscapSimpleName = model.getUncapSimpleName();
        final String queryType = model.getPrefix() + simpleName;
        builder.append("    public static final " + queryType + " " + unscapSimpleName + " = new " + queryType + "(\"" + unscapSimpleName + "\");\n\n");
    }

    protected void introImports(StringBuilder builder) {
        builder.append("import com.mysema.query.util.*;\n");
        builder.append("import com.mysema.query.types.path.*;\n");
        builder.append("import com.mysema.query.types.expr.*;\n");
    }

    protected void introJavadoc(StringBuilder builder, BeanModel model) {
        final String simpleName = model.getSimpleName();
        final String queryType = model.getPrefix() + simpleName;
        builder.append("/**\n");
        builder.append(" * " + queryType + " is a Querydsl query type for " + simpleName + "\n");
        builder.append(" * \n");
        builder.append(" */ \n");
    }

    protected void introPackage(StringBuilder builder, BeanModel model) {
        builder.append("package " + model.getPackageName() + ";\n\n");
    }

    protected void listOfEntity(PropertyModel field, Writer writer) throws IOException {
        serialize(field, "PEntityList<" + field.getTypeName()+ ">", writer, "createEntityList", field.getTypeName()+".class", "\"" + field.getSimpleTypeName()+"\"");        
    }

    protected void listOfEntityAccessor(PropertyModel field, Writer writer) throws IOException {
        final String escapedName = field.getEscapedName();
        final String queryType = field.getQueryTypeName();               
        StringBuilder builder = new StringBuilder();
        
        builder.append("    public " + queryType + " " + escapedName + "(int index) {\n");
        builder.append("        return new " + queryType + "(PathMetadata.forListAccess(" + escapedName+", index));\n");
        builder.append("    }\n\n");
        builder.append("    public " + queryType + " " + escapedName + "(com.mysema.query.types.expr.Expr<Integer> index) {\n");
        builder.append("        return new " + queryType + "(PathMetadata.forListAccess(" + escapedName+", index));\n");
        builder.append("    }\n\n");
        writer.append(builder.toString());
    }

    protected void listOfSimpleAccessor(PropertyModel field, Writer writer) throws IOException { 
        final String escapedName = field.getEscapedName();
        final String valueType = field.getValueTypeName();
        StringBuilder builder = new StringBuilder();
        
        builder.append("    public PSimple<" + valueType + "> " + escapedName + "(int index) {\n");
        builder.append("        return new PSimple<" + valueType + ">("+valueType+".class, PathMetadata.forListAccess(" + escapedName+", index));\n");
        builder.append("    }\n\n");
        builder.append("    public PSimple<" + valueType + "> " + escapedName + "(com.mysema.query.types.expr.Expr<Integer> index) {\n");
        builder.append("        return new PSimple<" + valueType + ">("+valueType+".class, PathMetadata.forListAccess(" + escapedName+", index));\n");
        builder.append("    }\n\n");
        writer.append(builder.toString());
        
    }

    protected void listSimple(PropertyModel field, Writer writer) throws IOException {
        serialize(field, "PComponentList<" + field.getTypeName()+">", writer, "createSimpleList", field.getTypeName()+".class");        
    }

    protected void mapOfEntity(PropertyModel field, Writer writer) throws IOException{
        final String keyType = field.getKeyTypeName();
        final String valueType = field.getValueTypeName();
        final String simpleName = field.getSimpleTypeName();
        serialize(field, "PEntityMap<"+keyType+","+valueType+">",
                writer, "createEntityMap", keyType+".class", valueType+".class", "\""+simpleName+"\"");
        
    }

    protected void mapOfEntityAccessor(PropertyModel field, Writer writer) throws IOException {
        final String escapedName = field.getEscapedName();
        final String queryType = field.getQueryTypeName();
        final String keyType = field.getKeyTypeName();
        StringBuilder builder = new StringBuilder();
        
        builder.append("    public " + queryType + " " + escapedName + "(" + keyType+ " key) {\n");
        builder.append("        return new " + queryType + "(PathMetadata.forMapAccess(" + escapedName+", key));\n");
        builder.append("    }\n\n");        
        builder.append("    public " + queryType + " " + escapedName + "(com.mysema.query.types.expr.Expr<"+keyType+"> key) {\n");
        builder.append("        return new " + queryType + "(PathMetadata.forMapAccess(" + escapedName+", key));\n");
        builder.append("    }\n\n");
        writer.append(builder.toString());
        
    }

    protected void mapOfSimple(PropertyModel field, Writer writer) throws IOException {               
        final String keyType = field.getKeyTypeName();
        final String valueType = field.getValueTypeName();
        
        serialize(field, "PComponentMap<"+keyType+","+valueType+">", writer, "createSimpleMap", keyType+".class", valueType+".class");
        
    }
    
    protected void mapOfSimpleAccessor(PropertyModel field, Writer writer) throws IOException {
//        final String fieldName = field.getName();     
        final String escapedName = field.getEscapedName();
        final String keyType = field.getKeyTypeName();
        final String valueType = field.getValueTypeName();
        StringBuilder builder = new StringBuilder();
        
        builder.append("    public PSimple<" + valueType + "> " + escapedName + "(" + keyType + " key) {\n");
        builder.append("        return new PSimple<" + valueType + ">("+valueType+".class, PathMetadata.forMapAccess(" + escapedName+", key));\n");
        builder.append("    }\n\n");
        builder.append("    public PSimple<" + valueType + "> " + escapedName + "(com.mysema.query.types.expr.Expr<"+keyType+"> key) {\n");
        builder.append("        return new PSimple<" + valueType + ">("+valueType+".class, PathMetadata.forMapAccess(" + escapedName+", key));\n");
        builder.append("    }\n\n");
        writer.append(builder.toString());
        
    }

    protected void numericField(PropertyModel field, Writer writer) throws IOException {
        serialize(field, "PNumber<" + field.getTypeName() + ">", writer, "createNumber", field.getTypeName() +".class");        
    }

    protected void outro(BeanModel model, Writer writer) throws IOException {
        writer.write("}\n");        
    }

    protected void serialize(PropertyModel field, String type, Writer writer) throws IOException {
        StringBuilder builder = new StringBuilder();
//        if (field.getDocString() != null){
//            builder.append("    /** "  + field.getDocString() + " */\n");    
//        }        
        builder.append("    public " + type + " " + field.getEscapedName()+";\n\n");
        writer.append(builder.toString());        
    }

    protected void serialize(PropertyModel field, String type, Writer writer, String factoryMethod, String... args) throws IOException{
        // construct value
        StringBuilder value = new StringBuilder();
        value.append(factoryMethod + "(\"" + field.getName() + "\"");
        for (String arg : args){
            value.append(", " + arg);
        }        
        value.append(")");
        
        // serialize it
        StringBuilder builder = new StringBuilder();
//        if (field.getDocString() != null){
//            builder.append("    /** "  + field.getDocString() + " */\n");    
//        }        
        builder.append("    public final " + type + " " + field.getEscapedName() + " = " + value + ";\n\n");
        writer.append(builder.toString());
    }

    protected void simpleField(PropertyModel field, Writer writer) throws IOException {
        serialize(field, "PSimple<" + field.getTypeName()+">", writer, "createSimple", field.getTypeName()+".class");        
    }

    protected void stringField(PropertyModel field, Writer writer) throws IOException {
        serialize(field, "PString", writer, "createString");        
    }

    protected void timeField(PropertyModel field, Writer writer) throws IOException {
        serialize(field, "PTime<" + field.getTypeName() + ">", writer, "createTime", field.getTypeName()+".class");        
    }



}
