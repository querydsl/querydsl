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
public class EntitySerializer extends AbstractSerializer{
    
    protected void constructors(EntityModel model, Writer writer) throws IOException {
        String simpleName = model.getSimpleName();
        String queryType = getQueryType(model, model, true);
        String localName = model.getLocalRawName();
        String genericName = model.getLocalGenericName();
        
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
            builder.append("        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);\n");
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
        if (hasEntityFields){
            if (!localName.equals(genericName)){
                builder.append("    @SuppressWarnings(\"unchecked\")\n");
            }        
            builder.append("    public " + queryType + "(PathMetadata<?> metadata, PathInits inits) {\n");
            builder.append("        "+thisOrSuper+"(");
            if (!localName.equals(genericName)){
                builder.append("(Class)");
            }
            builder.append(localName + ".class, \"" + simpleName + "\", metadata");
            builder.append(", inits");
            builder.append(");\n");
            builder.append("    }\n\n");    
        }                         
        
        // 5 
        if (hasEntityFields){            
            builder.append("    public "+queryType+"(Class<? extends "+genericName+"> type, @NotEmpty String entityName, PathMetadata<?> metadata, PathInits inits) {\n");
            builder.append("        super(type, entityName, metadata);\n");
            initEntityFields(builder, model);
            builder.append("    }\n\n"); 
        }
        
        writer.append(builder.toString());
        
    }
        
