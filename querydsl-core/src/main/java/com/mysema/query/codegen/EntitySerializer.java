/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.codegen;

import static com.mysema.codegen.Symbols.*;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.jcip.annotations.Immutable;

import org.apache.commons.collections15.Transformer;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import com.mysema.codegen.CodeWriter;
import com.mysema.codegen.model.ClassType;
import com.mysema.codegen.model.Constructor;
import com.mysema.codegen.model.Parameter;
import com.mysema.codegen.model.SimpleType;
import com.mysema.codegen.model.Type;
import com.mysema.codegen.model.TypeCategory;
import com.mysema.codegen.model.TypeExtends;
import com.mysema.codegen.model.Types;
import com.mysema.commons.lang.Assert;
import com.mysema.query.types.ConstructorExpression;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.PathMetadataFactory;
import com.mysema.query.types.expr.ComparableExpression;
import com.mysema.query.types.path.*;
import com.mysema.query.types.template.SimpleTemplate;

/**
 * EntitySerializer is a Serializer implementation for entity types
 *
 * @author tiwe
 *
 */
@Immutable
public class EntitySerializer implements Serializer{

    private static final Parameter PATH_METADATA = new Parameter("metadata", new ClassType(PathMetadata.class, (Type)null));

    private static final Parameter PATH_INITS = new Parameter("inits", new ClassType(PathInits.class));

    protected final TypeMappings typeMappings;

    protected final Collection<String> keywords;

    public EntitySerializer(TypeMappings mappings, Collection<String> keywords){
        this.typeMappings = Assert.notNull(mappings,"mappings");
        this.keywords = Assert.notNull(keywords,"keywords");
    }

    protected void constructors(EntityType model, SerializerConfig config, CodeWriter writer) throws IOException {
        String localName = writer.getRawName(model);
        String genericName = writer.getGenericName(true, model);

        boolean hasEntityFields = model.hasEntityFields();
        String thisOrSuper = hasEntityFields ? THIS : SUPER;

        boolean stringOrBoolean = model.getOriginalCategory() == TypeCategory.STRING || model.getOriginalCategory() == TypeCategory.BOOLEAN;

        // 1
        constructorsForVariables(writer, model);

        // 2
        if (!hasEntityFields){
            if (model.isFinal()){
                Type type = new ClassType(BeanPath.class, model);
                writer.beginConstructor(new Parameter("entity", type));
            }else{
                Type type = new ClassType(BeanPath.class, new TypeExtends(model));
                writer.beginConstructor(new Parameter("entity", type));
            }
            if (stringOrBoolean){
                writer.line("super(entity.getMetadata());");
            }else{
                writer.line("super(entity.getType(), entity.getMetadata());");
            }
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
            if (stringOrBoolean){
                writer.line("super(metadata);");
            }else{
                writer.line("super(", localName.equals(genericName) ? EMPTY : "(Class)", localName, ".class, metadata);");
            }
            writer.end();
        }

        // 4
        if (hasEntityFields){
            if (!localName.equals(genericName)){
                writer.suppressWarnings(UNCHECKED);
            }
            writer.beginConstructor(PATH_METADATA, PATH_INITS);
            writer.line(thisOrSuper, "(", localName.equals(genericName) ? EMPTY : "(Class)", localName, ".class, metadata, inits);");
            writer.end();
        }

        // 5
        if (hasEntityFields){
            Type type;
            if (model.isFinal()){
                type = new ClassType(Class.class, model);
            }else{
                type = new ClassType(Class.class, new TypeExtends(model));
            }
            writer.beginConstructor(new Parameter("type", type), PATH_METADATA, PATH_INITS);
            writer.line("super(type, metadata, inits);");
            initEntityFields(writer, config, model);
            writer.end();
        }

    }

