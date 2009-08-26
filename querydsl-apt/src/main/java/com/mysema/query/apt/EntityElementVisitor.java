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

import org.apache.commons.lang.StringUtils;

import com.mysema.query.codegen.ClassModel;
import com.mysema.query.codegen.ClassModelFactory;
import com.mysema.query.codegen.FieldModel;
import com.mysema.query.codegen.TypeModel;

/**
 * @author tiwe
 *
 */
public abstract class EntityElementVisitor extends SimpleElementVisitor6<ClassModel, Void>{
    
    private final ProcessingEnvironment env;
    
    private final String namePrefix;
    
    private final APTModelFactory typeFactory;
    
    private ClassModelFactory classModelFactory;
    
    EntityElementVisitor(ProcessingEnvironment env, String namePrefix, ClassModelFactory classModelFactory, APTModelFactory typeFactory){
        this.env = env;
        this.namePrefix = namePrefix;
        this.classModelFactory = classModelFactory;
        this.typeFactory = typeFactory;
    }
    
    @Override
    public ClassModel visitType(TypeElement e, Void p) {
        Elements elementUtils = env.getElementUtils();
        TypeModel sc = typeFactory.create(e.getSuperclass(), elementUtils);
        TypeModel c = typeFactory.create(e.asType(), elementUtils);
        ClassModel classModel = new ClassModel(classModelFactory, namePrefix, sc.getName(), c.getPackageName(), c.getName(), c.getSimpleName());
        List<? extends Element> elements = e.getEnclosedElements();
        
        // GETTERS
        for (ExecutableElement method : ElementFilter.methodsIn(elements)){
            String name = method.getSimpleName().toString();
            if (name.startsWith("get") && method.getParameters().isEmpty()){
                name = StringUtils.uncapitalize(name.substring(3));
            }else if (name.startsWith("is") && method.getParameters().isEmpty()){
                name = StringUtils.uncapitalize(name.substring(2));
            }else{
                continue;
            }
            if (isValidGetter(method)){
                try{
                    TypeModel typeModel = typeFactory.create(method.getReturnType(), elementUtils);
                    String docs = elementUtils.getDocComment(method);
                    classModel.addField(new FieldModel(classModel, name, typeModel, docs != null ? docs : name));    
                    
                }catch(IllegalArgumentException ex){
                    throw new RuntimeException("Caught exception for method " + c.getName()+"#"+method.getSimpleName(), ex);
                }
            }                
        }
        
        // FIELDS
        for (VariableElement field : ElementFilter.fieldsIn(elements)){
            if (isValidField(field)){
                try{
                    TypeModel typeModel = typeFactory.create(field.asType(), elementUtils);     
                    String name = field.getSimpleName().toString();
                    String docs = elementUtils.getDocComment(field);
                    classModel.addField(new FieldModel(classModel, name, typeModel, docs != null ? docs : name));    
                }catch(IllegalArgumentException ex){
                    throw new RuntimeException("Caught exception for field " + c.getName()+"#"+field.getSimpleName(), ex);
                }
                    
            }                
        }                        
        return classModel;
    }

    protected abstract boolean isValidGetter(ExecutableElement method);

    protected abstract boolean isValidField(VariableElement field);
}
