/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.apt.general;

import static com.mysema.query.apt.APTUtils.getString;
import static com.mysema.query.apt.APTUtils.writerFor;
import static com.sun.mirror.util.DeclarationVisitors.NO_OP;
import static com.sun.mirror.util.DeclarationVisitors.getDeclarationScanner;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.mysema.query.apt.Serializer;
import com.mysema.query.apt.Type;
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
public class GeneralProcessor implements AnnotationProcessor {

    public static final Serializer 
        DOMAIN_OUTER_TMPL = new Serializer.FreeMarker("/domain-as-outer-classes.ftl"),
        EMBEDDABLE_OUTER_TMPL = new Serializer.FreeMarker("/embeddable-as-outer-classes.ftl"),
        DTO_OUTER_TMPL = new Serializer.FreeMarker("/dto-as-outer-classes.ftl");

    final String  destPackage,  dtoPackage, namePrefix, targetFolder;

    final AnnotationProcessorEnvironment env;

    final String superClassAnnotation, domainAnnotation, dtoAnnotation, embeddableAnnotation;

    public GeneralProcessor(AnnotationProcessorEnvironment env,
            String superClassAnnotation, String domainAnnotation,
            String dtoAnnotation, String embeddableAnnotation) throws IOException {
        this.env = env;
        this.targetFolder = env.getOptions().get("-s");
        this.destPackage = getString(env.getOptions(), "destPackage", null);
        this.dtoPackage = getString(env.getOptions(), "dtoPackage", null);
        this.namePrefix = getString(env.getOptions(), "namePrefix", "");

        this.superClassAnnotation = superClassAnnotation;
        this.domainAnnotation = domainAnnotation;
        this.dtoAnnotation = dtoAnnotation;
        this.embeddableAnnotation = embeddableAnnotation;
    }

    private void addSupertypeFields(Type typeDecl,
            Map<String, Type> entityTypes, Map<String, Type> mappedSupertypes) {
        String stype = typeDecl.getSupertypeName();
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
    }

    private void createDomainClasses() {
        EntityVisitor superclassVisitor = new EntityVisitor();

        // mapped superclass
        AnnotationTypeDeclaration a;
        Map<String, Type> mappedSupertypes;
        if (superClassAnnotation != null){
            a = (AnnotationTypeDeclaration) env.getTypeDeclaration(superClassAnnotation);
            for (Declaration typeDecl : env.getDeclarationsAnnotatedWith(a)) {
                typeDecl.accept(getDeclarationScanner(superclassVisitor, NO_OP));
            }
            mappedSupertypes = superclassVisitor.types;    
        }else{
            mappedSupertypes = new HashMap<String,Type>();
        }        

        // domain types
        EntityVisitor entityVisitor = new EntityVisitor();
        a = (AnnotationTypeDeclaration) env
                .getTypeDeclaration(domainAnnotation);
        for (Declaration typeDecl : env.getDeclarationsAnnotatedWith(a)) {
            typeDecl.accept(getDeclarationScanner(entityVisitor, NO_OP));
        }
        Map<String, Type> entityTypes = entityVisitor.types;

        for (Type typeDecl : entityTypes.values()) {
            addSupertypeFields(typeDecl, entityTypes, mappedSupertypes);
        }

        if (entityTypes.isEmpty() || destPackage == null) {
            env.getMessager().printNotice("No class generation for domain types");
        } else {
            serializeAsOuterClasses(entityTypes.values(), destPackage, DOMAIN_OUTER_TMPL);
        }

    }
    
    private void createEmbeddableClasses() {
        EntityVisitor entityVisitor = new EntityVisitor();
        AnnotationTypeDeclaration a = (AnnotationTypeDeclaration) env
                .getTypeDeclaration(embeddableAnnotation);
        for (Declaration typeDecl : env.getDeclarationsAnnotatedWith(a)) {
            typeDecl.accept(getDeclarationScanner(entityVisitor, NO_OP));
        }
        Map<String, Type> entityTypes = entityVisitor.types;

        if (entityTypes.isEmpty() || destPackage == null) {
            env.getMessager().printNotice("No class generation for domain types");
        } else {
            serializeAsOuterClasses(entityTypes.values(), destPackage, EMBEDDABLE_OUTER_TMPL);
        }

    }

    private void createDTOClasses() {
        AnnotationTypeDeclaration a = (AnnotationTypeDeclaration) env
                .getTypeDeclaration(dtoAnnotation);
        DTOVisitor dtoVisitor = new DTOVisitor();
        for (Declaration typeDecl : env.getDeclarationsAnnotatedWith(a)) {
            typeDecl.accept(getDeclarationScanner(dtoVisitor, NO_OP));
        }

        if (dtoVisitor.types.isEmpty() || dtoPackage == null) {
//            env.getMessager().printNotice("No class generation for DTO types");
        } else {
            serializeAsOuterClasses(dtoVisitor.types, dtoPackage, DTO_OUTER_TMPL);
        }

    }

    public void process() {
        if (domainAnnotation != null) createDomainClasses();
        if (embeddableAnnotation != null) createEmbeddableClasses();
        if (dtoAnnotation != null) createDTOClasses();
    }

    private void serializeAsOuterClasses(Collection<Type> entityTypes, String packageName,
            Serializer serializer) {
        // populate model
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("pre", namePrefix);
        model.put("package", packageName);

        for (Type type : entityTypes) {
            model.put("type", type);
            model.put("classSimpleName", type.getSimpleName());

            // serialize it
            try {
                String path = packageName.replace('.', '/') + "/" + namePrefix
                        + type.getSimpleName() + ".java";
                serializer.serialize(model, writerFor(new File(
                        targetFolder, path)));
            } catch (Exception e) {
                throw new RuntimeException("Caught exception", e);
            }
        }
    }



}
