package com.querydsl.r2dbc.binding;


/**
 * BindTarget
 *
 * @see "org.springframework.data.r2dbc.dialect.BindTarget"
 */
public interface BindTarget {

    /**
     * Bind a value.
     *
     * @param identifier the identifier to bind to.
     * @param value      the value to bind.
     */
    void bind(String identifier, Object value);

    /**
     * Bind a value to an index. Indexes are zero-based.
     *
     * @param index the index to bind to.
     * @param value the value to bind.
     */
    void bind(int index, Object value);

    /**
     * Bind a {@literal null} value.
     *
     * @param identifier the identifier to bind to.
     * @param type       the type of {@literal null} value.
     */
    void bindNull(String identifier, Class<?> type);

    /**
     * Bind a {@literal null} value.
     *
     * @param index the index to bind to.
     * @param type  the type of {@literal null} value.
     */
    void bindNull(int index, Class<?> type);
}
