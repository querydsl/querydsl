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

import static com.querydsl.codegen.BeanSerializer.DEFAULT_JAVADOC_SUFFIX;

import java.util.Collections;

/**
 * {@code CodegenModule} provides a module for general serialization
 *
 * @author tiwe
 *
 */
public class CodegenModule  extends AbstractModule {

    /**
     * key for the query type name prefix
     */
    public static final String PREFIX = "prefix";

    /**
     * key for the query type name suffix
     */
    public static final String SUFFIX = "suffix";

    /**
     * key for the keywords set
     */
    public static final String KEYWORDS = "keywords";

    /**
     * key for the package suffix
     */
    public static final String PACKAGE_SUFFIX = "packageSuffix";

    /**
     * key for the custom imports set
     */
    public static final String IMPORTS = "imports";

    /**
     * key for the variable name function class
     */
    public static final String VARIABLE_NAME_FUNCTION_CLASS = "variableNameFunction";

    /**
     * the fully qualified class name of the <em>Single-Element Annotation</em> (with {@code String} element)
     * to indicate that these have been generated. Defaults to java's {@code Generated} annotation (depending on java version)
     */
    public static final String GENERATED_ANNOTATION_CLASS = "generatedAnnotationClass";

    protected static final String JAVADOC_SUFFIX = "javadocSuffix";

    @Override
    protected void configure() {
        bind(TypeMappings.class, JavaTypeMappings.class);
        bind(QueryTypeFactory.class, QueryTypeFactoryImpl.class);
        bind(EntitySerializer.class, DefaultEntitySerializer.class);
        bind(EmbeddableSerializer.class, DefaultEmbeddableSerializer.class);
        bind(ProjectionSerializer.class, DefaultProjectionSerializer.class);
        bind(SupertypeSerializer.class, DefaultSupertypeSerializer.class);
        bind(Filer.class, DefaultFiler.class);

        // configuration for QueryTypeFactory
        bind(PREFIX, "Q");
        bind(SUFFIX, "");
        bind(PACKAGE_SUFFIX, "");
        bind(KEYWORDS, Collections.<String>emptySet());
        bind(IMPORTS, Collections.<String>emptySet());
        bind(VARIABLE_NAME_FUNCTION_CLASS, DefaultVariableNameFunction.INSTANCE);
        bind(GENERATED_ANNOTATION_CLASS, GeneratedAnnotationResolver.resolveDefault());
        bind(JAVADOC_SUFFIX, DEFAULT_JAVADOC_SUFFIX);
    }

}
