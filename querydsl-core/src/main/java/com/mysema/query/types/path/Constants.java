package com.mysema.query.types.path;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public final class Constants {
    
    static final Set<Class<?>> typedClasses = new HashSet<Class<?>>(Arrays.<Class<?>>asList(
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

}
