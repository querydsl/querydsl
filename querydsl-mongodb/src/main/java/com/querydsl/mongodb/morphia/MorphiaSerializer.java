/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
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

import java.lang.reflect.AnnotatedElement;

import org.mongodb.morphia.Key;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;
import org.mongodb.morphia.annotations.Reference;
import org.mongodb.morphia.geo.Geometry;
import org.mongodb.morphia.geo.GeometryQueryConverter;
import org.mongodb.morphia.mapping.Mapper;

import com.mongodb.DBRef;
import com.querydsl.core.types.Constant;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.mongodb.MongodbSerializer;

/**
 * {@code MorphiaSerializer} extends {@link MongodbSerializer} with Morphia specific annotation handling
 *
 * @author tiwe
 *
 */
public class MorphiaSerializer extends MongodbSerializer {

    private final Morphia morphia;
    private final GeometryQueryConverter geometryQueryConverter;

    public MorphiaSerializer(Morphia morphia) {
        this.morphia = morphia == null ? new Morphia() : morphia;
        this.geometryQueryConverter = new GeometryQueryConverter(this.morphia.getMapper());
    }

    @Override
    public Object visit(Constant<?> expr, Void context) {
        if (Geometry.class.isAssignableFrom(expr.getType())) {
            return geometryQueryConverter.encode(expr.getConstant());
        } else {
            return super.visit(expr, context);
        }
    }

    @Override
    protected String getKeyForPath(Path<?> expr, PathMetadata metadata) {
        AnnotatedElement annotations = expr.getAnnotatedElement();
        if (annotations.isAnnotationPresent(Id.class)) {
            Path<?> parent = expr.getMetadata().getParent();
            if (parent.getAnnotatedElement().isAnnotationPresent(Reference.class)) {
                return null; // go to parent
            } else {
                return "_id";
            }
        } else if (annotations.isAnnotationPresent(Property.class)) {
            Property property = annotations.getAnnotation(Property.class);
            if (!property.value().equals(Mapper.IGNORED_FIELDNAME)) {
                return property.value();
            }
        } else if (annotations.isAnnotationPresent(Reference.class)) {
            Reference reference = annotations.getAnnotation(Reference.class);
            if (!reference.value().equals(Mapper.IGNORED_FIELDNAME)) {
                return reference.value();
            }
        }
        return super.getKeyForPath(expr, metadata);
    }

    @Override
    protected boolean isReference(Path<?> arg) {
        return arg.getAnnotatedElement().isAnnotationPresent(Reference.class);
    }

    @Override
    protected boolean isImplicitObjectIdConversion() {
        // see https://github.com/mongodb/morphia/wiki/FrequentlyAskedQuestions
        return false;
    }

    @Override
    protected boolean isId(Path<?> arg) {
        return arg.getAnnotatedElement().isAnnotationPresent(Id.class);
    }

    @Override
    protected DBRef asReference(Object constant) {
        Key<?> key = morphia.getMapper().getKey(constant);
        return morphia.getMapper().keyToRef(key);
    }

    @Override
    protected DBRef asReferenceKey(Class<?> entity, Object id) {
        Key<?> key = new Key<Object>(entity, id);
        return morphia.getMapper().keyToRef(key);
    }
}
