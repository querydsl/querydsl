/*
 * Copyright 2014, Timo Westkämper
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
package com.querydsl.sql.codegen.support;

import static com.google.common.base.StandardSystemProperty.JAVA_SPECIFICATION_VERSION;

import java.sql.SQLException;

/**
 * A {@code SQLExceptionWrapper} is used to accommodate for
 * Java™ 7's suppressed exceptions.
 *
 * <p>
 * When Java™ 6 is used, a fallback {@code SQLExceptionWrapper}
 * that produces similar output is used instead.
 * </p>
 * @author Shredder121
 */
public abstract class SQLExceptionWrapper {

    public static final SQLExceptionWrapper INSTANCE;

    static {
        double javaVersion
                = Double.parseDouble(JAVA_SPECIFICATION_VERSION.value());
        if (javaVersion > 1.6) {
            INSTANCE = new JaveSE7SQLExceptionWrapper();
        } else {
            INSTANCE = new JavaSE6SQLExceptionWrapper();
        }
    }

    public abstract RuntimeException wrap(SQLException exception);

    public abstract RuntimeException wrap(String message, SQLException exception);

}
