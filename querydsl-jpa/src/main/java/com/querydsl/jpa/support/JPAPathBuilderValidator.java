/*
 * Copyright 2014, Timo Westkämper
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
package com.querydsl.jpa.support;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.ManagedType;
import javax.persistence.metamodel.Metamodel;
import javax.persistence.metamodel.PluralAttribute;

import com.google.common.primitives.Primitives;
import com.querydsl.core.types.path.PathBuilderValidator;

/**
 * JPAPathBuilderValidator implements PathBuilderValidator using a JPA Metamodel instance
 */
public class JPAPathBuilderValidator implements PathBuilderValidator {

    private final Metamodel metamodel;

    public JPAPathBuilderValidator(EntityManager entityManager) {
        this.metamodel = entityManager.getMetamodel();
    }

    public JPAPathBuilderValidator(Metamodel metamodel) {
        this.metamodel = metamodel;
    }

    @Override
    public Class<?> validate(Class<?> parent, String property, Class<?> propertyType) {
        try {
            ManagedType managedType = metamodel.managedType(parent);
            Attribute attribute = managedType.getAttribute(property);
            if (attribute instanceof PluralAttribute) {
                return ((PluralAttribute)attribute).getElementType().getJavaType();
            } else {
                return Primitives.wrap(attribute.getJavaType());
            }
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
