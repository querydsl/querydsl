/*
 * Copyright 2011, Mysema Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.core.types.path;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author tiwe
 *
 */
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
    
    public static boolean isTyped(Class<?> cl) {
        return typedClasses.contains(cl);
    }
    
    private Constants() {}

}
