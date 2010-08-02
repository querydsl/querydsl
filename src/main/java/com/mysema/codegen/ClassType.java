package com.mysema.codegen;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Type represents a generic type used in code generation
 * 
 * @author tiwe
 *
 * @param <T>
 */
public class ClassType<T> implements Type<T> {

    private final Class<T> javaClass;
    
    private final List<Type<?>> parameters;
    
    public ClassType(Class<T> javaClass, List<Type<?>> parameters) {
        this.javaClass = javaClass;
        this.parameters = parameters;
    }
    
    public ClassType(Class<T> clazz, Type<?>... parameters) {
        this(clazz, Arrays.asList(parameters));
    }
    
    @Override
    public boolean equals(Object o){
        if (o == this){
            return true;
        }else if (o instanceof ClassType<?>){
            ClassType<?> t = (ClassType<?>)o;
            return t.javaClass.equals(javaClass) && t.parameters.equals(parameters);
        }else{
            return false;
        }
    }

    @Override
    public String getGenericName() {
        return getGenericName(Collections.<String>emptySet(), Collections.<String>emptySet());
    }
    
    @Override
    public String getGenericName(Set<String> packages, Set<String> classes) {
        if (parameters.isEmpty()){
            return ClassUtils.getName(javaClass, packages, classes);
        }else{
            StringBuilder builder = new StringBuilder();
            builder.append(ClassUtils.getName(javaClass, packages, classes));
            builder.append("<");
            boolean first = true;
            for (Type<?> parameter : parameters){
                builder.append(parameter.getGenericName(packages, classes));
                if (!first){
                    builder.append(",");
                }
                first = false;
            }
            builder.append(">");
            return builder.toString();
        }
    }
    
    @Override
    public String getName() {
        return javaClass.getName();
    }

    @Override
    public String getPackageName() {
        return javaClass.getPackage().getName();
    }

    @Override
    public List<Type<?>> getParameters() {
        return parameters;
    }

    @Override
    public String getSimpleName() {
        return javaClass.getSimpleName();
    }

    @Override
    public int hashCode(){
        return javaClass.hashCode();
    }
    
}
