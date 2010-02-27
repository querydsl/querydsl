/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import net.jcip.annotations.Immutable;

import org.apache.commons.collections15.Transformer;
import org.apache.commons.lang.StringEscapeUtils;

import com.mysema.commons.lang.Assert;
import com.mysema.query.types.custom.Custom;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.path.PComparable;
import com.mysema.query.types.path.PDate;
import com.mysema.query.types.path.PDateTime;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.PNumber;
import com.mysema.query.types.path.PTime;
import com.mysema.query.types.path.Path;
import com.mysema.query.types.path.PathMetadataFactory;
import com.mysema.util.CodeWriter;

/**
 * EntitySerializer is a Serializer implementation for entity types
 * 
 * @author tiwe
 *
 */
@Immutable
public class EntitySerializer implements Serializer{
    
    private static final String UNCHECKED = "unchecked";
    
    private static final String PATH_METADATA = "PathMetadata<?> metadata";
    
    private final TypeMappings typeMappings;
    
    public EntitySerializer(TypeMappings mappings){
        this.typeMappings = Assert.notNull(mappings);
    }
    
    protected void constructors(EntityModel model, SerializerConfig config, CodeWriter writer) throws IOException {
        String localName = model.getLocalRawName();
        String genericName = model.getLocalGenericName();
        
        boolean hasEntityFields = model.hasEntityFields();
        String thisOrSuper = hasEntityFields ? "this" : "super";
        
        // 1
        constructorsForVariables(writer, model);    

        // 2
        if (!hasEntityFields){
            writer.beginConstructor("PEntity<? extends "+genericName+"> entity");
            writer.line("super(entity.getType(),entity.getMetadata());");
            writer.end();                
        }        
        
        // 3        
        if (hasEntityFields){
            writer.beginConstructor(PATH_METADATA);
            writer.line("this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);");
            writer.end();
        }else{
            if (!localName.equals(genericName)){
                writer.suppressWarnings(UNCHECKED);
            }
            writer.beginConstructor(PATH_METADATA);
            writer.line("super(",localName.equals(genericName) ? "" : "(Class)",localName,".class, metadata);");
            writer.end();
        }               
        
        // 4
        if (hasEntityFields){
            if (!localName.equals(genericName)){
                writer.suppressWarnings(UNCHECKED);
            }        
            writer.beginConstructor(PATH_METADATA, "PathInits inits");
            writer.line(thisOrSuper, "(", localName.equals(genericName) ? "" : "(Class)", localName, ".class, metadata, inits);");
            writer.end();
        }                         
        
        // 5 
        if (hasEntityFields){            
            writer.beginConstructor("Class<? extends "+genericName+"> type", PATH_METADATA, "PathInits inits");
            writer.line("super(type, metadata, inits);");
            initEntityFields(writer, config, model); 
            writer.end();
        }
        
        
    }
        
    protected void constructorsForVariables(CodeWriter writer, EntityModel model) throws IOException {
        String localName = model.getLocalRawName();
        String genericName = model.getLocalGenericName();
        
        boolean hasEntityFields = model.hasEntityFields();
        String thisOrSuper = hasEntityFields ? "this" : "super";
        
        if (!localName.equals(genericName)){
            writer.suppressWarnings(UNCHECKED);
        }        
        writer.beginConstructor("String variable");      
        writer.line(thisOrSuper,"(", localName.equals(genericName) ? "" : "(Class)",
                localName, ".class, forVariable(variable)", hasEntityFields ? ", INITS" : "", ");");
        writer.end();
    }
    
    protected void entityAccessor(PropertyModel field, CodeWriter writer) throws IOException {
        String queryType = typeMappings.getPathType(field.getType(), field.getContext(), false);
        
        writer.beginMethod(queryType, field.getEscapedName());
        writer.lines("if (" + field.getEscapedName() + " == null){",
                     "    " + field.getEscapedName() + " = new " + queryType + "(forProperty(\"" + field.getName() + "\"));",
                     "}",
                     "return " + field.getEscapedName()+";");
        writer.end();
    }
    

