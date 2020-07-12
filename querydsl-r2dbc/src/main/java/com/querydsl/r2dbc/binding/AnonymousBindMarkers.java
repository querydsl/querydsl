/*
 * Copyright 2019-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.r2dbc.binding;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * AnonymousBindMarkers
 *
 * @see org.springframework.data.r2dbc.dialect.AnonymousBindMarkers
 */
class AnonymousBindMarkers implements BindMarkers {

    private static final AtomicIntegerFieldUpdater<AnonymousBindMarkers> COUNTER_INCREMENTER = AtomicIntegerFieldUpdater
            .newUpdater(AnonymousBindMarkers.class, "counter");

    // access via COUNTER_INCREMENTER
    @SuppressWarnings("unused")
    private volatile int counter;

    private final String placeholder;

    AnonymousBindMarkers(String placeholder) {
        this.counter = 0;
        this.placeholder = placeholder;
    }

    @Override
    public BindMarker next() {

        int index = COUNTER_INCREMENTER.getAndIncrement(this);

        return new IndexedBindMarker(placeholder, index);
    }

}
