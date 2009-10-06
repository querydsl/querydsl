package com.mysema.query.apt.jpa.hibernate;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;

import com.mysema.query.apt.jpa.JPAConfiguration;

/**
 * @author tiwe
 *
 */
public class HibernateConfiguration extends JPAConfiguration{

    public HibernateConfiguration(Class<? extends Annotation> entityAnn,
            Class<? extends Annotation> superTypeAnn,
            Class<? extends Annotation> embeddableAnn,
            Class<? extends Annotation> dtoAnn,
            Class<? extends Annotation> skipAnn) throws ClassNotFoundException {
        super(entityAnn, superTypeAnn, embeddableAnn, dtoAnn, skipAnn);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    protected List<Class<? extends Annotation>> getAnnotations() throws ClassNotFoundException{
        List<Class<? extends Annotation>> annotations = super.getAnnotations();
        for (String simpleName : Arrays.asList(
                "Type",
                "Cascade",
                "LazyCollection",
                "OnDelete")){
            annotations.add((Class<? extends Annotation>) Class.forName("org.hibernate.annotations."+simpleName));
        }
        return annotations;
        
    }
        

}
