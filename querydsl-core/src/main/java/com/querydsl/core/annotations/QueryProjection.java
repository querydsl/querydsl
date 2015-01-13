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

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotation for APT based querydsl type generation. Annotate constructors with this annotation.
 * 
 * <p>Example</p>
 * 
 * <pre>
 * class UserInfo {
 * 
 *     private String firstName, lastName;
 *     
 *     {@code @QueryProjection}
 *     public UserInfo(String firstName, String lastName) {
 *         this.firstName = firstName;
 *         this.lastName = lastName;
 *     }
 *     
 *     // getters and setters
 * }  
 * </pre>
 * 
 * <p>The projection can then be used like this</p>
 * 
 * <pre>
 * {@code
 * QUser user = QUser.user;
 * List <UserInfo> result = querydsl.from(user)
 *     .where(user.valid.eq(true))
 *     .list(new QUserInfo(user.firstName, user.lastName));
 * }    
 * </pre>
 */
@Documented
@Target(ElementType.CONSTRUCTOR)
@Retention(RUNTIME)
public @interface QueryProjection {

}
