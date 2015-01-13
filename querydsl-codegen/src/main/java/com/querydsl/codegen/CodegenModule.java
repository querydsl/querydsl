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

import java.util.Collections;

/**
 * CodegenModule provides a module general serialization
 *
 * @author tiwe
 *
 */
public class CodegenModule  extends AbstractModule {

    public static final String PREFIX = "prefix";

    public static final String SUFFIX = "suffix";

    public static final String KEYWORDS = "keywords";

    public static final String PACKAGE_SUFFIX = "packageSuffix";

    public static final String IMPORTS = "imports";

    @Override
    protected void configure() {
        bind(TypeMappings.class, JavaTypeMappings.class);
        bind(QueryTypeFactory.class, QueryTypeFactoryImpl.class);
        bind(EntitySerializer.class);
        bind(EmbeddableSerializer.class);
        bind(ProjectionSerializer.class);
        bind(SupertypeSerializer.class);

        // configuration for QueryTypeFactory
        bind(PREFIX, "Q");
        bind(SUFFIX, "");
        bind(PACKAGE_SUFFIX, "");
        bind(KEYWORDS, Collections.<String>emptySet());
        bind(IMPORTS, Collections.<String>emptySet());
    }

}