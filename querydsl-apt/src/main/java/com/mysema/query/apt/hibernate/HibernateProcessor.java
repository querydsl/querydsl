/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.apt.hibernate;

import static com.sun.mirror.util.DeclarationVisitors.NO_OP;
import static com.sun.mirror.util.DeclarationVisitors.getDeclarationScanner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import org.apache.commons.io.FileUtils;

import com.mysema.query.apt.FreeMarkerSerializer;
import com.mysema.query.apt.TypeDecl;
import com.sun.mirror.apt.AnnotationProcessor;
import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.mirror.declaration.Declaration;

/**
 * 
 * HibernateProcessor provides
 *
 * @author tiwe
 * @version $Id$
 *
 */
public class HibernateProcessor implements AnnotationProcessor {
    private final String destClass;

    private final AnnotationProcessorEnvironment env;

    private final String include;

    private final String namePrefix;

    private final String targetFolder;

    public HibernateProcessor(AnnotationProcessorEnvironment env) throws IOException {
        this.env = env;
        this.targetFolder = env.getOptions().get("-s");
        this.destClass = getString(env.getOptions(), "-AdestClass=", "");
        this.include = getFileContent(env.getOptions(), "-Ainclude=", "");
        this.namePrefix = getString(env.getOptions(), "-AnamePrefix=", "_");
    }

    private void addSupertypeFields(TypeDecl typeDecl,
            Map<String, TypeDecl> entityTypes,
            Map<String, TypeDecl> mappedSupertypes) {
        String stype = typeDecl.getSupertypeName();
        while (true){
            TypeDecl sdecl;
            if (entityTypes.containsKey(stype)){
                sdecl = entityTypes.get(stype);
            }else if (mappedSupertypes.containsKey(stype)){
                sdecl = mappedSupertypes.get(stype);
            }else{
                return;
            }
            typeDecl.include(sdecl);
            stype = sdecl.getSupertypeName();
        }        
    }

    private String getFileContent(Map<String, String> options, String prefix,
            String defaultValue) throws IOException {
        for (Map.Entry<String, String> entry : options.entrySet()) {
            if (entry.getKey().startsWith(prefix)) {
                String fileName = entry.getKey().substring(prefix.length());
                return FileUtils.readFileToString(new File(fileName), "UTF-8");
            }
        }
        return defaultValue;
    }

    private String getString(Map<String, String> options, String prefix,
            String defaultValue) {
        for (Map.Entry<String, String> entry : options.entrySet()) {
            if (entry.getKey().startsWith(prefix)) {
                return entry.getKey().substring(prefix.length());
            }
        }
        return defaultValue;
    }

    public void process() {
        AnnotationTypeDeclaration a;
        Map<String, Object> model = new HashMap<String, Object>();
        EntityVisitor visitor1 = new EntityVisitor(); 
        
        // mapped superclass
        a = (AnnotationTypeDeclaration) env
        .getTypeDeclaration("javax.persistence.Entity");        
        for (Declaration typeDecl : env.getDeclarationsAnnotatedWith(a)) {
            typeDecl.accept(getDeclarationScanner(visitor1, NO_OP));
        }
        Map<String,TypeDecl> mappedSupertypes = visitor1.types;
                
        // TODO : embeddable types

        // domain types
        visitor1 = new EntityVisitor();
        a = (AnnotationTypeDeclaration) env
                .getTypeDeclaration("javax.persistence.Entity");        
        for (Declaration typeDecl : env.getDeclarationsAnnotatedWith(a)) {
            typeDecl.accept(getDeclarationScanner(visitor1, NO_OP));
        }
        Map<String,TypeDecl> entityTypes = visitor1.types;
        
        for (TypeDecl typeDecl : entityTypes.values()){
            addSupertypeFields(typeDecl, entityTypes, mappedSupertypes);
        }

        // DTOs
        a = (AnnotationTypeDeclaration) env
                .getTypeDeclaration("com.mysema.query.dto.DTO");
        DTOVisitor visitor2 = new DTOVisitor();
        for (Declaration typeDecl : env.getDeclarationsAnnotatedWith(a)) {
            typeDecl.accept(getDeclarationScanner(visitor2, NO_OP));
        }
        
        // populate model
        model.put("domainTypes", new TreeSet<TypeDecl>(entityTypes.values()));
        model.put("dtoTypes", visitor2.types);
        model.put("pre", namePrefix);
        model.put("include", include);
        model.put("package", destClass.substring(0, destClass.lastIndexOf('.')));
        model.put("classSimpleName", destClass.substring(destClass.lastIndexOf('.') + 1));
        
        // serialize it
        String path = destClass.replace('.', '/') + ".java";
        File file = new File(targetFolder, path);
        if (!file.getParentFile().mkdirs()){
            System.err.println("Folder " + file.getParent() + " could not be created");
        }
        try {
            Writer out = new OutputStreamWriter(new FileOutputStream(file));
            new FreeMarkerSerializer("/querydsl-hibernate.ftl").serialize(
                    model, out);
        } catch (Exception e) {
            String error = "Caught " + e.getClass().getName();
            throw new RuntimeException(error, e);
        }
    }
    
}