/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import net.jcip.annotations.Immutable;

@Immutable
public class EntitySerializer implements Serializer{
    
    public void serialize(BeanModel model, Writer writer) throws IOException{
        // intro
        intro(model, writer);
        
        for (PropertyModel property : model.getProperties()){
            switch(property.getTypeCategory()){
            case STRING: stringField(property, writer); break;
            case BOOLEAN: booleanField(property, writer); break;
            case SIMPLE: simpleField(property, writer); break;
            case COMPARABLE: comparableField(property, writer); break;
            case DATE: dateField(property, writer); break;
            case DATETIME: dateTimeField(property, writer); break;
            case TIME: timeField(property, writer); break;
            case NUMERIC: numericField(property, writer); break;
            case SIMPLECOLLECTION: collectionOfSimple(property, writer); break;
            case ENTITYCOLLECTION: collectionOfEntity(property, writer); break;
            case SIMPLEMAP: mapOfSimple(property, writer); break;
            case ENTITYMAP: mapOfEntity(property, writer); break;
            case SIMPLELIST: listOfSimple(property, writer); break;
            case ENTITYLIST: listOfEntity(property, writer); break;
            case ENTITY: entityField(property, writer); break;
            }
        }
        
        // constructors
        constructors(model, writer);
        
        for (PropertyModel property : model.getProperties()){
            switch(property.getTypeCategory()){
            case SIMPLEMAP: mapOfSimpleAccessor(property, writer); break;
            case ENTITYMAP: mapOfEntityAccessor(property, writer); break;
            case SIMPLELIST: listOfSimpleAccessor(property, writer); break;
            case ENTITYLIST: listOfEntityAccessor(property, writer); break;
            }
        }
        
        // outro
        outro(model, writer);
    }
        
