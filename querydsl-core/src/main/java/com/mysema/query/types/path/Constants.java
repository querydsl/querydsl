/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types.path;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

final class Constants {
    
    private static final Set<Class<?>> typedClasses = new HashSet<Class<?>>(Arrays.<Class<?>>asList(
            ArrayPath.class,
            PathBuilder.class,
            ComparablePath.class,
            EnumPath.class,
            DatePath.class,
            DateTimePath.class,
            BeanPath.class,
            EntityPathBase.class,
            NumberPath.class,
            SimplePath.class,
            TimePath.class
            ));
    
    public static boolean isTyped(Class<?> cl){
        return typedClasses.contains(cl);
    }
    
    private Constants(){}

}
