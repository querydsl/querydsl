package com.mysema.query.apt;

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
    
    public static final String QUERYDSL_UNKNOWN_AS_EMBEDDABLE = "querydsl.unknownAsEmbeddable";

    public static final String DEFAULT_OVERWRITE = "defaultOverwrite";

    private APTOptions() {}
    
}
