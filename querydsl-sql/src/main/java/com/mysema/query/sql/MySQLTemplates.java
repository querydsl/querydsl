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
package com.mysema.query.sql;

import java.math.BigDecimal;

import com.mysema.query.types.Ops;

/**
 * MySQLTemplates is an SQL dialect for MySQL
 *
 * tested with MySQL CE 5.1
 *
 * @author tiwe
 *
 */
public class MySQLTemplates extends SQLTemplates {

    public MySQLTemplates() {
        this('\\', false);
    }

    public MySQLTemplates(boolean quote) {
        this('\\', quote);
    }
    
    public MySQLTemplates(char escape, boolean quote) {
        super("`", escape, quote);
        setParameterMetadataAvailable(false);
        addClass2TypeMappings("bool", Boolean.class);
        addClass2TypeMappings("int", Integer.class);
        
        addClass2TypeMappings("decimal",
                Double.class,
                Float.class,
                BigDecimal.class);
//        addClass2TypeMappings("text", String.class);

        add(Ops.CONCAT, "concat({0}, {1})",0);
        add(Ops.DateTimeOps.YEAR_MONTH, "extract(year_month from {0})");

        // like without escape
        if (escape == '\\') {
            add(Ops.LIKE, "{0} like {1}");
            add(Ops.ENDS_WITH, "{0} like {%1}");
            add(Ops.ENDS_WITH_IC, "{0l} like {%%1}");
            add(Ops.STARTS_WITH, "{0} like {1%}");
            add(Ops.STARTS_WITH_IC, "{0l} like {1%%}");
            add(Ops.STRING_CONTAINS, "{0} like {%1%}");
            add(Ops.STRING_CONTAINS_IC, "{0l} like {%%1%%}");
        }    
        
        add(Ops.MathOps.LOG, "log({1},{0})");
        add(Ops.MathOps.COSH, "(exp({0}) + exp({0} * -1)) / 2");
        add(Ops.MathOps.COTH, "(exp({0} * 2) + 1) / (exp({0} * 2) - 1)");
        add(Ops.MathOps.SINH, "(exp({0}) - exp({0} * -1)) / 2");
        add(Ops.MathOps.TANH, "(exp({0} * 2) - 1) / (exp({0} * 2) + 1)");
        
    }

}