    protected void entityField(PropertyModel field, SerializerConfig config, CodeWriter writer) throws IOException {
        String queryType = typeMappings.getPathType(field.getType(), field.getContext(), false);        
        if (field.isInherited()){
            writer.line("// inherited");
        }       
        if (config.useEntityAccessors()){
            writer.protectedField(queryType, field.getEscapedName());
        }else{
            writer.publicFinal(queryType, field.getEscapedName());    
        }                
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

    protected void initEntityFields(CodeWriter writer, SerializerConfig config, EntityModel model) throws IOException {        
        EntityModel superModel = model.getSuperModel();
        if (superModel != null && superModel.hasEntityFields()){
            String superQueryType = typeMappings.getPathType(superModel, model, false);
            writer.line("this._super = new " + superQueryType + "(type, metadata, inits);");            
        }
        
        for (PropertyModel field : model.getProperties()){            
            if (field.getType().getCategory() == TypeCategory.ENTITY){
                String queryType = typeMappings.getPathType(field.getType(), model, false);                               
                if (!field.isInherited()){          
                    writer.line("this." + field.getEscapedName() + " = ",
                        "inits.isInitialized(\""+field.getName()+"\") ? ",
                        "new " + queryType + "(forProperty(\"" + field.getName() + "\")",
                        field.getType().hasEntityFields() ? (", inits.get(\""+field.getName()+"\")") : "",
                        ") : null;");
                }else if (!config.useEntityAccessors()){
                    writer.line("this." + field.getEscapedName() + " = ", "_super." + field.getEscapedName() +";");
                }   
                
            }else if (field.isInherited() && superModel != null && superModel.hasEntityFields()){
                writer.line("this." + field.getEscapedName() + " = _super." + field.getEscapedName() + ";");
            }
        }        
    }

    protected void intro(EntityModel model, SerializerConfig config, CodeWriter writer) throws IOException {                
        introPackage(writer, model);        
        introImports(writer, config, model);
        writer.nl();
        
        introJavadoc(writer, model);        
        introClassHeader(writer, model);        
        
        introFactoryMethods(writer, model);   
        introInits(writer, model);
        if (config.createDefaultVariable()){
            introDefaultInstance(writer, model);    
        }           
        if (model.getSuperModel() != null){
            introSuper(writer, model);    
        }        
    }

    @SuppressWarnings(UNCHECKED)
    protected void introClassHeader(CodeWriter writer, EntityModel model) throws IOException {
        String queryType = typeMappings.getPathType(model, model, true);
        String localName = model.getLocalGenericName();
        
        TypeCategory category = model.getOriginalCategory();
        Class<? extends Path> pathType;
        switch(category){
            case COMPARABLE : pathType = PComparable.class; break;
            case DATE: pathType = PDate.class; break;
            case DATETIME: pathType = PDateTime.class; break;
            case TIME: pathType = PTime.class; break;
            case NUMERIC: pathType = PNumber.class; break;
            default : pathType = PEntity.class;
        }        
        writer.suppressWarnings("serial");        
        for (Annotation annotation : model.getAnnotations()){
            writer.annotation(annotation);
        }        
        writer.beginClass(queryType, pathType.getSimpleName() + "<" + localName + ">");
    }

    protected void introDefaultInstance(CodeWriter writer, EntityModel model) throws IOException {
        String simpleName = model.getUncapSimpleName();
        String queryType = typeMappings.getPathType(model, model, true);
        
        writer.publicStaticFinal(queryType, simpleName, "new " + queryType + "(\"" + simpleName + "\")");
    }

    protected void introFactoryMethods(CodeWriter writer, final EntityModel model) throws IOException {
        String localName = model.getLocalRawName();
        String genericName = model.getLocalGenericName();
        
        for (ConstructorModel c : model.getConstructors()){
            // begin
            if (!localName.equals(genericName)){
                writer.suppressWarnings(UNCHECKED);
            }            
            writer.beginStaticMethod("EConstructor<" + genericName + ">", "create", c.getParameters(), new Transformer<ParameterModel, String>(){
                @Override
                public String transform(ParameterModel p) {
                    return typeMappings.getExprType(p.getType(), model, false, false, true) + " " + p.getName();
                }                
            });
            
            // body
            writer.beginLine("return new EConstructor<" + genericName + ">(");
            if (!localName.equals(genericName)){
                writer.append("(Class)");
            }
            writer.append(localName + ".class");
            writer.append(", new Class[]{");
            boolean first = true;
            for (ParameterModel p : c.getParameters()){
                if (!first){
                    writer.append(", ");
                }
                if (p.getType().getPrimitiveName() != null){
                    writer.append(p.getType().getPrimitiveName()+".class");
                }else{
                    p.getType().appendLocalRawName(model, writer);
                    writer.append(".class");    
                }                
                first = false;
            }
            writer.append("}");
            
            for (ParameterModel p : c.getParameters()){
                writer.append(", " + p.getName());
            }
            
            // end
            writer.append(");\n");
            writer.end();
        }        
    }

    protected void introImports(CodeWriter writer, SerializerConfig config, EntityModel model) throws IOException {       
        writer.imports(Path.class.getPackage());
        writer.staticimports(PathMetadataFactory.class);        
        
        if (!model.getConstructors().isEmpty()
                || !model.getMethods().isEmpty()
                || (model.hasLists() && config.useListAccessors()) 
                || (model.hasMaps() && config.useMapAccessors())){
            writer.imports(Expr.class.getPackage());
        }
        
        if (!model.getMethods().isEmpty()){
            writer.imports(Custom.class.getPackage());
        }
    }

    protected void introInits(CodeWriter writer, EntityModel model) throws IOException {
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
                inits.add(0, "*");
                writer.privateStaticFinal("PathInits", "INITS", "new PathInits(" + writer.join("\"", "\"", inits) + ")"); 
            }else{
                writer.privateStaticFinal("PathInits", "INITS", "PathInits.DIRECT");
            }
                
        }               
    }

    protected void introJavadoc(CodeWriter writer, EntityModel model) throws IOException {
        String simpleName = model.getSimpleName();
        String queryType = model.getPrefix() + simpleName;        
        writer.javadoc(queryType + " is a Querydsl query type for " + simpleName);
    }

    protected void introPackage(CodeWriter writer, EntityModel model) throws IOException {
        writer.packageDecl(model.getPackageName());
        writer.nl();
    }
    
    protected void introSuper(CodeWriter writer, EntityModel model) throws IOException {
        EntityModel superModel = model.getSuperModel();
        String superQueryType = typeMappings.getPathType(superModel, model, false);
        
        if (!superModel.hasEntityFields()){
            writer.publicFinal(superQueryType, "_super", "new " + superQueryType + "(this)");    
        }else{
            writer.publicFinal(superQueryType, "_super");    
        }                  
    }  

    protected void listAccessor(PropertyModel field, CodeWriter writer) throws IOException {
        String escapedName = field.getEscapedName();
        String queryType = typeMappings.getPathType(field.getParameter(0), field.getContext(), false);

        writer.beginMethod(queryType, escapedName, "int index");
        writer.line("return " + escapedName + ".get(index);").end();
        
        writer.beginMethod(queryType, escapedName, "Expr<Integer> index");
        writer.line("return " + escapedName +".get(index);").end();
    }

    protected void mapAccessor(PropertyModel field, CodeWriter writer) throws IOException {
        String escapedName = field.getEscapedName();
        String queryType = typeMappings.getPathType(field.getParameter(1), field.getContext(), false);
        String keyType = field.getParameter(0).getLocalGenericName(field.getContext(), false);
        String genericKey = field.getParameter(0).getLocalGenericName(field.getContext(), true);
        
        writer.beginMethod(queryType, escapedName, keyType + " key");
        writer.line("return " + escapedName + ".get(key);").end();
        
        writer.beginMethod(queryType, escapedName, "Expr<" + genericKey + "> key");
        writer.line("return " + escapedName + ".get(key);").end();
    }

    protected void method(final EntityModel model, MethodModel method, SerializerConfig config, CodeWriter writer) throws IOException {
        // header
        String type = typeMappings.getExprType(method.getReturnType(), model, false, true, false);
        writer.beginMethod(type, method.getName(), method.getParameters(), new Transformer<ParameterModel,String>(){
            @Override
            public String transform(ParameterModel p) {
                return typeMappings.getExprType(p.getType(), model, false, false, true) + " " + p.getName();
            }            
        });
        
        // body start
        String customClass = typeMappings.getCustomType(method.getReturnType(), model, true);        
        writer.beginLine("return " + customClass + ".create(");
        String fullName = method.getReturnType().getFullName();
        if (!fullName.equals(String.class.getName()) && !fullName.equals(Boolean.class.getName())){
            method.getReturnType().appendLocalRawName(model, writer);
            writer.append(".class, ");
        }        
        writer.append("\"" + StringEscapeUtils.escapeJava(method.getTemplate()) + "\"");
        writer.append(", this");
        for (ParameterModel p : method.getParameters()){
            writer.append(", " + p.getName());
        }        
        writer.append(");\n");

        // body end
        writer.end();
    }

    protected void outro(EntityModel model, CodeWriter writer) throws IOException {
        writer.end();        
    }

    public void serialize(EntityModel model, SerializerConfig config, CodeWriter writer) throws IOException{
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

    protected void serialize(PropertyModel field, String type, CodeWriter writer, String factoryMethod, String... args) throws IOException{
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
        if (field.isInherited()){
            writer.line("//inherited");
        }        
        if (value.length() > 0){
            writer.publicFinal(type, field.getEscapedName(), value.toString());    
        }else{
            writer.publicFinal(type, field.getEscapedName());
        }        
    }

    private void serializeProperties(EntityModel model,  SerializerConfig config, CodeWriter writer) throws IOException {
        for (PropertyModel property : model.getProperties()){
            String queryType = typeMappings.getPathType(property.getType(), model, false);
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
                localGenericName = property.getParameter(0).getLocalGenericName(model, true);
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
                String genericQueryType = typeMappings.getPathType(property.getParameter(1), model, false);
                String keyType = property.getParameter(0).getLocalRawName(model);
                String valueType = property.getParameter(1).getLocalRawName(model);
                queryType = typeMappings.getPathType(property.getParameter(1), model, true);
                
                serialize(property, "PMap<"+genericKey+", "+genericValue+", "+genericQueryType+">",
                        writer, "this.<"+genericKey+", "+genericValue+", "+genericQueryType+">createMap", 
                        keyType+".class", 
                        valueType+".class", 
                        queryType+".class");
                break;
            case LIST:                 
                localGenericName = property.getParameter(0).getLocalGenericName(model, true);                
                genericQueryType = typeMappings.getPathType(property.getParameter(0), model, false);
                localRawName = property.getParameter(0).getLocalRawName(model);
                queryType = typeMappings.getPathType(property.getParameter(0), model, true);
                
                serialize(property, "PList<" + localGenericName+ ", " + genericQueryType +  ">", writer, "createList", localRawName+".class", queryType +".class");  
                break;
            case ENTITY: 
                entityField(property, config, writer); 
                break;
            }
        }
    }



}
