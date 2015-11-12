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
package com.querydsl.apt;

/**
 * APT options supported by Querydsl
 *
 * @author tiwe
 *
 */
public final class APTOptions {

    /**
     * set whether default variables are created (default: true)
     */
    public static final String QUERYDSL_CREATE_DEFAULT_VARIABLE = "querydsl.createDefaultVariable";

    /**
     * set the prefix for query types (default: Q)
     */
    public static final String QUERYDSL_PREFIX = "querydsl.prefix";

    /**
     * set a suffix for query types (default: empty)
     */
    public static final String QUERYDSL_SUFFIX = "querydsl.suffix";

    /**
     * set a suffix for query type packages (default: empty)
     */
    public static final String QUERYDSL_PACKAGE_SUFFIX = "querydsl.packageSuffix";

    /**
     * enable accessors for direct key based map access (default: false)
     */
    public static final String QUERYDSL_MAP_ACCESSORS = "querydsl.mapAccessors";

    /**
     * enable accessors for direct indexed list access (default: false)
     */
    public static final String QUERYDSL_LIST_ACCESSORS = "querydsl.listAccessors";

    /**
     * enable reference field accessors (default: false)
     */
    public static final String QUERYDSL_ENTITY_ACCESSORS = "querydsl.entityAccessors";

    /**
     * Set whether fields are used as metadata source (default: true)
     */
    public static final String QUERYDSL_USE_FIELDS = "querydsl.useFields";

    /**
     * Set whether accessors are used as metadata source (default: true)
     */
    public static final String QUERYDSL_USE_GETTERS = "querydsl.useGetters";

    /**
     * comma separated list of packages to be excluded from code generation (default: none)
     */
    public static final String QUERYDSL_EXCLUDED_PACKAGES = "querydsl.excludedPackages";

    /**
     * comma separated list of class names to be excluded from code generation (default: none)
     */
    public static final String QUERYDSL_EXCLUDED_CLASSES = "querydsl.excludedClasses";

    /**
     * comma separated list of packages to be included into code generation (default: all)
     */
    public static final String QUERYDSL_INCLUDED_PACKAGES = "querydsl.includedPackages";

    /**
     * comma separated list of class names to be included into code generation (default: all)
     */
    public static final String QUERYDSL_INCLUDED_CLASSES = "querydsl.includedClasses";

    /**
     * set whether unknown non-annotated classes should be treated as embeddable (default: false)
     */
    public static final String QUERYDSL_UNKNOWN_AS_EMBEDDABLE = "querydsl.unknownAsEmbeddable";

    /**
     * set the variable name function class
     */
    public static final String QUERYDSL_VARIABLE_NAME_FUNCTION_CLASS = "querydsl.variableNameFunctionClass";

    private APTOptions() { }

}
