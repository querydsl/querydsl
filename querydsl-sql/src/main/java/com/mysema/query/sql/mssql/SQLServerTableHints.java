package com.mysema.query.sql.mssql;

public enum SQLServerTableHints {
    NOEXPAND,
    FASTFIRSTROW, 
    FORCESEEK,
    HOLDLOCK,
    NOLOCK, 
    NOWAIT,
    PAGLOCK, 
    READCOMMITTED, 
    READCOMMITTEDLOCK, 
    READPAST, 
    READUNCOMMITTED, 
    REPEATABLEREAD, 
    ROWLOCK, 
    SERIALIZABLE, 
    TABLOCK, 
    TABLOCKX, 
    UPDLOCK, 
    XLOCK
}