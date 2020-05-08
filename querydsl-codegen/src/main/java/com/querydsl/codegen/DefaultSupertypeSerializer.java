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
package com.querydsl.codegen;

import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * {@code SupertypeSerializer} is a {@link Serializer} implementation for supertypes
 *
 * @author tiwe
 *
 */
public final class DefaultSupertypeSerializer extends DefaultEntitySerializer implements SupertypeSerializer {

    /**
     * Create a new SupertypeSerializer instance
     *
     * @param typeMappings type mappings to be used
     * @param keywords keywords to be used
     */
    @Inject
    public DefaultSupertypeSerializer(TypeMappings typeMappings, @Named("keywords") Collection<String> keywords) {
        super(typeMappings, keywords);
    }

}
