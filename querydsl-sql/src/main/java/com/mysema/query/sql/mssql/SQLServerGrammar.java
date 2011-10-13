/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
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