    protected void constructorsForVariables(CodeWriter writer, EntityType model) throws IOException {
        String localName = writer.getRawName(model);
        String genericName = writer.getGenericName(true, model);

        boolean hasEntityFields = model.hasEntityFields();
        String thisOrSuper = hasEntityFields ? THIS : SUPER;

        if (!localName.equals(genericName)){
            writer.suppressWarnings(UNCHECKED);
        }
        writer.beginConstructor(new Parameter("variable", Types.STRING));
        writer.line(thisOrSuper,"(", localName.equals(genericName) ? EMPTY : "(Class)",
                localName, ".class, forVariable(variable)", hasEntityFields ? ", INITS" : EMPTY, ");");
        writer.end();
    }

    protected void entityAccessor(EntityType model, Property field, CodeWriter writer) throws IOException {
        Type queryType = typeMappings.getPathType(field.getType(), model, false);
        writer.beginPublicMethod(queryType, field.getEscapedName());
        writer.line("if (", field.getEscapedName(), " == null){");
        writer.line("    ", field.getEscapedName(), " = new ", writer.getRawName(queryType), "(forProperty(\"", field.getName(), "\"));");
        writer.line("}");
        writer.line(RETURN, field.getEscapedName(), SEMICOLON);
        writer.end();
    }

    protected void entityField(EntityType model, Property field, SerializerConfig config, CodeWriter writer) throws IOException {
        Type queryType = typeMappings.getPathType(field.getType(), model, false);
        if (field.isInherited()){
            writer.line("// inherited");
        }
        if (config.useEntityAccessors()){
            writer.protectedField(queryType, field.getEscapedName());
        }else{
            writer.publicFinal(queryType, field.getEscapedName());
        }
    }

    protected boolean hasOwnEntityProperties(EntityType model){
        if (model.hasEntityFields()){
            for (Property property : model.getProperties()){
                if (!property.isInherited() && property.getType().getCategory() == TypeCategory.ENTITY){
                    return true;
                }
            }
        }
        return false;
    }

    protected void initEntityFields(CodeWriter writer, SerializerConfig config, EntityType model) throws IOException {
        Supertype superType = model.getSuperType();
        if (superType != null && superType.getEntityType().hasEntityFields()){
            Type superQueryType = typeMappings.getPathType(superType.getEntityType(), model, false);
            writer.line("this._super = new " + writer.getRawName(superQueryType) + "(type, metadata, inits);");
        }

        for (Property field : model.getProperties()){
            if (field.getType().getCategory() == TypeCategory.ENTITY){
                initEntityField(writer, config, model, field);

            }else if (field.isInherited() && superType != null && superType.getEntityType().hasEntityFields()){
                writer.line("this.", field.getEscapedName(), " = _super.", field.getEscapedName(), SEMICOLON);
            }
        }
    }

    protected void initEntityField(CodeWriter writer, SerializerConfig config, EntityType model, Property field) throws IOException {
        Type queryType = typeMappings.getPathType(field.getType(), model, false);
        if (!field.isInherited()){
            boolean hasEntityFields = field.getType() instanceof EntityType && ((EntityType)field.getType()).hasEntityFields();
            writer.line("this." + field.getEscapedName() + ASSIGN,
                "inits.isInitialized(\""+field.getName()+"\") ? ",
                NEW + writer.getRawName(queryType) + "(forProperty(\"" + field.getName() + "\")",
                hasEntityFields ? (", inits.get(\""+field.getName()+"\")") : EMPTY,
                ") : null;");
        }else if (!config.useEntityAccessors()){
            writer.line("this.", field.getEscapedName(), ASSIGN, "_super.", field.getEscapedName(), SEMICOLON);
        }
    }

    protected void intro(EntityType model, SerializerConfig config, CodeWriter writer) throws IOException {
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
        if (model.getSuperType() != null){
            introSuper(writer, model);
        }
    }