    protected void constructorsForVariables(StringBuilder builder, EntityModel model) {
        String simpleName = model.getSimpleName();
        String queryType = getQueryType(model, model, true);
        String localName = model.getLocalRawName();
        String genericName = model.getLocalGenericName();
        
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
            builder.append(", INITS");
        }
        builder.append(");\n");
        builder.append("    }\n\n");        
    }


    protected void entityField(PropertyModel field, Writer writer) throws IOException {
        String queryType = getQueryType(field.getType(), field.getEntityModel(), false);
        
        StringBuilder builder = new StringBuilder();
        if (field.isInherited()){
            builder.append("    // inherited\n");
        }       
        builder.append("    public final " + queryType + " " + field.getEscapedName() + ";\n\n");
        writer.append(builder.toString());
    }

    protected void initEntityFields(StringBuilder builder, EntityModel model) {
        EntityModel superModel = model.getSuperModel();
        if (superModel != null && superModel.hasEntityFields()){
            String superQueryType = superModel.getPrefix() + superModel.getSimpleName();
            if (!superModel.getPackageName().equals(model.getPackageName())){
                superQueryType = superModel.getPackageName() + "." + superQueryType;
            }   
            builder.append("        this._super = new " + superQueryType + "(type, entityName, metadata, inits);\n");            
        }
        
        for (PropertyModel field : model.getProperties()){            
            if (field.getType().getTypeCategory() == TypeCategory.ENTITY){
                String queryType = getQueryType(field.getType(), model, false);
                builder.append("        this." + field.getEscapedName() + " = ");
                if (!field.isInherited()){                    
                    builder.append("inits.isInitialized(\""+field.getName()+"\") ? ");
                    builder.append("new " + queryType + "(PathMetadata.forProperty(this,\"" + field.getName() + "\")");
                    if (field.getType().hasEntityFields()){
                        builder.append(", inits.getInits(\""+field.getName()+"\")");    
                    }
                    builder.append(") : null;\n");
                }else{
                    builder.append("_super." + field.getEscapedName() +";\n");
                }   
                
            }else if (field.isInherited() && superModel != null && superModel.hasEntityFields()){
                builder.append("        this." + field.getEscapedName() + " = ");
                builder.append("_super." + field.getEscapedName() + ";\n");
            }
        }        
    }

    protected void intro(EntityModel model, Writer writer) throws IOException {        
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

    protected void introClassHeader(StringBuilder builder, EntityModel model) {
        String queryType = getQueryType(model, model, true);
        String localName = model.getLocalGenericName();
        
        builder.append("@SuppressWarnings(\"serial\")\n");
        builder.append("public class " + queryType + " extends PEntity<" + localName + "> {\n\n");
    }

    protected void introDefaultInstance(StringBuilder builder, EntityModel model) {
        String unscapSimpleName = model.getUncapSimpleName();
        String queryType = getQueryType(model, model, true);
        
        builder.append("    public static final " + queryType + " " + unscapSimpleName + " = new " + queryType + "(\"" + unscapSimpleName + "\");\n\n");
    }

    protected void introFactoryMethods(StringBuilder builder, EntityModel model) throws IOException {
        String localName = model.getLocalRawName();
        String genericName = model.getLocalGenericName();
        
        for (ConstructorModel c : model.getConstructors()){
            // begin
            if (!localName.equals(genericName)){
                builder.append("    @SuppressWarnings(\"unchecked\")\n");
            }            
            builder.append("    public static EConstructor<" + genericName + "> create(");
            boolean first = true;
            for (ParameterModel p : c.getParameters()){
                if (!first) builder.append(", ");
                builder.append("Expr<");
                if (!p.getType().isFinal()){
                    builder.append("? extends ");
                }
                builder = p.getType().getLocalGenericName(model, builder, false);
                builder.append("> ").append(p.getName());
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
                if (p.getType().getPrimitiveName() != null){
                    builder.append(p.getType().getPrimitiveName()+".class");
                }else{
                    builder = p.getType().getLocalRawName(model, builder);
                    builder.append(".class");    
                }                
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

    protected void introImports(StringBuilder builder, EntityModel model) {
        builder.append("import com.mysema.query.util.*;\n");
        builder.append("import com.mysema.query.types.path.*;\n");
        if (!model.getConstructors().isEmpty() || model.hasLists() || model.hasMaps()){
            builder.append("import com.mysema.query.types.expr.*;\n");
        }
    }

    protected void introInits(StringBuilder builder, EntityModel model) {
        if (model.hasEntityFields()){
            List<String> inits = new ArrayList<String>();
            for (PropertyModel property : model.getProperties()){
                if (property.getType().getTypeCategory() == TypeCategory.ENTITY){
                    for (String init : property.getInits()){
                        inits.add(property.getEscapedName() + "." + init);    
                    }   
                }
            }
            
            if (!inits.isEmpty()){
                builder.append("    private static final PathInits INITS = new PathInits(\"*\"");
                for (String init : inits){
                    builder.append(", \"" + init + "\"");    
                }    
                builder.append(");\n\n");    
            }else{
                builder.append("    private static final PathInits INITS = PathInits.DIRECT;\n\n");
            }
                
        }               
    }

    protected void introJavadoc(StringBuilder builder, EntityModel model) {
        String simpleName = model.getSimpleName();
        String queryType = model.getPrefix() + simpleName;
        
        builder.append("/**\n");
        builder.append(" * " + queryType + " is a Querydsl query type for " + simpleName + "\n");
        builder.append(" * \n");
        builder.append(" */ \n");
    }

    protected void introPackage(StringBuilder builder, EntityModel model) {
        builder.append("package " + model.getPackageName() + ";\n\n");
    }
    
    protected void introSuper(StringBuilder builder, EntityModel model) {
        EntityModel superModel = model.getSuperModel();
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

    protected void listAccessor(PropertyModel field, Writer writer) throws IOException {
        String escapedName = field.getEscapedName();
        String queryType = getQueryType(field.getParameter(0), field.getEntityModel(), false);
        
        StringBuilder builder = new StringBuilder();        
        builder.append("    public " + queryType + " " + escapedName + "(int index) {\n");
        builder.append("        return " + escapedName + ".get(index);\n");
        builder.append("    }\n\n");
        builder.append("    public " + queryType + " " + escapedName + "(Expr<Integer> index) {\n");
        builder.append("        return " + escapedName + ".get(index);\n");
        builder.append("    }\n\n");
        writer.append(builder.toString());
    }

    protected void mapAccessor(PropertyModel field, Writer writer) throws IOException {
        String escapedName = field.getEscapedName();
        String queryType = getQueryType(field.getParameter(1), field.getEntityModel(), false);
        String keyType = field.getParameter(0).getLocalGenericName(field.getEntityModel(), false);
        String genericKey = field.getParameter(0).getLocalGenericName(field.getEntityModel(), true);
        
        StringBuilder builder = new StringBuilder();        
        builder.append("    public " + queryType + " " + escapedName + "(" + keyType+ " key) {\n");
        builder.append("        return " + escapedName + ".get(key);\n");
        builder.append("    }\n\n");        
        builder.append("    public " + queryType + " " + escapedName + "(Expr<"+genericKey+"> key) {\n");
        builder.append("        return " + escapedName + ".get(key);\n");
        builder.append("    }\n\n");
        writer.append(builder.toString());        
    }

    protected void outro(EntityModel model, Writer writer) throws IOException {
        writer.write("}\n");        
    }

    public void serialize(EntityModel model, Writer writer) throws IOException{
        intro(model, writer);        
        serializeProperties(model, writer);        
        constructors(model, writer);        
        serializeAccessors(model, writer);
        outro(model, writer);
    }

    protected void serialize(PropertyModel field, String type, Writer writer, String factoryMethod, String... args) throws IOException{
        EntityModel superModel = field.getEntityModel().getSuperModel();
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

    private void serializeAccessors(EntityModel model, Writer writer) throws IOException {
        for (PropertyModel property : model.getProperties()){
            switch(property.getType().getTypeCategory()){
            case MAP: mapAccessor(property, writer); break;
            case LIST: listAccessor(property, writer); break;
            }
        }
    }      
    
    private void serializeProperties(EntityModel model, Writer writer) throws IOException {
        for (PropertyModel property : model.getProperties()){
            String queryType = getQueryType(property.getType(), model, false);
            String localGenericName = property.getType().getLocalGenericName(model, true);
            String localRawName = property.getType().getLocalRawName(model);
            
            switch(property.getType().getTypeCategory()){
            case STRING: 
                serialize(property, queryType, writer, "createString");
                break;
            case BOOLEAN: 
                serialize(property, queryType, writer, "createBoolean"); 
                break;
            case SIMPLE: 
                serialize(property, queryType, writer, "createSimple", localRawName+".class"); 
                break;
            case COMPARABLE: 
                serialize(property, queryType, writer, "createComparable", localRawName + ".class"); 
                break;
            case DATE: 
                serialize(property, queryType, writer, "createDate", localRawName+".class"); 
                break;
            case DATETIME: 
                serialize(property, queryType, writer, "createDateTime", localRawName + ".class"); 
                break;
            case TIME: 
                serialize(property, queryType, writer, "createTime", localRawName+".class"); 
                break;
            case NUMERIC: 
                serialize(property, queryType, writer, "createNumber", localRawName +".class");
                break;
            case COLLECTION: 
                localGenericName = property.getParameter(0).getLocalGenericName(model, true);
                localRawName = property.getParameter(0).getLocalRawName(model);
                serialize(property, "PCollection<" + localGenericName+">", writer, "createCollection",localRawName+".class");
                break;
            case MAP:                 
                String genericKey = property.getParameter(0).getLocalGenericName(model, true);
                String genericValue = property.getParameter(1).getLocalGenericName(model, true);
                String genericQueryType = getQueryType(property.getParameter(1), model, false);
                String keyType = property.getParameter(0).getLocalRawName(model);
                String valueType = property.getParameter(1).getLocalRawName(model);
                queryType = getQueryType(property.getParameter(1), model, true);
                
                // this.<"+genericKey+","+genericValue+","+genericQueryType+"
                serialize(property, "PMap<"+genericKey+","+genericValue+","+genericQueryType+">",
                        writer, "this.<"+genericKey+","+genericValue+","+genericQueryType+">createMap", 
                        keyType+".class", 
                        valueType+".class", 
                        queryType+".class");
                break;
            case LIST:                 
                localGenericName = property.getParameter(0).getLocalGenericName(model, true);                
                genericQueryType = getQueryType(property.getParameter(0), model, false);
                localRawName = property.getParameter(0).getLocalRawName(model);
                queryType = getQueryType(property.getParameter(0), model, true);
                
                serialize(property, "PList<" + localGenericName+ "," + genericQueryType +  ">", writer, "createList", localRawName+".class", queryType +".class");  
                break;
            case ENTITY: 
                entityField(property, writer); 
                break;
            }
        }
    }



}
