package com.querydsl.r2dbc.binding;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.function.Function;

/**
 * Name-based bind markers.
 *
 * @author Mark Paluch
 */
class NamedBindMarkers implements BindMarkers {

    private static final AtomicIntegerFieldUpdater<NamedBindMarkers> COUNTER_INCREMENTER = AtomicIntegerFieldUpdater
            .newUpdater(NamedBindMarkers.class, "counter");

    // access via COUNTER_INCREMENTER
    @SuppressWarnings("unused")
    private volatile int counter;

    private final String prefix;

    private final String namePrefix;

    private final int nameLimit;

    private final Function<String, String> hintFilterFunction;

    NamedBindMarkers(String prefix, String namePrefix, int nameLimit, Function<String, String> hintFilterFunction) {

        this.prefix = prefix;
        this.namePrefix = namePrefix;
        this.nameLimit = nameLimit;
        this.hintFilterFunction = hintFilterFunction;
    }

    @Override
    public BindMarker next() {

        String name = nextName();

        return new NamedBindMarker(prefix + name, name);
    }

    @Override
    public BindMarker next(String hint) {
        if (Objects.isNull(prefix) || prefix.isEmpty()) {
            throw new IllegalArgumentException("Name hint must not be null");
        }

        String name = nextName() + hintFilterFunction.apply(hint);

        if (name.length() > nameLimit) {
            name = name.substring(0, nameLimit);
        }

        return new NamedBindMarker(prefix + name, name);
    }

    private String nextName() {

        int index = COUNTER_INCREMENTER.getAndIncrement(this);
        return namePrefix + index;
    }

    /**
     * A single named bind marker.
     */
    static class NamedBindMarker implements BindMarker {

        private final String placeholder;

        private final String identifier;

        NamedBindMarker(String placeholder, String identifier) {

            this.placeholder = placeholder;
            this.identifier = identifier;
        }

        @Override
        public String getPlaceholder() {
            return this.placeholder;
        }

        @Override
        public void bind(BindTarget target, Object value) {
            target.bind(this.identifier, value);
        }

        @Override
        public void bindNull(BindTarget target, Class<?> valueType) {
            target.bindNull(this.identifier, valueType);
        }

    }

}
