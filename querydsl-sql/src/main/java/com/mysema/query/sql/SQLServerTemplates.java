package com.mysema.query.sql;

import com.mysema.query.types.operation.Ops;

/**
 * SQLServerTemplates is an SQL dialect for Microsoft SQL Server
 * 
 * tested with MS SQL Server 2005 Express
 * 
 * @author tiwe
 *
 */
// NOTE : under construction
class SQLServerTemplates extends SQLTemplates{
    {
        add(Ops.DateTimeOps.HOUR, "datepart(hour, {0})");
        add(Ops.DateTimeOps.MINUTE, "datepart(minute, {0})");
        add(Ops.DateTimeOps.SECOND, "datepart(second, {0})");
    }
}
