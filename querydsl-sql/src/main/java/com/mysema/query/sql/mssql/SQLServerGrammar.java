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
package com.mysema.query.sql.mssql;

import com.mysema.query.types.Constant;
import com.mysema.query.types.ConstantImpl;
import com.mysema.query.types.Template;
import com.mysema.query.types.TemplateFactory;
import com.mysema.query.types.expr.DateExpression;
import com.mysema.query.types.expr.NumberExpression;
import com.mysema.query.types.template.DateTemplate;
import com.mysema.query.types.template.NumberTemplate;

/**
 * Convenience functions and constants for SQL Server usage
 *
 * @author tiwe
 *
 */
public final class SQLServerGrammar {

    private SQLServerGrammar() {}
    
    // TODO : generalize
    private static final Template DATEDIFF = TemplateFactory.DEFAULT.create("datediff({0s},{1},{2})");

    // TODO : generalize
    private static final Template DATEADD = TemplateFactory.DEFAULT.create("dateadd({0s},{1},{2})");
    
    public static final NumberExpression<Long> rowNumber = NumberTemplate.create(Long.class, "row_number");

    public static final NumberExpression<Long> rn = NumberTemplate.create(Long.class, "rn");

    static String tableHints(SQLServerTableHints... tableHints) {
        StringBuilder hints = new StringBuilder(" with ").append("(");
        for (int i = 0; i < tableHints.length; i++) {
            if (i > 0) {
                hints.append(", ");
            }
            hints.append(tableHints[i].name());
        }
        hints.append(")");
        return hints.toString();        
    }
    
    public static RowNumber rowNumber() {
        return new RowNumber();
    }
    
    private static final <T> Constant<T> constant(T constant) {
        return new ConstantImpl<T>(constant);
    }
    
    public static <D extends Comparable<D>> NumberExpression<Integer> datediff(DatePart datePart, D start, D end) {
        return NumberTemplate.create(Integer.class, DATEDIFF, constant(datePart.name()), constant(start), constant(end));
    }
    
    public static <D extends Comparable<D>> DateExpression<D> dateadd(DatePart datePart, int num, D date) {
        return DateTemplate.<D>create((Class<D>)date.getClass(), DATEADD, constant(datePart.name()), constant(num), constant(date));
    }
    
}
