package com.querydsl.r2dbc.binding;

/**
 * BindMarker
 *
 * @see "org.springframework.data.r2dbc.dialect.BindMarker"
 */
public interface BindMarker {

    String getPlaceholder();

    void bind(BindTarget bindTarget, Object value);

    void bindNull(BindTarget bindTarget, Class<?> valueType);
}
