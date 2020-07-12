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
package com.querydsl.r2dbc.support;

/**
 * A {@code SQLExceptionWrapper} is used to accommodate for
 * Java™ 7's suppressed exceptions.
 *
 * <p>
 * When Java™ 6 is used, a fallback {@code SQLExceptionWrapper}
 * that produces similar output is used instead.
 * </p>
 *
 * @author Shredder121
 */
public abstract class AbstractR2DBCExceptionWrapper {

    public static final AbstractR2DBCExceptionWrapper INSTANCE;

    static {
        INSTANCE = new R2DBCSQLExceptionWrapper();
    }

    public abstract RuntimeException wrap(Throwable exception);

    public abstract RuntimeException wrap(String message, Throwable exception);

}