    @SuppressWarnings(UNCHECKED)
    protected void introClassHeader(CodeWriter writer, EntityType model) throws IOException {
        Type queryType = typeMappings.getPathType(model, model, true);

        TypeCategory category = model.getOriginalCategory();
        Class<? extends Path> pathType;

        if (model.getProperties().isEmpty()){
            switch(category){
                case COMPARABLE : pathType = ComparablePath.class; break;
                case ENUM: pathType = EnumPath.class; break;
                case DATE: pathType = DatePath.class; break;
                case DATETIME: pathType = DateTimePath.class; break;
                case TIME: pathType = TimePath.class; break;
                case NUMERIC: pathType = NumberPath.class; break;
                case STRING: pathType = StringPath.class; break;
                case BOOLEAN: pathType = BooleanPath.class; break;
                default : pathType = EntityPathBase.class;
            }
        }else{
            pathType = EntityPathBase.class;
        }

        for (Annotation annotation : model.getAnnotations()){
            writer.annotation(annotation);
        }

        if (category == TypeCategory.BOOLEAN || category == TypeCategory.STRING){
            writer.beginClass(queryType, new ClassType(pathType));
        }else{
            writer.beginClass(queryType, new ClassType(category, pathType, model));
        }

        // TODO : generate proper serialVersionUID here
        writer.privateStaticFinal(Types.LONG_P, "serialVersionUID", String.valueOf(model.hashCode()));
    }

    protected void introDefaultInstance(CodeWriter writer, EntityType model) throws IOException {
        String simpleName = model.getUncapSimpleName();
        Type queryType = typeMappings.getPathType(model, model, true);
        String alias = simpleName;
        if (keywords.contains(simpleName.toUpperCase())){
            alias += "1";
        }
        writer.publicStaticFinal(queryType, simpleName, NEW + queryType.getSimpleName() + "(\"" + alias + "\")");

    }

    protected void introFactoryMethods(CodeWriter writer, final EntityType model) throws IOException {
        String localName = writer.getRawName(model);
        String genericName = writer.getGenericName(true, model);

        for (Constructor c : model.getConstructors()){
            // begin
            if (!localName.equals(genericName)){
                writer.suppressWarnings(UNCHECKED);
            }
            Type returnType = new ClassType(ConstructorExpression.class, model);
            writer.beginStaticMethod(returnType, "create", c.getParameters(), new Transformer<Parameter, Parameter>(){
                @Override
                public Parameter transform(Parameter p) {
                    return new Parameter(p.getName(), typeMappings.getExprType(p.getType(), model, false, false, true));
                }
            });

            // body
            // TODO : replace with class reference
            writer.beginLine("return new ConstructorExpression<" + genericName + ">(");
            if (!localName.equals(genericName)){
                writer.append("(Class)");
            }
            writer.append(localName + DOT_CLASS);
            writer.append(", new Class[]{");
            boolean first = true;
            for (Parameter p : c.getParameters()){
                if (!first){
                    writer.append(COMMA);
                }
                if (p.getType().getPrimitiveName() != null){
                    writer.append(p.getType().getPrimitiveName()+DOT_CLASS);
                }else{
                    writer.append(writer.getRawName(p.getType()));
                    writer.append(DOT_CLASS);
                }
                first = false;
            }
            writer.append("}");

            for (Parameter p : c.getParameters()){
                writer.append(COMMA + p.getName());
            }

            // end
            writer.append(");\n");
            writer.end();
        }
    }

    protected void introImports(CodeWriter writer, SerializerConfig config, EntityType model) throws IOException {
        writer.staticimports(PathMetadataFactory.class);

        introDelegatePackages(writer, model);

        List<Package> packages = new ArrayList<Package>();
        packages.add(PathMetadata.class.getPackage());
        packages.add(SimplePath.class.getPackage());
        if (!model.getConstructors().isEmpty() || !model.getDelegates().isEmpty()){
            packages.add(ComparableExpression.class.getPackage());
        }

        writer.imports(packages.toArray(new Package[packages.size()]));
    }

