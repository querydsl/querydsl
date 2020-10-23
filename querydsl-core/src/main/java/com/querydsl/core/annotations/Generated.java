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

package com.querydsl.core.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.CLASS;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * The Generated annotation is used to mark source code that has been generated.
 * It uses <code>CLASS</code> retention to allow byte code analysis tools like
 * Jacoco detect this code as being generated.
 * <p/>
 * It is now used instead of <code>javax.annotation.Generated</code>.
 */

@Documented
@Retention(CLASS)
@Target(TYPE)
public @interface Generated {
    /**
     * The value element MUST have the name of the code generator.
     * The recommended convention is to use the fully qualified name of the
     * code generator.
     */
    String[] value();
}
