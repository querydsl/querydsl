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
package com.querydsl.apt;

/**
 * APT options supported by Querydsl
 *
 * @author tiwe
 *
 */
public final class APTOptions {

    public static final String QUERYDSL_CREATE_DEFAULT_VARIABLE = "querydsl.createDefaultVariable";

    public static final String QUERYDSL_PREFIX = "querydsl.prefix";

    public static final String QUERYDSL_SUFFIX = "querydsl.suffix";

    public static final String QUERYDSL_PACKAGE_SUFFIX = "querydsl.packageSuffix";

    public static final String QUERYDSL_MAP_ACCESSORS = "querydsl.mapAccessors";

    public static final String QUERYDSL_LIST_ACCESSORS = "querydsl.listAccessors";

    public static final String QUERYDSL_ENTITY_ACCESSORS = "querydsl.entityAccessors";

    public static final String QUERYDSL_EXCLUDED_PACKAGES = "querydsl.excludedPackages";

    public static final String QUERYDSL_EXCLUDED_CLASSES = "querydsl.excludedClasses";

    public static final String QUERYDSL_INCLUDED_PACKAGES = "querydsl.includedPackages";

    public static final String QUERYDSL_INCLUDED_CLASSES = "querydsl.includedClasses";

    public static final String QUERYDSL_UNKNOWN_AS_EMBEDDABLE = "querydsl.unknownAsEmbeddable";

    private APTOptions() {}

}