    protected void introFactoryMethods(StringBuilder builder, BeanModel model) throws IOException {
        final String localName = model.getLocalName();
        final String genericName = model.getGenericName();
        
        for (ConstructorModel c : model.getConstructors()){
            // begin
            if (!localName.equals(genericName)){
                builder.append("    @SuppressWarnings(\"unchecked\")\n");
            }            
            builder.append("    public static EConstructor<" + genericName + "> create(");
            boolean first = true;
            for (ParameterModel p : c.getParameters()){
                if (!first) builder.append(", ");
                builder.append("Expr<" + p.getTypeName() + "> " + p.getName());
                first = false;
            }
            builder.append("){\n");
            
            // body
            builder.append("        return new EConstructor<" + genericName + ">(");
            if (!localName.equals(genericName)){
                builder.append("(Class)");
            }
            builder.append(localName + ".class");
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
    }

    protected void booleanField(PropertyModel field, Writer writer) throws IOException {
        serialize(field, "PBoolean", writer, "createBoolean");
    }

    protected void collectionOfEntity(PropertyModel field, Writer writer) throws IOException {
        String genericTypeName = field.getGenericParameterName(0);
        String typeName = field.getRawParameterName(0);
        serialize(field, "PEntityCollection<" + genericTypeName+">", writer, "createEntityCollection",typeName+".class");        
    }
    
    protected void collectionOfSimple(PropertyModel field, Writer writer) throws IOException {
        String genericTypeName = field.getGenericParameterName(0);
        String typeName = field.getRawParameterName(0);
        serialize(field, "PComponentCollection<" + genericTypeName+">", writer, "createSimpleCollection", typeName+".class");        
    }
        
    protected void comparableField(PropertyModel field, Writer writer) throws IOException {        
        serialize(field, "PComparable<" + field.getGenericTypeName() + ">", writer, "createComparable", field.getTypeName() + ".class");
    }
    
    protected void constructors(BeanModel model, Writer writer) throws IOException {
        final String simpleName = model.getSimpleName();
        final String queryType = model.getPrefix() + simpleName;
        final String localName = model.getLocalName();
        final String genericName = model.getGenericName();
        
        StringBuilder builder = new StringBuilder();
        
        boolean hasEntityFields = model.hasEntityFields();
        String thisOrSuper = hasEntityFields ? "this" : "super";
        
        // 1
        constructorsForVariables(builder, model);    

        // 2
        if (!hasEntityFields){
            builder.append("    public " + queryType + "(PEntity<? extends "+genericName+"> entity) {\n");
            builder.append("        super(entity.getType(), entity.getEntityName(), entity.getMetadata()");
            builder.append(");\n");
            builder.append("    }\n\n");    
        }        
        
        // 3        
        if (hasEntityFields){
            builder.append("    public " + queryType + "(PathMetadata<?> metadata) {\n");
            builder.append("        this(metadata, metadata.isRoot() ? __inits : PathInits.DEFAULT);\n");
            builder.append("    }\n\n");
        }else{
            if (!localName.equals(genericName)){
                builder.append("    @SuppressWarnings(\"unchecked\")\n");
            }
            builder.append("    public " + queryType + "(PathMetadata<?> metadata) {\n");
            builder.append("        super(");
            if (!localName.equals(genericName)){
                builder.append("(Class)");
            }
            builder.append(localName+".class, \""+simpleName+"\", metadata);\n");
            builder.append("    }\n\n");
        }       
        
        
        // 4
        if (!localName.equals(genericName)){
            builder.append("    @SuppressWarnings(\"unchecked\")\n");
        }        
        builder.append("    public " + queryType + "(PathMetadata<?> metadata, PathInits inits) {\n");
        builder.append("        "+thisOrSuper+"(");
        if (!localName.equals(genericName)){
            builder.append("(Class)");
        }
        builder.append(localName + ".class, \"" + simpleName + "\", metadata");
        if (hasEntityFields){
            builder.append(", inits");
        }
        builder.append(");\n");
        builder.append("    }\n\n");       
        
        // 5 (with entity field initialization)
        if (hasEntityFields){            
            builder.append("    public "+queryType+"(Class<? extends "+genericName+"> type, @NotEmpty String entityName, PathMetadata<?> metadata, PathInits inits) {\n");
            builder.append("        super(type, entityName, metadata);\n");
            initEntityFields(builder, model);
            builder.append("    }\n\n"); 
        }
        
        writer.append(builder.toString());
        
    }

    protected void initEntityFields(StringBuilder builder, BeanModel model) {
        BeanModel superModel = model.getSuperModel();
        if (superModel != null && superModel.hasEntityFields()){
            String superQueryType = superModel.getPrefix() + superModel.getSimpleName();
            if (!superModel.getPackageName().equals(model.getPackageName())){
                superQueryType = superModel.getPackageName() + "." + superQueryType;
            }   
            builder.append("        this._super = new " + superQueryType + "(type, entityName, metadata, inits);\n");            
        }
        
        for (PropertyModel field : model.getProperties()){            
            if (field.getTypeCategory() == TypeCategory.ENTITY){
                builder.append("        this." + field.getEscapedName() + " = ");
                if (!field.isInherited()){
                    builder.append("inits.isInitialized(\""+field.getName()+"\") ? ");
                    builder.append("new " + field.getQueryTypeName() + "(PathMetadata.forProperty(this,\"" + field.getName() + "\"), inits.getInits(\""+field.getName()+"\")) : null;\n");    
                }else{
                    builder.append("_super." + field.getEscapedName() +";\n");
                }   
            }else if (field.isInherited() && superModel != null && superModel.hasEntityFields()){
                builder.append("        this." + field.getEscapedName() + " = ");
                builder.append("_super." + field.getEscapedName() + ";\n");
            }
        }        
    }
    
    protected void constructorsForVariables(StringBuilder builder, BeanModel model) {
        final String simpleName = model.getSimpleName();
        final String queryType = model.getPrefix() + simpleName;
        final String localName = model.getLocalName();
        final String genericName = model.getGenericName();
        
        boolean hasEntityFields = model.hasEntityFields();
        String thisOrSuper = hasEntityFields ? "this" : "super";
        
        if (!localName.equals(genericName)){
            builder.append("    @SuppressWarnings(\"unchecked\")\n");
        }        
        builder.append("    public " + queryType + "(@NotEmpty String variable) {\n");
        builder.append("        "+thisOrSuper+"(");
        if (!localName.equals(genericName)){
            builder.append("(Class)");   
        }
        builder.append(localName + ".class, \""+simpleName+"\", PathMetadata.forVariable(variable)");
        if (hasEntityFields){
            builder.append(", __inits");
        }
        builder.append(");\n");
        builder.append("    }\n\n");        
    }  

    protected void dateField(PropertyModel field, Writer writer) throws IOException {
        serialize(field, "PDate<" + field.getGenericTypeName() + ">", writer, "createDate", field.getTypeName()+".class");
    }

    protected void dateTimeField(PropertyModel field, Writer writer) throws IOException {
        serialize(field, "PDateTime<" + field.getGenericTypeName() + ">", writer, "createDateTime", field.getTypeName()+".class");        
    }

    protected void entityField(PropertyModel field, Writer writer) throws IOException {
        final String type = field.getQueryTypeName();
        
        StringBuilder builder = new StringBuilder();
        if (field.isInherited()){
            builder.append("    // inherited\n");
        }       
        builder.append("    public final " + type + " " + field.getEscapedName() + ";\n\n");
        writer.append(builder.toString());
    }

    protected void intro(BeanModel model, Writer writer) throws IOException {        
        StringBuilder builder = new StringBuilder();        
        introPackage(builder, model);        
        introImports(builder, model);        
        introJavadoc(builder, model);        
        introClassHeader(builder, model);        
        introFactoryMethods(builder, model);   
        introInits(builder, model);
        introDefaultInstance(builder, model);   
        if (model.getSuperModel() != null){
            introSuper(builder, model);    
        }        
        writer.append(builder.toString());
    }

    protected void introInits(StringBuilder builder, BeanModel model) {
        if (model.hasEntityFields()){
            List<String> inits = new ArrayList<String>();
            for (PropertyModel property : model.getProperties()){
                if (property.getTypeCategory() == TypeCategory.ENTITY){
                    for (String init : property.getInits()){
                        inits.add(property.getEscapedName() + "." + init);    
                    }   
                }
            }
            
            if (!inits.isEmpty()){
                builder.append("    private static final PathInits __inits = new PathInits(\"*\"");
                for (String init : inits){
                    builder.append(", \"" + init + "\"");    
                }    
                builder.append(");\n\n");    
            }else{
                builder.append("    private static final PathInits __inits = PathInits.DIRECT;\n\n");
            }
                
        }               
    }

    protected void introSuper(StringBuilder builder, BeanModel model) {
        BeanModel superModel = model.getSuperModel();
        String superQueryType = superModel.getPrefix() + superModel.getSimpleName();
        if (!model.getPackageName().equals(superModel.getPackageName())){
            superQueryType = superModel.getPackageName() + "." + superQueryType;
        }
        if (!superModel.hasEntityFields()){
            builder.append("    public final "+superQueryType+" _super = new " + superQueryType + "(this);\n\n");    
        }else{
            builder.append("    public final "+superQueryType+" _super;\n\n");    
        }                  
    }

    protected void introClassHeader(StringBuilder builder, BeanModel model) {
        final String queryType = model.getPrefix() + model.getSimpleName();
        final String localName = model.getGenericName();
        
        builder.append("@SuppressWarnings(\"serial\")\n");
        builder.append("public class " + queryType + " extends PEntity<" + localName + "> {\n\n");
    }

    protected void introDefaultInstance(StringBuilder builder, BeanModel model) {
        final String simpleName = model.getSimpleName();
        final String unscapSimpleName = model.getUncapSimpleName();
        final String queryType = model.getPrefix() + simpleName;
        
        builder.append("    public static final " + queryType + " " + unscapSimpleName + " = new " + queryType + "(\"" + unscapSimpleName + "\");\n\n");
    }

    protected void introImports(StringBuilder builder, BeanModel model) {
        builder.append("import com.mysema.query.util.*;\n");
        builder.append("import com.mysema.query.types.path.*;\n");
        if (!model.getConstructors().isEmpty() || model.hasLists() || model.hasMaps()){
            builder.append("import com.mysema.query.types.expr.*;\n");
        }
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
        String genericTypeName = field.getGenericParameterName(0);
        String typeName = field.getRawParameterName(0);
        
        serialize(field, "PEntityList<" + genericTypeName+ "," + field.getQueryTypeName() +  ">", writer, "createEntityList", 
                typeName+".class",
                field.getQueryTypeName() +".class");        
    }

    protected void listOfEntityAccessor(PropertyModel field, Writer writer) throws IOException {
        final String escapedName = field.getEscapedName();
        final String queryType = field.getQueryTypeName();               
        
        StringBuilder builder = new StringBuilder();        
        builder.append("    public " + queryType + " " + escapedName + "(int index) {\n");
        builder.append("        return " + escapedName + ".get(index);\n");
        builder.append("    }\n\n");
        builder.append("    public " + queryType + " " + escapedName + "(Expr<Integer> index) {\n");
        builder.append("        return " + escapedName + ".get(index);\n");
        builder.append("    }\n\n");
        writer.append(builder.toString());
    }

    protected void listOfSimpleAccessor(PropertyModel field, Writer writer) throws IOException { 
        final String escapedName = field.getEscapedName();
        final String valueType = field.getGenericParameterName(0);
        
        StringBuilder builder = new StringBuilder();        
        builder.append("    public PSimple<" + valueType + "> " + escapedName + "(int index) {\n");
        builder.append("        return " + escapedName + ".get(index);\n");
        builder.append("    }\n\n");
        builder.append("    public PSimple<" + valueType + "> " + escapedName + "(Expr<Integer> index) {\n");
        builder.append("        return " + escapedName + ".get(index);\n");
        builder.append("    }\n\n");
        writer.append(builder.toString());
        
    }

    protected void listOfSimple(PropertyModel field, Writer writer) throws IOException {
        String genericTypeName = field.getGenericParameterName(0);
        String typeName = field.getRawParameterName(0);
        
        serialize(field, "PComponentList<" + genericTypeName+">", writer, "createSimpleList", typeName+".class");        
    }

    protected void mapOfEntity(PropertyModel field, Writer writer) throws IOException{
        final String keyType = field.getRawParameterName(0);
        final String valueType = field.getRawParameterName(1);
        final String genericKey = field.getGenericParameterName(0);
        final String genericValue = field.getGenericParameterName(1);
        
        serialize(field, "PEntityMap<"+genericKey+","+genericValue+","+field.getQueryTypeName()+">",
                writer, "createEntityMap", 
                keyType+".class", 
                valueType+".class", 
                field.getQueryTypeName()+".class");
        
    }

    protected void mapOfEntityAccessor(PropertyModel field, Writer writer) throws IOException {
        final String escapedName = field.getEscapedName();
        final String queryType = field.getQueryTypeName();
        final String keyType = field.getGenericParameterName(0);
        final String genericKey = field.getGenericParameterName(0);
        
        StringBuilder builder = new StringBuilder();        
        builder.append("    public " + queryType + " " + escapedName + "(" + keyType+ " key) {\n");
        builder.append("        return " + escapedName + ".get(key);\n");
        builder.append("    }\n\n");        
        builder.append("    public " + queryType + " " + escapedName + "(Expr<"+genericKey+"> key) {\n");
        builder.append("        return " + escapedName + ".get(key);\n");
        builder.append("    }\n\n");
        writer.append(builder.toString());
        
    }

    protected void mapOfSimple(PropertyModel field, Writer writer) throws IOException {               
        final String keyType = field.getRawParameterName(0);
        final String valueType = field.getRawParameterName(1);
        final String genericKey = field.getGenericParameterName(0);
        final String genericValue = field.getGenericParameterName(1);
        
        serialize(field, "PComponentMap<"+genericKey+","+genericValue+">", 
            writer, "createSimpleMap", keyType+".class", valueType+".class");
        
    }
    
    protected void mapOfSimpleAccessor(PropertyModel field, Writer writer) throws IOException {
        final String escapedName = field.getEscapedName();
        final String genericKey = field.getGenericParameterName(0);
        final String genericValue = field.getGenericParameterName(1);
        
        StringBuilder builder = new StringBuilder();
        
        builder.append("    public PSimple<" + genericValue + "> " + escapedName + "(" + genericKey + " key) {\n");
        builder.append("        return " + escapedName + ".get(key);\n");
        builder.append("    }\n\n");
        builder.append("    public PSimple<" + genericValue + "> " + escapedName + "(Expr<"+genericKey+"> key) {\n");
        builder.append("        return " + escapedName + ".get(key);\n");
        builder.append("    }\n\n");
        writer.append(builder.toString());
        
    }

    protected void numericField(PropertyModel field, Writer writer) throws IOException {
        serialize(field, "PNumber<" + field.getGenericTypeName() + ">", writer, "createNumber", field.getTypeName() +".class");        
    }

    protected void outro(BeanModel model, Writer writer) throws IOException {
        writer.write("}\n");        
    }

    protected void serialize(PropertyModel field, String type, Writer writer, String factoryMethod, String... args) throws IOException{
        BeanModel superModel = field.getBeanModel().getSuperModel();
        // construct value
        StringBuilder value = new StringBuilder();
        if (field.isInherited() && superModel != null){
            if (!superModel.hasEntityFields()){
                value.append("_super." + field.getEscapedName());    
            }            
        }else{
            value.append(factoryMethod + "(\"" + field.getName() + "\"");
            for (String arg : args){
                value.append(", " + arg);
            }        
            value.append(")");    
        }                 
        
        // serialize it
        StringBuilder builder = new StringBuilder();
        if (field.isInherited()){
            builder.append("    // inherited\n");
        }        
        if (value.length() > 0){
            builder.append("    public final " + type + " " + field.getEscapedName() + " = " + value + ";\n\n");    
        }else{
            builder.append("    public final " + type + " " + field.getEscapedName() + ";\n\n");
        }        
        writer.append(builder.toString());
    }

    protected void simpleField(PropertyModel field, Writer writer) throws IOException {
        serialize(field, "PSimple<" + field.getGenericTypeName()+">", writer, "createSimple", field.getTypeName()+".class");        
    }

    protected void stringField(PropertyModel field, Writer writer) throws IOException {
        serialize(field, "PString", writer, "createString");        
    }

    protected void timeField(PropertyModel field, Writer writer) throws IOException {
        serialize(field, "PTime<" + field.getGenericTypeName() + ">", writer, "createTime", field.getTypeName()+".class");        
    }

}
