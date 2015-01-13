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
package com.querydsl.codegen;

import javax.annotation.Nullable;

import com.mysema.codegen.model.Type;

/**
 * Supertype defines a tuple of a {@link Type} and an optional {@link EntityType} instance used for 
 * supertype references in EntityType instances
 * 
 * @author tiwe
 *
 */
public class Supertype {

    @Nullable
    private EntityType entityType;

    private final Type type;

    public Supertype(Type type) {
        this.type = type;
    }
    
    public Supertype(Type type, EntityType entityType) {
        this.type = type;
        this.entityType = entityType;
    }

    @Nullable
    public EntityType getEntityType() {
        return entityType;
    }

    public Type getType() {
        return type;
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }

    @Override
    public int hashCode() {
        return type.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o instanceof Supertype) {
            return ((Supertype)o).type.equals(type);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return type.toString();
    }

}
