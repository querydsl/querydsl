/**
 * 
 */
package com.mysema.query.sql;

import com.mysema.query.types.operation.Ops;

/**
 * @author tiwe
 *
 */
public class DerbyTemplates extends SQLTemplates {
    {
        addClass2TypeMappings("smallint", Byte.class);
        
        add(Ops.CONCAT, "varchar({0} || {1})");
        add(Ops.MathOps.ROUND, "floor({0})");

        add(Ops.DateTimeOps.YEAR, "year({0})");
        add(Ops.DateTimeOps.MONTH, "month({0})");

        add(Ops.DateTimeOps.HOUR, "hour({0})");
        add(Ops.DateTimeOps.MINUTE, "minute({0})");
        add(Ops.DateTimeOps.SECOND, "second({0})");
    }
}