    protected void introDelegatePackages(CodeWriter writer, EntityType model) throws IOException {
        Set<String> packages = new HashSet<String>();
        for (Delegate delegate : model.getDelegates()){
            if (!delegate.getDelegateType().getPackageName().equals(model.getPackageName())){
                packages.add(delegate.getDelegateType().getPackageName());
            }
        }
        writer.importPackages(packages.toArray(new String[packages.size()]));
    }

    protected void introInits(CodeWriter writer, EntityType model) throws IOException {
        if (model.hasEntityFields()){
            List<String> inits = new ArrayList<String>();
            for (Property property : model.getProperties()){
                if (property.getType().getCategory() == TypeCategory.ENTITY){
                    for (String init : property.getInits()){
                        inits.add(property.getEscapedName() + DOT + init);
                    }
                }
            }
            ClassType pathInitsType = new ClassType(PathInits.class);
            if (!inits.isEmpty()){
                inits.add(0, STAR);
                String initsAsString = QUOTE + StringUtils.join(inits, "\", \"") + QUOTE;
                writer.privateStaticFinal(pathInitsType, "INITS", "new PathInits(" + initsAsString + ")");
            }else{
                writer.privateStaticFinal(pathInitsType, "INITS", "PathInits.DIRECT");
            }

        }
    }

    protected void introJavadoc(CodeWriter writer, EntityType model) throws IOException {
        Type queryType = typeMappings.getPathType(model, model, true);
        writer.javadoc(queryType.getSimpleName() + " is a Querydsl query type for " + model.getSimpleName());
    }

    protected void introPackage(CodeWriter writer, EntityType model) throws IOException {
        Type queryType = typeMappings.getPathType(model, model, false);
        if (!queryType.getPackageName().isEmpty()){
            writer.packageDecl(queryType.getPackageName());
        }
    }

    protected void introSuper(CodeWriter writer, EntityType model) throws IOException {
        EntityType superType = model.getSuperType().getEntityType();
        Type superQueryType = typeMappings.getPathType(superType, model, false);
        if (!superType.hasEntityFields()){
            writer.publicFinal(superQueryType, "_super", NEW + writer.getRawName(superQueryType) + "(this)");
        }else{
            writer.publicFinal(superQueryType, "_super");
        }
    }

    protected void listAccessor(EntityType model, Property field, CodeWriter writer) throws IOException {
        String escapedName = field.getEscapedName();
        Type queryType = typeMappings.getPathType(field.getParameter(0), model, false);

        writer.beginPublicMethod(queryType, escapedName, new Parameter("index", Types.INT));
        writer.line(RETURN + escapedName + ".get(index);").end();

        writer.beginPublicMethod(queryType, escapedName, new Parameter("index", new ClassType(Expression.class, Types.INTEGER)));
        writer.line(RETURN + escapedName +".get(index);").end();
    }

    protected void mapAccessor(EntityType model, Property field, CodeWriter writer) throws IOException {
        String escapedName = field.getEscapedName();
        Type queryType = typeMappings.getPathType(field.getParameter(1), model, false);

        writer.beginPublicMethod(queryType, escapedName, new Parameter("key", field.getParameter(0)));
        writer.line(RETURN + escapedName + ".get(key);").end();

        writer.beginPublicMethod(queryType, escapedName, new Parameter("key", new ClassType(Expression.class, field.getParameter(0))));
        writer.line(RETURN + escapedName + ".get(key);").end();
    }

    private void delegate(final EntityType model, Delegate delegate, SerializerConfig config, CodeWriter writer) throws IOException {
        Parameter[] params = delegate.getParameters().toArray(new Parameter[delegate.getParameters().size()]);
        writer.beginPublicMethod(delegate.getReturnType(), delegate.getName(), params);

        // body start
        writer.beginLine(RETURN + delegate.getDelegateType().getSimpleName() + "."+delegate.getName()+"(");
        writer.append("this");
        if (!model.equals(delegate.getDeclaringType())){
            int counter = 0;
            EntityType type = model;
            while (type != null && !type.equals(delegate.getDeclaringType())){
                type = type.getSuperType() != null ? type.getSuperType().getEntityType() : null;
                counter++;
            }
            for (int i = 0; i < counter; i++){
                writer.append("._super");
            }
        }
        for (Parameter parameter : delegate.getParameters()){
            writer.append(COMMA + parameter.getName());
        }
        writer.append(");\n");

        // body end
        writer.end();
    }

