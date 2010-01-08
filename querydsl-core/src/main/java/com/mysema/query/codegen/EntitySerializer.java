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

import org.apache.commons.lang.StringEscapeUtils;

import net.jcip.annotations.Immutable;

/**
 * EntitySerializer is a Serializer implementation for entity types
 * 
 * @author tiwe
 *
 */
@Immutable
public class EntitySerializer extends AbstractSerializer{
    
    protected void constructors(EntityModel model, SerializerConfig config, Writer writer) throws IOException {
        String queryType = getPathType(model, model, true);
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
            builder.append("        super(entity.getType(),entity.getMetadata()");
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
            builder.append(localName+".class, metadata);\n");
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
            builder.append(localName + ".class, metadata");
            builder.append(", inits");
            builder.append(");\n");
            builder.append("    }\n\n");    
        }                         
        
        // 5 
        if (hasEntityFields){            
            builder.append("    public "+queryType+"(Class<? extends "+genericName+"> type, PathMetadata<?> metadata, PathInits inits) {\n");
            builder.append("        super(type, metadata, inits);\n");
            initEntityFields(builder, config, model);
            builder.append("    }\n\n"); 
        }
        
        writer.append(builder.toString());
        
    }
        
    protected void constructorsForVariables(StringBuilder builder, EntityModel model) {
        String queryType = getPathType(model, model, true);
        String localName = model.getLocalRawName();
        String genericName = model.getLocalGenericName();
        
        boolean hasEntityFields = model.hasEntityFields();
        String thisOrSuper = hasEntityFields ? "this" : "super";
        
        if (!localName.equals(genericName)){
            builder.append("    @SuppressWarnings(\"unchecked\")\n");
        }        
        builder.append("    public " + queryType + "(String variable) {\n");
        builder.append("        "+thisOrSuper+"(");
        if (!localName.equals(genericName)){
            builder.append("(Class)");   
        }
        builder.append(localName + ".class, forVariable(variable)");
        if (hasEntityFields){
            builder.append(", INITS");
        }
        builder.append(");\n");
        builder.append("    }\n\n");        
    }
    
    protected void entityField(PropertyModel field, SerializerConfig config, Writer writer) throws IOException {
        String queryType = getPathType(field.getType(), field.getContext(), false);
        
        StringBuilder builder = new StringBuilder();
        if (field.isInherited()){
            builder.append("    // inherited\n");
        }       
        if (config.useEntityAccessors()){
            builder.append("    protected ");
        }else{
            builder.append("    public final ");    
        }        
        builder.append(queryType + " " + field.getEscapedName() + ";\n\n");
        writer.append(builder.toString());
    }
    

    protected void entityAccessor(PropertyModel field, Writer writer) throws IOException {
        String queryType = getPathType(field.getType(), field.getContext(), false);
        
        StringBuilder builder = new StringBuilder();
        builder.append("    public " + queryType + " " + field.getEscapedName() + "(){\n");
        builder.append("        if (" + field.getEscapedName() + " == null){\n");
        builder.append("            " + field.getEscapedName() + " = new " + queryType + "(forProperty(\"" + field.getName() + "\"));\n");
        builder.append("        }\n");
        builder.append("        return " + field.getEscapedName()+";\n");
        builder.append("    }\n\n");
        writer.append(builder.toString());
    }


    protected boolean hasOwnEntityProperties(EntityModel model){
        if (model.hasEntityFields()){
            for (PropertyModel property : model.getProperties()){
                if (!property.isInherited() && property.getType().getCategory() == TypeCategory.ENTITY){
                    return true;
                }
            }    
        }        
        return false;
    }

    protected void initEntityFields(StringBuilder builder, SerializerConfig config, EntityModel model) {
        EntityModel superModel = model.getSuperModel();
        if (superModel != null && superModel.hasEntityFields()){
            String superQueryType = getPathType(superModel, model, false);
            builder.append("        this._super = new " + superQueryType + "(type, metadata, inits);\n");            
        }
        
        for (PropertyModel field : model.getProperties()){            
            if (field.getType().getCategory() == TypeCategory.ENTITY){
                String queryType = getPathType(field.getType(), model, false);                               
                if (!field.isInherited()){                    
                    builder.append("        this." + field.getEscapedName() + " = ");
                    builder.append("inits.isInitialized(\""+field.getName()+"\") ? ");
                    builder.append("new " + queryType + "(forProperty(\"" + field.getName() + "\")");
                    if (field.getType().hasEntityFields()){
                        builder.append(", inits.get(\""+field.getName()+"\")");    
                    }
                    builder.append(") : null;\n");
                }else if (!config.useEntityAccessors()){
                    builder.append("        this." + field.getEscapedName() + " = ");
                    builder.append("_super." + field.getEscapedName() +";\n");
                }   
                
            }else if (field.isInherited() && superModel != null && superModel.hasEntityFields()){
                builder.append("        this." + field.getEscapedName() + " = ");
                builder.append("_super." + field.getEscapedName() + ";\n");
            }
        }        
    }

    protected void intro(EntityModel model, SerializerConfig config, Writer writer) throws IOException {        
        StringBuilder builder = new StringBuilder();        
        introPackage(builder, model);        
        introImports(builder, config, model);        
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
        String queryType = getPathType(model, model, true);
        String localName = model.getLocalGenericName();
        
        builder.append("@SuppressWarnings(\"serial\")\n");
        builder.append("public class " + queryType + " extends PEntity<" + localName + "> {\n\n");
    }

    protected void introDefaultInstance(StringBuilder builder, EntityModel model) {
        String unscapSimpleName = model.getUncapSimpleName();
        String queryType = getPathType(model, model, true);
        
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
                builder.append(getExprType(p.getType(), model, false));
                builder.append(" ").append(p.getName());
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

    protected void introImports(StringBuilder builder, SerializerConfig config, EntityModel model) {        
        builder.append("import com.mysema.query.types.path.*;\n");
        builder.append("import static com.mysema.query.types.path.PathMetadataFactory.*;\n");        
        
        if (!model.getConstructors().isEmpty()
                || !model.getMethods().isEmpty()
                || (model.hasLists() && config.useListAccessors()) 
                || (model.hasMaps() && config.useMapAccessors())){
            builder.append("import com.mysema.query.types.expr.*;\n");
        }
        
        if (!model.getMethods().isEmpty()){
            builder.append("import com.mysema.query.types.custom.*;\n");
        }
    }

    protected void introInits(StringBuilder builder, EntityModel model) {
        if (model.hasEntityFields()){
            List<String> inits = new ArrayList<String>();
            for (PropertyModel property : model.getProperties()){
                if (property.getType().getCategory() == TypeCategory.ENTITY){
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
        String superQueryType = getPathType(superModel, model, false);
        
        if (!superModel.hasEntityFields()){
            builder.append("    public final "+superQueryType+" _super = new " + superQueryType + "(this);\n\n");    
        }else{
            builder.append("    public final "+superQueryType+" _super;\n\n");    
        }                  
    }  

    protected void listAccessor(PropertyModel field, Writer writer) throws IOException {
        String escapedName = field.getEscapedName();
        String queryType = getPathType(field.getParameter(0), field.getContext(), false);
        
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
        String queryType = getPathType(field.getParameter(1), field.getContext(), false);
        String keyType = field.getParameter(0).getLocalGenericName(field.getContext(), false);
        String genericKey = field.getParameter(0).getLocalGenericName(field.getContext(), true);
        
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

    public void serialize(EntityModel model, SerializerConfig config, Writer writer) throws IOException{
        intro(model, config, writer);        
        
        // properties
        serializeProperties(model, config, writer);        
        
        // constructors
        constructors(model, config, writer);        

        // methods
        for (MethodModel method : model.getMethods()){
            method(model, method, config, writer);
        }
        
        // property accessors
        for (PropertyModel property : model.getProperties()){
            TypeCategory category = property.getType().getCategory();
            if (category == TypeCategory.MAP && config.useMapAccessors()){
                mapAccessor(property, writer);
            }else if (category == TypeCategory.LIST && config.useListAccessors()){
                listAccessor(property, writer);
            }else if (category == TypeCategory.ENTITY && config.useEntityAccessors()){
                entityAccessor(property, writer);
            }
        }
        outro(model, writer);
    }


    protected void method(EntityModel model, MethodModel method, SerializerConfig config, Writer writer) throws IOException {
        StringBuilder builder = new StringBuilder();
        String returnType = getExprType(method.getReturnType(), model, false);
        builder.append("    public " + returnType + " " + method.getName() + "(");
        boolean first = true;        
        // parameters
        for (ParameterModel p : method.getParameters()){
            if (!first) builder.append(", ");
            String paramType = getExprType(p.getType(), model, false);
            builder.append(paramType + " " + p.getName());           
            first = false;
        }
        builder.append("){\n");
        String customClass = getCustomType(method.getReturnType(), model, true);        
        builder.append("        return " + customClass + ".create(");
        String fullName = method.getReturnType().getFullName();
        if (!fullName.equals(String.class.getName()) && !fullName.equals(Boolean.class.getName())){
            method.getReturnType().getLocalRawName(model, builder);
            builder.append(".class, ");
        }        
        builder.append("\"" + StringEscapeUtils.escapeJava(method.getTemplate()) + "\"");
        builder.append(", this");
        for (ParameterModel p : method.getParameters()){
            builder.append(", " + p.getName());
        }        
        builder.append(");\n");
        builder.append("    }\n\n");
        writer.write(builder.toString());
    }

    protected void serialize(PropertyModel field, String type, Writer writer, String factoryMethod, String... args) throws IOException{
        EntityModel superModel = field.getContext().getSuperModel();
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

    private void serializeProperties(EntityModel model,  SerializerConfig config, Writer writer) throws IOException {
        for (PropertyModel property : model.getProperties()){
            String queryType = getPathType(property.getType(), model, false);
            String localGenericName = property.getType().getLocalGenericName(model, true);
            String localRawName = property.getType().getLocalRawName(model);
            
            switch(property.getType().getCategory()){
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
            case ARRAY:    
                // TODO : support for typed arrays
                localGenericName = property.getParameter(0).getLocalGenericName(model, true);
                localRawName = property.getParameter(0).getLocalRawName(model);
                serialize(property, "PArray<" + localGenericName+">", writer, "createArray",localRawName+".class");
                break;
            case COLLECTION: 
                localGenericName = property.getParameter(0).getLocalGenericName(model, true);
                localRawName = property.getParameter(0).getLocalRawName(model);
                serialize(property, "PCollection<" + localGenericName+">", writer, "createCollection",localRawName+".class");
                break;
            case SET: 
                localGenericName = property.getParameter(0).getLocalGenericName(model, true);
                localRawName = property.getParameter(0).getLocalRawName(model);
                serialize(property, "PSet<" + localGenericName+">", writer, "createSet",localRawName+".class");
                break;
            case MAP:                 
                String genericKey = property.getParameter(0).getLocalGenericName(model, true);
                String genericValue = property.getParameter(1).getLocalGenericName(model, true);
                String genericQueryType = getPathType(property.getParameter(1), model, false);
                String keyType = property.getParameter(0).getLocalRawName(model);
                String valueType = property.getParameter(1).getLocalRawName(model);
                queryType = getPathType(property.getParameter(1), model, true);
                
                // this.<"+genericKey+", "+genericValue+", "+genericQueryType+"
                serialize(property, "PMap<"+genericKey+", "+genericValue+", "+genericQueryType+">",
                        writer, "this.<"+genericKey+", "+genericValue+", "+genericQueryType+">createMap", 
                        keyType+".class", 
                        valueType+".class", 
                        queryType+".class");
                break;
            case LIST:                 
                localGenericName = property.getParameter(0).getLocalGenericName(model, true);                
                genericQueryType = getPathType(property.getParameter(0), model, false);
                localRawName = property.getParameter(0).getLocalRawName(model);
                queryType = getPathType(property.getParameter(0), model, true);
                
                serialize(property, "PList<" + localGenericName+ ", " + genericQueryType +  ">", writer, "createList", localRawName+".class", queryType +".class");  
                break;
            case ENTITY: 
                entityField(property, config, writer); 
                break;
            }
        }
    }



}
