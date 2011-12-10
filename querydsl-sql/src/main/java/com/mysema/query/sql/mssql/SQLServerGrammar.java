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

import com.mysema.query.types.expr.NumberExpression;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.template.NumberTemplate;

/**
 * Convenience functions and constants for SQL Server usage
 *
 * @author tiwe
 *
 */
public final class SQLServerGrammar {

    private SQLServerGrammar() {}

    public static final NumberExpression<Long> rowNumber = NumberTemplate.create(Long.class, "row_number");

    public static final NumberPath<Long> rn = new NumberPath<Long>(Long.class, "rn");

    public static RowNumber rowNumber() {
        return new RowNumber();
    }

}