    protected void outro(EntityType model, CodeWriter writer) throws IOException {
        writer.end();
    }

    public void serialize(EntityType model, SerializerConfig config, CodeWriter writer) throws IOException{
        intro(model, config, writer);

        // properties
        serializeProperties(model, config, writer);

        // constructors
        constructors(model, config, writer);

        // delegates
        for (Delegate delegate : model.getDelegates()){
            delegate(model, delegate, config, writer);
        }

        // property accessors
        for (Property property : model.getProperties()){
            TypeCategory category = property.getType().getCategory();
            if (category == TypeCategory.MAP && config.useMapAccessors()){
                mapAccessor(model, property, writer);
            }else if (category == TypeCategory.LIST && config.useListAccessors()){
                listAccessor(model, property, writer);
            }else if (category == TypeCategory.ENTITY && config.useEntityAccessors()){
                entityAccessor(model, property, writer);
            }
        }
        outro(model, writer);
    }

    protected void serialize(EntityType model, Property field, Type type, CodeWriter writer, String factoryMethod, String... args) throws IOException{
        Supertype superType = model.getSuperType();
        // construct value
        StringBuilder value = new StringBuilder();
        if (field.isInherited() && superType != null){
            if (!superType.getEntityType().hasEntityFields()){
                value.append("_super." + field.getEscapedName());
            }
        }else{
            value.append(factoryMethod + "(\"" + field.getName() + QUOTE);
            for (String arg : args){
                value.append(COMMA + arg);
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

    private void customField(EntityType model, Property field, SerializerConfig config, CodeWriter writer) throws IOException {
        Type queryType = typeMappings.getPathType(field.getType(), model, false);
        writer.line("// custom");
        if (field.isInherited()){
            writer.line("// inherited");
            Supertype superType = model.getSuperType();
            if (!superType.getEntityType().hasEntityFields()){
                writer.publicFinal(queryType, field.getEscapedName(),"_super." + field.getEscapedName());
            }else{
                writer.publicFinal(queryType, field.getEscapedName());
            }
        }else{
            String value = NEW + writer.getRawName(queryType) + "(forProperty(\"" + field.getName() + "\"))";
            writer.publicFinal(queryType, field.getEscapedName(), value);
        }
    }

    protected void serializeProperties(EntityType model,  SerializerConfig config, CodeWriter writer) throws IOException {
        for (Property property : model.getProperties()){
            // FIXME : the custom types should have the custom type category
            if (typeMappings.isRegistered(property.getType())
                    && property.getType().getCategory() != TypeCategory.CUSTOM
                    && property.getType().getCategory() != TypeCategory.ENTITY){
                customField(model, property, config, writer);
                continue;
            }
            
            // strips of "? extends " etc
            Type propertyType = new SimpleType(property.getType(), property.getType().getParameters());
            Type queryType = typeMappings.getPathType(propertyType, model, false);
            Type genericQueryType = null;
            String localRawName = writer.getRawName(property.getType());
            
            switch(property.getType().getCategory()){
            case STRING:
                serialize(model, property, queryType, writer, "createString");
                break;

            case BOOLEAN:
                serialize(model, property, queryType, writer, "createBoolean");
                break;

            case SIMPLE:
                serialize(model, property, queryType, writer, "createSimple", localRawName + DOT_CLASS);
                break;

            case COMPARABLE:
                serialize(model, property, queryType, writer, "createComparable", localRawName + DOT_CLASS);
                break;

            case ENUM:
                serialize(model, property, queryType, writer, "createEnum", localRawName + DOT_CLASS);
                break;

            case DATE:
                serialize(model, property, queryType, writer, "createDate", localRawName + DOT_CLASS);
                break;

            case DATETIME:
                serialize(model, property, queryType, writer, "createDateTime", localRawName + DOT_CLASS);
                break;

            case TIME:
                serialize(model, property, queryType, writer, "createTime", localRawName + DOT_CLASS);
                break;

            case NUMERIC:
                serialize(model, property, queryType, writer, "createNumber", localRawName + DOT_CLASS);
                break;

            case CUSTOM:
                customField(model, property, config, writer);
                break;

            case ARRAY:
                serialize(model, property, new ClassType(ArrayPath.class, property.getType().getComponentType()), writer, "createArray", localRawName + DOT_CLASS);
                break;

            case COLLECTION:
                genericQueryType = typeMappings.getPathType(getRaw(property.getParameter(0)), model, false);
                String genericKey = writer.getGenericName(true, property.getParameter(0));
                localRawName = writer.getRawName(property.getParameter(0));
                queryType = typeMappings.getPathType(property.getParameter(0), model, true);

                serialize(model, property, new ClassType(CollectionPath.class, getRaw(property.getParameter(0)), genericQueryType),
                        writer, "this.<"+genericKey + COMMA + writer.getGenericName(true, genericQueryType) + ">createCollection",
                        localRawName + DOT_CLASS, writer.getRawName(queryType) + DOT_CLASS);
                break;

            case SET:
                genericQueryType = typeMappings.getPathType(getRaw(property.getParameter(0)), model, false);
                genericKey = writer.getGenericName(true, property.getParameter(0));
                localRawName = writer.getRawName(property.getParameter(0));
                queryType = typeMappings.getPathType(property.getParameter(0), model, true);

                serialize(model, property, new ClassType(SetPath.class, getRaw(property.getParameter(0)), genericQueryType),
                        writer, "this.<"+genericKey + COMMA + writer.getGenericName(true, genericQueryType) + ">createSet",
                        localRawName + DOT_CLASS, writer.getRawName(queryType) + DOT_CLASS);
                break;

            case LIST:
                genericQueryType = typeMappings.getPathType(getRaw(property.getParameter(0)), model, false);
                genericKey = writer.getGenericName(true, property.getParameter(0));
                localRawName = writer.getRawName(property.getParameter(0));
                queryType = typeMappings.getPathType(property.getParameter(0), model, true);

                serialize(model, property, new ClassType(ListPath.class, getRaw(property.getParameter(0)), genericQueryType),
                        writer, "this.<"+genericKey + COMMA + writer.getGenericName(true, genericQueryType) + ">createList",
                        localRawName + DOT_CLASS, writer.getRawName(queryType) + DOT_CLASS);
                break;

            case MAP:
                genericKey = writer.getGenericName(true, property.getParameter(0));
                String genericValue = writer.getGenericName(true, property.getParameter(1));
                genericQueryType = typeMappings.getPathType(getRaw(property.getParameter(1)), model, false);
                String keyType = writer.getRawName(property.getParameter(0));
                String valueType = writer.getRawName(property.getParameter(1));
                queryType = typeMappings.getPathType(property.getParameter(1), model, true);

                serialize(model, property, new ClassType(MapPath.class, getRaw(property.getParameter(0)), getRaw(property.getParameter(1)), genericQueryType),
                        writer, "this.<" + genericKey + COMMA + genericValue + COMMA + writer.getGenericName(true, genericQueryType) + ">createMap",
                        keyType+DOT_CLASS, valueType+DOT_CLASS, writer.getRawName(queryType)+DOT_CLASS);
                break;

            case ENTITY:
                entityField(model, property, config, writer);
                break;
            }
        }
    }

    private Type getRaw(Type type) {
        if (type instanceof EntityType && type.getPackageName().startsWith("ext.java")){
            return type;
        }else{
            return new SimpleType(type, type.getParameters());
        }
    }
    
}
