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

/**
 * A single indexed bind marker.
 */
class IndexedBindMarker implements BindMarker {

    private final String placeholder;

    private final int index;

    IndexedBindMarker(String placeholder, int index) {
        this.placeholder = placeholder;
        this.index = index;
    }

    @Override
    public String getPlaceholder() {
        return placeholder;
    }

    @Override
    public void bind(BindTarget target, Object value) {
        target.bind(this.index, value);
    }

    @Override
    public void bindNull(BindTarget target, Class<?> valueType) {
        target.bindNull(this.index, valueType);
    }

    public int getIndex() {
        return index;
    }

}
