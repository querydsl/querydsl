package com.mysema.codegen;

import java.util.Arrays;
import java.util.List;

/**
 * Type represents a generic type used in code generation
 * 
 * @author tiwe
 *
 * @param <T>
 */
public class Type<T> {

    private final Class<T> javaClass;
    
    private final List<Type<?>> parameters;
    
    public Type(Class<T> javaClass, List<Type<?>> parameters) {
        this.javaClass = javaClass;
        this.parameters = parameters;
    }
    
    public Type(Class<T> clazz, Type<?>... parameters) {
        this(clazz, Arrays.asList(parameters));
    }
    
    public Class<T> getJavaClass() {
        return javaClass;
    }

    public List<Type<?>> getParameters() {
        return parameters;
    }

    public String getName() {
        return javaClass.getName();
    }
    
    @Override
    public int hashCode(){
        return javaClass.hashCode();
    }
    
    @Override
    public boolean equals(Object o){
        if (o == this){
            return true;
        }else if (o instanceof Type<?>){
            Type<?> t = (Type<?>)o;
            return t.javaClass.equals(javaClass) && t.parameters.equals(parameters);
        }else{
            return false;
        }
    }
    
}
