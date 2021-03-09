package com.querydsl.r2dbc.binding;


import java.util.Objects;
import java.util.function.Function;

/**
 * BindMarkersFactory
 *
 * @see "org.springframework.data.r2dbc.dialect.BindMarkersFactory"
 */
@FunctionalInterface
public interface BindMarkersFactory {

    BindMarkers create();

    static BindMarkersFactory indexed(String prefix, int beginWith) {
        if (Objects.isNull(prefix) || prefix.isEmpty()) {
            throw new IllegalArgumentException("Prefix must not be null!");
        }
        return () -> new IndexedBindMarkers(prefix, beginWith);
    }

    static BindMarkersFactory anonymous(String placeholder) {
        if (Objects.isNull(placeholder) || placeholder.isEmpty()) {
            throw new IllegalArgumentException("Placeholder must not be empty!");
        }

        return () -> new AnonymousBindMarkers(placeholder);
    }

    static BindMarkersFactory named(String prefix, String namePrefix, int maxLength) {
        return named(prefix, namePrefix, maxLength, Function.identity());
    }

    static BindMarkersFactory named(String prefix, String namePrefix, int maxLength,
                                    Function<String, String> hintFilterFunction) {
        if (Objects.isNull(prefix) || prefix.isEmpty()) {
            throw new IllegalArgumentException("Prefix must not be null!");
        }
        if (Objects.isNull(namePrefix) || namePrefix.isEmpty()) {
            throw new IllegalArgumentException("Index prefix must not be null!");
        }
        if (Objects.isNull(hintFilterFunction)) {
            throw new IllegalArgumentException("Hint filter function must not be null!");
        }

        return () -> new NamedBindMarkers(prefix, namePrefix, maxLength, hintFilterFunction);
    }

}
