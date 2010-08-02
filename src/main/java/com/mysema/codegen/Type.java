package com.mysema.codegen;

import java.util.List;
import java.util.Set;

/**
 * @author tiwe
 *
 * @param <T>
 */
public interface Type<T> {
    
    String getGenericName();

    String getGenericName(Set<String> packages, Set<String> classes);
    
    String getName();

    String getPackageName();
    
    List<Type<?>> getParameters();
    
    String getSimpleName();

}