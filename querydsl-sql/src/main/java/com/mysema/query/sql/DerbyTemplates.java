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
        add(Ops.CONCAT, "{0} || {1}");
        add(Ops.MathOps.ROUND, "floor({0})");
        add(Ops.SUBSTR_1ARG, "substr({0},{1}+1)");
        add(Ops.SUBSTR_2ARGS, "substr({0},{1}+1,{2}+1)");

//        add(Ops.STARTS_WITH, "{0} like ({1} || '%')");
//        add(Ops.ENDS_WITH, "{0} like ('%' || {1})");
//        add(Ops.STARTS_WITH_IC, "lower({0}) like (lower({1}) || '%')");
//        add(Ops.ENDS_WITH_IC, "lower({0}) like ('%' || lower({1}))");

        add(Ops.DateTimeOps.YEAR, "year({0})");
        add(Ops.DateTimeOps.MONTH, "month({0})");

        add(Ops.DateTimeOps.HOUR, "hour({0})");
        add(Ops.DateTimeOps.MINUTE, "minute({0})");
        add(Ops.DateTimeOps.SECOND, "second({0})");
    }
}