/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.r2dbc;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.BeanPath;
import com.querydsl.core.types.dsl.PathInits;

import javax.annotation.Nullable;

/**
 * {@code R2DBCBeanPath} represents bean paths
 *
 * @param <T> expression type
 * @author mc_fish
 */
public class R2DBCBeanPath<T> extends BeanPath<T> {

    private static final long serialVersionUID = -1845524024957822731L;

    public R2DBCBeanPath(Class<? extends T> type, String variable) {
        super(type, variable);
    }

    public R2DBCBeanPath(Class<? extends T> type, Path<?> parent, String property) {
        super(type, parent, property);
    }

    public R2DBCBeanPath(Class<? extends T> type, PathMetadata metadata) {
        super(type, metadata);
    }

    public R2DBCBeanPath(Class<? extends T> type, PathMetadata metadata, @Nullable PathInits inits) {
        super(type, metadata, inits);
    }

    /**
     * Create a new Date path
     *
     * @param <A>
     * @param property property name
     * @param type     property type
     * @return property path
     */
    @SuppressWarnings("unchecked")
    protected <A extends Comparable> LocalDatePath<A> createLocalDate(String property, Class<? super A> type) {
        return add(new LocalDatePath<A>((Class) type, forProperty(property)));
    }

    /**
     * Create a new DateTime path
     *
     * @param <A>
     * @param property property name
     * @param type     property type
     * @return property path
     */
    @SuppressWarnings("unchecked")
    protected <A extends Comparable> LocalDateTimePath<A> createLocalDateTime(String property, Class<? super A> type) {
        return add(new LocalDateTimePath<A>((Class) type, forProperty(property)));
    }

    /**
     * Create a new Time path
     *
     * @param <A>
     * @param property property name
     * @param type     property type
     * @return property path
     */
    @SuppressWarnings("unchecked")
    protected <A extends Comparable> LocalTimePath<A> createLocalTime(String property, Class<? super A> type) {
        return add(new LocalTimePath<A>((Class) type, forProperty(property)));
    }

}
