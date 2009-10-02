package com.mysema.query.apt;

import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.lang.model.util.SimpleElementVisitor6;

import net.jcip.annotations.Immutable;

import org.apache.commons.lang.StringUtils;

import com.mysema.query.annotations.QueryType;
import com.mysema.query.codegen.ClassModel;
import com.mysema.query.codegen.FieldModel;
import com.mysema.query.codegen.TypeCategory;
import com.mysema.query.codegen.TypeModel;

/**
 * @author tiwe
 *
 */
@Immutable
public final class EntityElementVisitor extends SimpleElementVisitor6<ClassModel, Void>{
    
    private final ProcessingEnvironment env;
    
    private final String namePrefix;
    
    private final APTModelFactory typeFactory;
    
    private final Configuration configuration;
    
    EntityElementVisitor(ProcessingEnvironment env, Configuration conf, String namePrefix, APTModelFactory typeFactory){
        this.env = env;
        this.configuration = conf;
        this.namePrefix = namePrefix;
        this.typeFactory = typeFactory;
    }
    
    @Override
    public ClassModel visitType(TypeElement e, Void p) {
        Elements elementUtils = env.getElementUtils();
        TypeModel sc = typeFactory.create(e.getSuperclass(), elementUtils);
        TypeModel c = typeFactory.create(e.asType(), elementUtils);
        ClassModel classModel = new ClassModel(namePrefix, sc.getName(), c.getPackageName(), c.getName(), c.getSimpleName());
        List<? extends Element> elements = e.getEnclosedElements();
    
        VisitorConfig config = configuration.getConfig(e, elements);
        
        if (config.isVisitMethods()){
            for (ExecutableElement method : ElementFilter.methodsIn(elements)){
                String name = method.getSimpleName().toString();
                if (name.startsWith("get") && method.getParameters().isEmpty()){
                    name = StringUtils.uncapitalize(name.substring(3));
                }else if (name.startsWith("is") && method.getParameters().isEmpty()){
                    name = StringUtils.uncapitalize(name.substring(2));
                }else{
                    continue;
                }
                if (configuration.isValidGetter(method)){
                    try{
                        TypeModel typeModel = typeFactory.create(method.getReturnType(), elementUtils);
                        if (method.getAnnotation(QueryType.class) != null){
                            TypeCategory category = TypeCategory.get(method.getAnnotation(QueryType.class).value());
                            if (category == null){
                                continue;
                            }
                            typeModel = typeModel.as(category);
                        }
                        classModel.addField(new FieldModel(classModel, name, typeModel, null));    
                        
                    }catch(IllegalArgumentException ex){
                        StringBuilder builder = new StringBuilder();
                        builder.append("Caught exception for method ");
                        builder.append(c.getName()).append("#").append(method.getSimpleName());
                        throw new RuntimeException(builder.toString(), ex);
                    }
                }                
            }   
        }
        
        
        if (config.isVisitFields()){
            for (VariableElement field : ElementFilter.fieldsIn(elements)){
                if (configuration.isValidField(field)){
                    try{
                        TypeModel typeModel = typeFactory.create(field.asType(), elementUtils);     
                        if (field.getAnnotation(QueryType.class) != null){
                            TypeCategory category = TypeCategory.get(field.getAnnotation(QueryType.class).value());
                            if (category == null){
                                continue;
                            }
                            typeModel = typeModel.as(category);
                        }
                        String name = field.getSimpleName().toString();
                        classModel.addField(new FieldModel(classModel, name, typeModel, null));    
                    }catch(IllegalArgumentException ex){
                        StringBuilder builder = new StringBuilder();
                        builder.append("Caught exception for field ");
                        builder.append(c.getName()).append("#").append(field.getSimpleName());
                        throw new RuntimeException(builder.toString(), ex);
                    }
                        
                }                
            }    
        }
        
        return classModel;
    }
    
}
