/*
 * Copyright 2010, Mysema Ltd
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
package com.querydsl.codegen.utils.model;

import java.util.List;
import java.util.Set;

/**
 * @author tiwe
 */
public interface Type {

    Type as(TypeCategory category);

    Type asArrayType();

    Type getComponentType();

    Type getEnclosingType();

    TypeCategory getCategory();

    String getFullName();

    String getGenericName(boolean asArgType);

    String getGenericName(boolean asArgType, Set<String> packages, Set<String> classes);

    Class<?> getJavaClass();

    String getPackageName();

    List<Type> getParameters();

    String getRawName(Set<String> packages, Set<String> classes);

    String getSimpleName();

    boolean isFinal();

    boolean isPrimitive();

    boolean isMember();

}