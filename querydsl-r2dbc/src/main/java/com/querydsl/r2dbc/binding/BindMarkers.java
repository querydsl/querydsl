package com.querydsl.r2dbc.binding;

/**
 * BindMarkers
 *
 * @see "org.springframework.data.r2dbc.dialect.BindMarkers"
 */
@FunctionalInterface
public interface BindMarkers {

    BindMarker next();

    default BindMarker next(String hint) {
        return next();
    }
}
