/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql.mssql;

import com.mysema.query.types.path.NumberPath;

/**
 * Convenience functions and constants for SQL Server usage
 *
 * @author tiwe
 *
 */
public final class SQLServerGrammar {

    private SQLServerGrammar(){}

    public static final NumberPath<Long> rowNumber = new NumberPath<Long>(Long.class, "row_number");

    public static final NumberPath<Long> rn = new NumberPath<Long>(Long.class, "rn");

    public static RowNumber rowNumber(){
        return new RowNumber();
    }

}
