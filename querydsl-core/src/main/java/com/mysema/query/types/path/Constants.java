package com.mysema.query.types.path;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public final class Constants {
    
    static final Set<Class<?>> typedClasses = new HashSet<Class<?>>(Arrays.<Class<?>>asList(
            PathBuilder.class,
            PComparable.class,
            PEnum.class,
            PDate.class,
            PDateTime.class,
            BeanPath.class,
            EntityPathBase.class,
            PNumber.class,
            PSimple.class,
            PTime.class
            ));

}
