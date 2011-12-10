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
package com.mysema.query.codegen;

import java.util.List;

import net.jcip.annotations.Immutable;

import com.mysema.codegen.model.Parameter;
import com.mysema.codegen.model.Type;
import com.mysema.commons.lang.Assert;

/**
 * Delegate defines a delegate method which dispatches to an external static method
 * 
 * @author tiwe
 *
 */
@Immutable
public class Delegate {

    private final Type declaringType;

    private final Type delegateType;

    private final String name;

    private final List<Parameter> parameters;

    private final Type returnType;

    public Delegate(Type declaringType, Type delegateType, String name, List<Parameter> params, Type returnType) {
        this.declaringType = Assert.notNull(declaringType,"declaringType");
        this.delegateType = Assert.notNull(delegateType,"delegateType");
        this.name = Assert.notNull(name,"name");
        this.parameters = Assert.notNull(params,"params");
        this.returnType = Assert.notNull(returnType,"returnType");
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o instanceof Delegate) {
            Delegate m = (Delegate)o;
            return m.name.equals(name) && m.parameters.equals(parameters);
        } else {
            return false;
        }
    }

    public Type getDeclaringType() {
        return declaringType;
    }

    public Type getDelegateType() {
        return delegateType;
    }

    public String getName() {
        return name;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public Type getReturnType() {
        return returnType;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString(){
        return delegateType.getFullName() + "." + name + " " + parameters;
    }

}
