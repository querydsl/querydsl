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
package com.querydsl.jdo;

import com.querydsl.core.types.JavaTemplates;
import com.querydsl.core.types.Ops;

/**
 * JDOQLTemplates provides patterns for JDOQL serialization
 *
 * @author tiwe
 *
 */
public final class JDOQLTemplates extends JavaTemplates {

    @SuppressWarnings("FieldNameHidesFieldInSuperclass") //Intentional
    public static final JDOQLTemplates DEFAULT = new JDOQLTemplates();

    protected JDOQLTemplates() {
        // String
        add(Ops.STRING_CONTAINS, "{0}.indexOf({1}) > -1", 25);
        add(Ops.STRING_CONTAINS_IC, "{0l}.indexOf({1l}) > -1", 25);
        add(Ops.EQ_IGNORE_CASE, "{0l}.equals({1l})");
        add(Ops.STRING_IS_EMPTY, "{0} == \"\"", 25);
        add(Ops.LIKE, "{0}.like({1})");
        add(Ops.LIKE_ESCAPE, "{0}.like({1})");

        add(Ops.STRING_CAST, "(String){0}");

        // Date
        add(Ops.DateTimeOps.MONTH, "({0}.getMonth() + 1)"); // getMonth() in JDO returns a range from 0-11
        add(Ops.DateTimeOps.DAY_OF_MONTH, "{0}.getDay()");
        add(Ops.DateTimeOps.MILLISECOND, "0"); // NOT supported in JDOQL

        add(Ops.DateTimeOps.YEAR_MONTH, "({0}.getYear() * 100 + {0}.getMonth() + 1)");

        // other
        add(Ops.ALIAS, "{0} {1}");
    }

}
