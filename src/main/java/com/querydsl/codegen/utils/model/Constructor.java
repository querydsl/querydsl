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

import java.util.Collection;

/**
 * @author tiwe
 * 
 */
public final class Constructor {

    private final Collection<Parameter> parameters;

    public Constructor(Collection<Parameter> params) {
        parameters = params;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o instanceof Constructor) {
            return ((Constructor) o).parameters.equals(parameters);
        } else {
            return false;
        }
    }

    public Collection<Parameter> getParameters() {
        return parameters;
    }

    @Override
    public int hashCode() {
        return parameters.hashCode();
    }

}
