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
package com.querydsl.mongodb.morphia;

import org.mongodb.morphia.Key;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.annotations.Property;
import org.mongodb.morphia.annotations.Reference;

import com.mongodb.DBRef;
import com.querydsl.mongodb.MongodbSerializer;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.PathType;

/**
 * MorphiaSerializer extends {@link MongodbSerializer} with Morphia specific annotation handling
 *
 * @author tiwe
 *
 */
public class MorphiaSerializer extends MongodbSerializer {

    private final Morphia morphia;

    public MorphiaSerializer(Morphia morphia) {
        this.morphia = morphia;
    }

    @Override
    protected String getKeyForPath(Path<?> expr, PathMetadata<?> metadata) {
        if (metadata.getPathType() == PathType.PROPERTY && expr.getAnnotatedElement().isAnnotationPresent(Property.class)) {
            return expr.getAnnotatedElement().getAnnotation(Property.class).value();
        } else {
            return super.getKeyForPath(expr, metadata);
        }
    }

    @Override
    protected boolean isReference(Path<?> arg) {
        return arg.getAnnotatedElement().isAnnotationPresent(Reference.class);
    }

    @Override
    protected DBRef asReference(Object constant) {
        Key<?> key = morphia.getMapper().getKey(constant);
        return morphia.getMapper().keyToRef(key);
    }

}
