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
package com.querydsl.core.alias;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * DefaultTypeSystem is the default implementation of the {@link TypeSystem} interface
 * 
 * @author tiwe
 *
 */
public class DefaultTypeSystem implements TypeSystem{
    
    @Override
    public boolean isCollectionType(Class<?> cl) {
        return Collection.class.isAssignableFrom(cl);
    }

    @Override
    public boolean isListType(Class<?> cl) {
        return List.class.isAssignableFrom(cl);
    }
    
    @Override
    public boolean isSetType(Class<?> cl) {
        return Set.class.isAssignableFrom(cl);
    }

    @Override
    public boolean isMapType(Class<?> cl) {
        return Map.class.isAssignableFrom(cl);
    }

}
