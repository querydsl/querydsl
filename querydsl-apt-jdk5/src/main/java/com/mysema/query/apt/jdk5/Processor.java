/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.apt.jdk5;

import static com.sun.mirror.util.DeclarationVisitors.NO_OP;
import static com.sun.mirror.util.DeclarationVisitors.getDeclarationScanner;

import java.util.HashMap;
import java.util.Map;

import com.mysema.query.codegen.ClassModel;
import com.mysema.query.codegen.Serializers;
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
public abstract class Processor implements AnnotationProcessor {

    protected final String namePrefix, targetFolder;

    protected final AnnotationProcessorEnvironment env;

    protected final String superClassAnnotation, domainAnnotation, dtoAnnotation;

    public Processor(AnnotationProcessorEnvironment env,
            String superClassAnnotation, String domainAnnotation,
            String dtoAnnotation) {
        this.env = env;
        this.targetFolder = env.getOptions().get("-s");
        this.namePrefix = APTFactory.getString(env.getOptions(), "namePrefix", "Q");
        this.superClassAnnotation = superClassAnnotation;
        this.domainAnnotation = domainAnnotation;
        this.dtoAnnotation = dtoAnnotation;
    }

    protected EntityVisitor createEntityVisitor() {
        return new EntityVisitor();
    }

    protected DTOVisitor createDTOVisitor() {
        return new DTOVisitor();
    }

    private void createDomainClasses() {
        EntityVisitor superclassVisitor = createEntityVisitor();

        // mapped superclass
        AnnotationTypeDeclaration a;
        Map<String, ClassModel> mappedSupertypes;
        if (superClassAnnotation != null) {
            a = (AnnotationTypeDeclaration) env.getTypeDeclaration(superClassAnnotation);
            for (Declaration typeDecl : env.getDeclarationsAnnotatedWith(a)) {
                typeDecl.accept(getDeclarationScanner(superclassVisitor, NO_OP));
            }
            mappedSupertypes = superclassVisitor.types;
        } else {
            mappedSupertypes = new HashMap<String, ClassModel>();
        }

        // domain types
        EntityVisitor entityVisitor = createEntityVisitor();
        a = (AnnotationTypeDeclaration) env.getTypeDeclaration(domainAnnotation);
        for (Declaration typeDecl : env.getDeclarationsAnnotatedWith(a)) {
            typeDecl.accept(getDeclarationScanner(entityVisitor, NO_OP));
        }
        Map<String, ClassModel> entityTypes = entityVisitor.types;

        for (ClassModel typeDecl : entityTypes.values()) {
            typeDecl.addSupertypeFields(entityTypes, mappedSupertypes);
        }

        if (entityTypes.isEmpty()) {
            env.getMessager().printNotice("No class generation for domain types");
        } else {
            Serializers.DOMAIN.serialize(targetFolder, namePrefix, entityTypes.values());
        }

    }

    private void createDTOClasses() {
        AnnotationTypeDeclaration a = (AnnotationTypeDeclaration) env.getTypeDeclaration(dtoAnnotation);
        DTOVisitor dtoVisitor = createDTOVisitor();
        for (Declaration typeDecl : env.getDeclarationsAnnotatedWith(a)) {
            typeDecl.accept(getDeclarationScanner(dtoVisitor, NO_OP));
        }
        if (dtoVisitor.types.isEmpty()) {
            env.getMessager().printNotice("No class generation for DTO types");
        } else {
            Serializers.DTO.serialize(targetFolder, namePrefix, dtoVisitor.types);
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

}
