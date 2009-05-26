/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.apt.general;

import static com.mysema.query.apt.APTUtils.getString;
import static com.mysema.query.apt.APTUtils.writerFor;
import static com.sun.mirror.util.DeclarationVisitors.NO_OP;
import static com.sun.mirror.util.DeclarationVisitors.getDeclarationScanner;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.mysema.query.apt.FreeMarkerSerializer;
import com.mysema.query.apt.model.Type;
import com.sun.mirror.apt.AnnotationProcessor;
import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.mirror.declaration.Declaration;

/**
 * GeneralProcessor is the main processor for APT code generation.
 * 
 * @author tiwe
 * @version $Id$
 */
public abstract class GeneralProcessor implements AnnotationProcessor {

    public static final FreeMarkerSerializer DOMAIN_OUTER_TMPL = new FreeMarkerSerializer(
            "/domain-as-outer-classes.ftl"),
            EMBEDDABLE_OUTER_TMPL = new FreeMarkerSerializer(
                    "/embeddable-as-outer-classes.ftl"),
            DTO_OUTER_TMPL = new FreeMarkerSerializer(
                    "/dto-as-outer-classes.ftl");

    protected final String namePrefix, targetFolder;

    protected final AnnotationProcessorEnvironment env;

    protected final String superClassAnnotation, domainAnnotation,
            dtoAnnotation;

    public GeneralProcessor(AnnotationProcessorEnvironment env,
            String superClassAnnotation, String domainAnnotation,
            String dtoAnnotation) {
        this.env = env;
        this.targetFolder = env.getOptions().get("-s");
        this.namePrefix = getString(env.getOptions(), "namePrefix", "Q");

        this.superClassAnnotation = superClassAnnotation;
        this.domainAnnotation = domainAnnotation;
        this.dtoAnnotation = dtoAnnotation;
    }

    private void addSupertypeFields(Type typeDecl,
            Map<String, Type> entityTypes, Map<String, Type> mappedSupertypes) {
        String stype = typeDecl.getSupertypeName();
        Class<?> superClass = safeClassForName(stype);
        if (entityTypes.containsKey(stype)
                || mappedSupertypes.containsKey(stype)) {
            while (true) {
                Type sdecl;
                if (entityTypes.containsKey(stype)) {
                    sdecl = entityTypes.get(stype);
                } else if (mappedSupertypes.containsKey(stype)) {
                    sdecl = mappedSupertypes.get(stype);
                } else {
                    return;
                }
                typeDecl.include(sdecl);
                stype = sdecl.getSupertypeName();
            }

        } else if (superClass != null && !superClass.equals(Object.class)) {
            // TODO : recursively up ?
            Type type = TypeFactory.createType(superClass);
            // include fields of supertype
            typeDecl.include(type);
        }
    }

    private Class<?> safeClassForName(String stype) {
        try {
            return stype != null ? Class.forName(stype) : null;
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    protected DefaultEntityVisitor createEntityVisitor() {
        return new DefaultEntityVisitor();
    }

    protected DefaultDTOVisitor createDTOVisitor() {
        return new DefaultDTOVisitor();
    }

    private void createDomainClasses() {
        DefaultEntityVisitor superclassVisitor = createEntityVisitor();

        // mapped superclass
        AnnotationTypeDeclaration a;
        Map<String, Type> mappedSupertypes;
        if (superClassAnnotation != null) {
            a = (AnnotationTypeDeclaration) env
                    .getTypeDeclaration(superClassAnnotation);
            for (Declaration typeDecl : env.getDeclarationsAnnotatedWith(a)) {
                typeDecl
                        .accept(getDeclarationScanner(superclassVisitor, NO_OP));
            }
            mappedSupertypes = superclassVisitor.types;
        } else {
            mappedSupertypes = new HashMap<String, Type>();
        }

        // domain types
        DefaultEntityVisitor entityVisitor = createEntityVisitor();
        a = (AnnotationTypeDeclaration) env
                .getTypeDeclaration(domainAnnotation);
        for (Declaration typeDecl : env.getDeclarationsAnnotatedWith(a)) {
            typeDecl.accept(getDeclarationScanner(entityVisitor, NO_OP));
        }
        Map<String, Type> entityTypes = entityVisitor.types;

        for (Type typeDecl : entityTypes.values()) {
            addSupertypeFields(typeDecl, entityTypes, mappedSupertypes);
        }

        if (entityTypes.isEmpty()) {
            env.getMessager().printNotice(
                    "No class generation for domain types");
        } else {
            serializeAsOuterClasses(entityTypes.values(), DOMAIN_OUTER_TMPL);
        }

    }

    private void createDTOClasses() {
        AnnotationTypeDeclaration a = (AnnotationTypeDeclaration) env
                .getTypeDeclaration(dtoAnnotation);
        DefaultDTOVisitor dtoVisitor = createDTOVisitor();
        for (Declaration typeDecl : env.getDeclarationsAnnotatedWith(a)) {
            typeDecl.accept(getDeclarationScanner(dtoVisitor, NO_OP));
        }
        if (dtoVisitor.types.isEmpty()) {
            env.getMessager().printNotice("No class generation for DTO types");
        } else {
            serializeAsOuterClasses(dtoVisitor.types, DTO_OUTER_TMPL);
        }

    }

    public void process() {
        if (domainAnnotation != null) {
            createDomainClasses();
        }
        if (dtoAnnotation != null) {
            createDTOClasses();
        }
    }

    protected void serializeAsOuterClasses(Collection<Type> entityTypes,
            FreeMarkerSerializer serializer) {
        // populate model
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("pre", namePrefix);

        for (Type type : entityTypes) {
            String packageName = type.getPackageName();
            model.put("package", packageName);
            model.put("type", type);
            model.put("classSimpleName", type.getSimpleName());

            // serialize it
            try {
                String path = packageName.replace('.', '/') + "/" + namePrefix
                        + type.getSimpleName() + ".java";
                serializer.serialize(model, writerFor(new File(targetFolder,
                        path)));
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
    }

}
