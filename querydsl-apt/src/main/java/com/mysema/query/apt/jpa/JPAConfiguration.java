package com.mysema.query.apt.jpa;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;

import com.mysema.query.apt.Configuration;
import com.mysema.query.apt.VisitorConfig;

/**
 * @author tiwe
 *
 */
public class JPAConfiguration extends Configuration {
   
    private List<Class<? extends Annotation>> annotations;
    
    @SuppressWarnings("unchecked")
    public JPAConfiguration(Class<? extends Annotation> entityAnn,
            Class<? extends Annotation> superTypeAnn,
            Class<? extends Annotation> embeddableAnn,
            Class<? extends Annotation> dtoAnn,
            Class<? extends Annotation> skipAnn) throws ClassNotFoundException {
        super(entityAnn, superTypeAnn, embeddableAnn, dtoAnn, skipAnn);
        this.annotations = new ArrayList<Class<? extends Annotation>>();
        for (String simpleName : Arrays.asList(
                "Column",
                "Embedded",
                "EmbeddedId",
                "GeneratedValue",
                "Id",
                "JoinColumn",
                "ManyToOne",
                "OneToMany",
                "PrimaryKeyJoinColumn")){
            annotations.add((Class<? extends Annotation>) Class.forName("javax.persistence."+simpleName));
        }
    }
    
    @Override
    public VisitorConfig getConfig(TypeElement e, List<? extends Element> elements){
        boolean fields = false, methods = false;
        for (Element element : elements){
            if (element.getKind().equals(ElementKind.FIELD) ){
                if (!fields && hasJPAAnnotation(element)){
                    fields = true;
                }
            }else if (element.getKind().equals(ElementKind.METHOD)){
                if (!methods && hasJPAAnnotation(element)){
                    methods = true;
                }
            }
        }    
        if (fields && !methods){
            return VisitorConfig.FIELDS_ONLY;
        }else if (methods && !fields){
            return VisitorConfig.METHODS_ONLY;
        }else{
            return VisitorConfig.ALL;    
        }        
        
    }
    
    private boolean hasJPAAnnotation(Element element){
        for (Class<? extends Annotation> annotation : annotations){
            if (element.getAnnotation(annotation) != null){
                return true;
            }
        }
        return false;
    }

}
