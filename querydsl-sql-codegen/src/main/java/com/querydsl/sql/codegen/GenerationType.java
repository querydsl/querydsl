package com.querydsl.sql.codegen;

/**
 * The {@code GenerationType} is used to decide what should be generated based the {@link NamingStrategy}.
 */
public enum GenerationType {

    /**
     * The whole class must be generated.
     */
    CLASS,

    /**
     * Only the foreign keys must be generated.
     */
    FOREIGN_KEYS,

    /**
     * No generate anything.
     */
    NONE;

}
