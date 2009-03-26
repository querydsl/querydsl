package com.mysema.query.apt.jpa;

import static com.sun.mirror.util.DeclarationVisitors.NO_OP;
import static com.sun.mirror.util.DeclarationVisitors.getDeclarationScanner;

import java.util.Map;

import com.mysema.query.apt.general.DefaultEntityVisitor;
import com.mysema.query.apt.general.GeneralProcessor;
import com.mysema.query.apt.model.Type;
import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.mirror.declaration.Declaration;

/**
 * JpaProcessor provides
 *
 * @author tiwe
 * @version $Id$
 */
public class JpaProcessor extends GeneralProcessor{
    
    public static final String jpaSuperClass = "javax.persistence.MappedSuperclass",
        jpaEntity = "javax.persistence.Entity",                        
        jpaEmbeddable = "javax.persistence.Embeddable";
    
    public JpaProcessor(AnnotationProcessorEnvironment env) {
        super(env, jpaSuperClass, jpaEntity, qdDto);
    }

    private void createEmbeddableClasses() {
        DefaultEntityVisitor entityVisitor = new DefaultEntityVisitor();
        AnnotationTypeDeclaration a = (AnnotationTypeDeclaration) env.getTypeDeclaration(jpaEmbeddable);
        for (Declaration typeDecl : env.getDeclarationsAnnotatedWith(a)) {
            typeDecl.accept(getDeclarationScanner(entityVisitor, NO_OP));
        }
        
        Map<String, Type> entityTypes = entityVisitor.types;
        if (entityTypes.isEmpty()) {
            env.getMessager().printNotice("No class generation for domain types");
        } else {
            serializeAsOuterClasses(entityTypes.values(), EMBEDDABLE_OUTER_TMPL);
        }

    }
    
    public void process() {
        super.process();
        createEmbeddableClasses();
    }
}
