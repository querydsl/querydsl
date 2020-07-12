package com.querydsl.r2dbc.binding;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * Index-based bind marker. This implementation creates indexed bind markers using a numeric index and an optional
 * prefix for bind markers to be represented within the query string.
 *
 * @author Mark Paluch
 * @author Jens Schauder
 */
class IndexedBindMarkers implements BindMarkers {

    private static final AtomicIntegerFieldUpdater<IndexedBindMarkers> COUNTER_INCREMENTER = AtomicIntegerFieldUpdater
            .newUpdater(IndexedBindMarkers.class, "counter");

    // access via COUNTER_INCREMENTER
    @SuppressWarnings("unused")
    private volatile int counter;

    private final int offset;
    private final String prefix;

    /**
     * Creates a new {@link IndexedBindMarker} instance given {@code prefix} and {@code beginWith}.
     *
     * @param prefix    bind parameter prefix.
     * @param beginWith the first index to use.
     */
    IndexedBindMarkers(String prefix, int beginWith) {
        this.counter = 0;
        this.prefix = prefix;
        this.offset = beginWith;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.r2dbc.dialect.BindMarkers#next()
     */
    @Override
    public BindMarker next() {

        int index = COUNTER_INCREMENTER.getAndIncrement(this);

        return new IndexedBindMarker(prefix + "" + (index + offset), index);
    }
}
