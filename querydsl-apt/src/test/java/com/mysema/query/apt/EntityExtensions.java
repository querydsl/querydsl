package com.mysema.query.apt;

import com.mysema.query.annotations.QueryDelegate;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.path.BooleanPath;

public class EntityExtensions {
    
    @QueryDelegate(EntityWithExtensions.class)
    public static Predicate extension(QEntityWithExtensions entity){
        return new BooleanPath("b");
    }

}
