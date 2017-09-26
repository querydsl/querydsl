/*
 * Copyright 2017, The Querydsl Team (http://www.querydsl.com/team)
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
package com.querydsl.core.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Points to the source of query delegates for a type. This is needed for incremental compilation, because otherwise
 * the annotation processor cannot find the query delegate methods.
 *
 * @author nimehner
 *
 */
@Documented
@Target({TYPE})
@Retention(RUNTIME)
public @interface QueryDelegateSource {

    /**
     * Target type for delegate source
     */
    Class<?>[] value();

}
