package com.mysema.query.hql;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.mysema.query.types.Constant;
import com.mysema.query.types.Operator;
import com.mysema.query.types.Ops;
import com.mysema.query.types.PathType;

/**
 * HQLTemplates extends JPQLTemplates with Hibernate specific fixes
 * 
 * @author tiwe
 *
 */
public class HQLTemplates extends JPQLTemplates{

    private static final List<Operator<?>> wrapElements = Arrays.<Operator<?>> asList(
            Ops.QuantOps.ALL, 
            Ops.QuantOps.ANY,
            Ops.QuantOps.AVG_IN_COL, 
            Ops.EXISTS);
    
    public static final HQLTemplates DEFAULT = new HQLTemplates();
    
    protected HQLTemplates() {
        //CHECKSTYLE:OFF        
        add(CAST, "cast({0} as {1s})");
        add(Ops.INSTANCE_OF, "{0}.class = {1}"); // TODO : remove this when Hibernate supports type(alias)
        add(MEMBER_OF, "{0} in elements({1})"); // TODO : remove this when Hibernate supports member of properly
        
        // path types
        for (PathType type : new PathType[] { 
                PathType.LISTVALUE,
                PathType.MAPVALUE,
                PathType.MAPVALUE_CONSTANT }) {
            add(type, "{0}[{1}]");
        }
        add(PathType.LISTVALUE_CONSTANT, "{0}[{1s}]");    
        
        // date time
        add(Ops.DateTimeOps.MILLISECOND, "0"); // NOT supported in HQL
        add(Ops.DateTimeOps.SECOND, "second({0})");
        add(Ops.DateTimeOps.MINUTE, "minute({0})");
        add(Ops.DateTimeOps.HOUR, "hour({0})");
        add(Ops.DateTimeOps.DAY_OF_MONTH, "day({0})");
        add(Ops.DateTimeOps.MONTH, "month({0})");
        add(Ops.DateTimeOps.YEAR, "year({0})");
        add(Ops.DateTimeOps.YEAR_MONTH, "year({0}) * 100 + month({0})");
        //CHECKSTYLE:ON
    }
    
    public boolean wrapElements(Operator<?> operator){
        return wrapElements.contains(operator);
    }

    public boolean wrapConstant(Constant<?> expr) {
        Class<?> type = expr.asExpr().getType();
        return type.isArray() || Collection.class.isAssignableFrom(type);
    }

    public boolean isTypeAsString() {
        return true;
    }

}
