/*
 * Copyright 2011, Mysema Ltd
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

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Declaration of a static delegate method
 * 
 * <p>Example :</p>
 * 
 * <pre>
 * {@code @QueryDelegate(User.class)}
 * public static Predicate like(QUser entity, User user) {
 *     BooleanBuilder builder = new BooleanBuilder(); 
 *     if (user.getFirstName() != null) {
 *         builder.and(entity.firstName.eq(user.getFirstName()));
 *     }
 *     if (user.getLastName() != null) {
 *         builder.and(entity.lastName.eq(user.getLastName()));
 *     }
 *     return builder.getValue();
 * }
 * </pre>
 * 
 * <p>This will be then available in the QUser type as an instance method without the first 
 * argument:</p>
 * 
 * <pre>
 * User user = new User();
 * user.setFirstName("John");
 * user.setLastName("Doe");
 * Predicate predicate = QUser.user.like(user);
 * </pre>
 *
 * @author tiwe
 *
 */
@Documented
@Target({METHOD})
@Retention(RUNTIME)
public @interface QueryDelegate {

    /**
     * @return
     */
    Class<?> value();

}
