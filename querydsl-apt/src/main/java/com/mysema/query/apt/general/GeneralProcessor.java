package com.mysema.query.apt.general;

import static com.sun.mirror.util.DeclarationVisitors.NO_OP;
import static com.sun.mirror.util.DeclarationVisitors.getDeclarationScanner;

import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import org.apache.commons.io.FileUtils;

import com.mysema.query.apt.Serializer;
import com.mysema.query.apt.Type;
import com.sun.mirror.apt.AnnotationProcessor;
import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.mirror.declaration.Declaration;

/**
 * GeneralProcessor provides
 *
 * @author tiwe
 * @version $Id$
 */
public class GeneralProcessor implements AnnotationProcessor{

    static final Serializer 
        DOMAIN_INNER_TMPL = new Serializer.FreeMarker("/domain-as-inner-classes.ftl"),
        DOMAIN_OUTER_TMPL = new Serializer.FreeMarker("/domain-as-outer-classes.ftl"),
        DTO_INNER_TMPL = new Serializer.FreeMarker("/dto-as-inner-classes.ftl"),
        DTO_OUTER_TMPL = new Serializer.FreeMarker("/dto-as-outer-classes.ftl");

    private final String destClass, destPackage, dtoClass, dtoPackage;
    
    private final AnnotationProcessorEnvironment env;

    private final String include, namePrefix, targetFolder;
    
    private final String domainAnnotation;
    
    public GeneralProcessor(AnnotationProcessorEnvironment env, 
            String domainAnnotation) throws IOException {
        this.env = env;
        this.targetFolder = env.getOptions().get("-s");
        this.destClass = getString(env.getOptions(), "-AdestClass=", null);
        this.destPackage = getString(env.getOptions(), "-AdestPackage=", null);
        this.dtoClass = getString(env.getOptions(), "-AdtoClass=", null);
        this.dtoPackage = getString(env.getOptions(), "-AdtoPackage=", null); 
        this.include = getFileContent(env.getOptions(), "-Ainclude=", "");
        this.namePrefix = getString(env.getOptions(), "-AnamePrefix=", "");
        
        this.domainAnnotation = domainAnnotation;
    }

