package com.mysema.query.sql.mssql;

import com.mysema.query.types.path.PNumber;

public final class SQLServerGrammar {

    private SQLServerGrammar(){}
    
    public static final PNumber<Long> rowNumber = new PNumber<Long>(Long.class, "row_number");
    
    public static final PNumber<Long> rn = new PNumber<Long>(Long.class, "rn");
    
    public static RowNumber rowNumber(){
        return new RowNumber();
    }
    
}
