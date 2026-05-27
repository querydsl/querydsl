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
package com.querydsl.apt.domain;

import com.querydsl.core.annotations.QueryProjection;
import kotlin.jvm.JvmInline;

public class KotlinValueClassTest {

    /**
     * Simulates a Kotlin value class:
     * {@code @JvmInline value class UserId(val value: String)}
     */
    @JvmInline
    public static final class UserId {
        private final String value;

        public UserId(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * Simulates a Kotlin data class with @QueryProjection and a value class parameter:
     * {@code data class UserDto @QueryProjection constructor(val id: UserId, val name: String)}
     */
    public static class UserDto {

        @QueryProjection
        public UserDto(UserId id, String name) {
        }
    }
}