    private void addSupertypeFields(Type typeDecl,
            Map<String, Type> entityTypes,
            Map<String, Type> mappedSupertypes) {
        String stype = typeDecl.getSupertypeName();
        while (true){
            Type sdecl;
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

    private void createDomainClasses() {
        EntityVisitor visitor1 = new EntityVisitor(); 
        
        // mapped superclass
        AnnotationTypeDeclaration a = (AnnotationTypeDeclaration) env
        .getTypeDeclaration(domainAnnotation);        
        for (Declaration typeDecl : env.getDeclarationsAnnotatedWith(a)) {
            typeDecl.accept(getDeclarationScanner(visitor1, NO_OP));
        }
        Map<String,Type> mappedSupertypes = visitor1.types;
                
        // TODO : embeddable types

        // domain types
        visitor1 = new EntityVisitor();
        a = (AnnotationTypeDeclaration) env.getTypeDeclaration("javax.persistence.Entity");        
        for (Declaration typeDecl : env.getDeclarationsAnnotatedWith(a)) {
            typeDecl.accept(getDeclarationScanner(visitor1, NO_OP));
        }
        Map<String,Type> entityTypes = visitor1.types;
        
        for (Type typeDecl : entityTypes.values()){
            addSupertypeFields(typeDecl, entityTypes, mappedSupertypes);
        }
        
        if (destClass != null){
            serializeAsInnerClasses(entityTypes.values());
        }else if (destPackage != null){
            serializeAsOuterClasses(entityTypes.values());
        }else{
            System.err.print("No class generation for domain types");
        }
        
    }

    private void createDTOClasses() {        
        AnnotationTypeDeclaration a = (AnnotationTypeDeclaration) env
                .getTypeDeclaration("com.mysema.query.annotations.DTO");
        DTOVisitor visitor2 = new DTOVisitor();
        for (Declaration typeDecl : env.getDeclarationsAnnotatedWith(a)) {
            typeDecl.accept(getDeclarationScanner(visitor2, NO_OP));
        }         
        
        if (dtoClass != null){
            serializeDTOsAsInnerClasses(visitor2.types);                       
        }else if (dtoPackage != null){
            serializeDTOsAsOuterClasses(visitor2.types);
        }else{
            System.err.print("No class generation for DTO types");
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
        if (!"".equals(destClass)) createDomainClasses();
        if (!"".equals(dtoClass))  createDTOClasses();                 
    }

    private void serializeAsInnerClasses(Collection<Type> entityTypes) {
        // populate model
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("domainTypes", new TreeSet<Type>(entityTypes));
        model.put("pre", namePrefix);
        model.put("include", include);
        model.put("package", destClass.substring(0, destClass.lastIndexOf('.')));
        model.put("classSimpleName", destClass.substring(destClass.lastIndexOf('.') + 1));
        
        // serialize it
        String path = destClass.replace('.', '/') + ".java";
        File file = new File(targetFolder, path);
        if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()){
            System.err.println("Folder " + file.getParent() + " could not be created");
        }
        try {
            DOMAIN_INNER_TMPL.serialize(model, writerFor(file));
        } catch (Exception e) {                
            throw new RuntimeException("Caught exception",e);
        }
    }
    

    private void serializeAsOuterClasses(Collection<Type> entityTypes) {
        // populate model
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("pre", namePrefix);
        model.put("include", include);
        model.put("package", destPackage);
        
        for (Type type : entityTypes){
            model.put("type", type);
            model.put("classSimpleName", type.getSimpleName());
            
            // serialize it
            String path = destPackage.replace('.', '/') + "/" + namePrefix + type.getSimpleName() + ".java";
            File file = new File(targetFolder, path);
            if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()){
                System.err.println("Folder " + file.getParent() + " could not be created");
            }
            try {
                DOMAIN_OUTER_TMPL.serialize(model, writerFor(file));
            } catch (Exception e) {                
                throw new RuntimeException("Caught exception",e);
            }
        }        
    }
    
    private void serializeDTOsAsInnerClasses(Collection<Type> types) {
        // populate model
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("dtoTypes", types);
        model.put("pre", namePrefix);
        model.put("package", dtoClass.substring(0, dtoClass.lastIndexOf('.')));
        model.put("classSimpleName", dtoClass.substring(dtoClass.lastIndexOf('.') + 1));
        
        // serialize it
        String path = dtoClass.replace('.', '/') + ".java";
        File file = new File(targetFolder, path);
        if (!file.getParentFile().mkdirs()){
            System.err.println("Folder " + file.getParent() + " could not be created");
        }
        try {
            DTO_INNER_TMPL.serialize(model, writerFor(file));
        } catch (Exception e) {                
            throw new RuntimeException("Caught exception",e);
        }        
    }

    private void serializeDTOsAsOuterClasses(Collection<Type> types){
        // populate model
           Map<String, Object> model = new HashMap<String, Object>();        
           model.put("pre", namePrefix);
           model.put("include", include);
           model.put("package", dtoPackage);
           
           for (Type type : types){
               model.put("type", type);
               model.put("classSimpleName", type.getSimpleName());
               
               // serialize it
               String path = dtoPackage.replace('.', '/') + "/" + namePrefix + type.getSimpleName() + ".java";
               File file = new File(targetFolder, path);
               if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()){
                   System.err.println("Folder " + file.getParent() + " could not be created");
               }
               try {
                   DTO_OUTER_TMPL.serialize(model, writerFor(file));
               } catch (Exception e) {                
                   throw new RuntimeException("Caught exception",e);
               }   
           }
          
       }

    private Writer writerFor(File file){
        try {
            return new OutputStreamWriter(new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            String error = "Caught " + e.getClass().getName();
            throw new RuntimeException(error, e);
        }
    }
       
}
