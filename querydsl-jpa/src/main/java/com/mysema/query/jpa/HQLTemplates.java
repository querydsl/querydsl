package com.mysema.query.jpa;

import java.util.Arrays;
import java.util.List;

import com.mysema.query.types.Operator;
import com.mysema.query.types.Ops;
import com.mysema.query.types.PathType;

/**
 * HQLTemplates extends JPQLTemplates with Hibernate specific extensions
 *
 * @author tiwe
 *
 */
public class HQLTemplates extends JPQLTemplates {

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
        add(PathType.COLLECTION_ANY, "any elements({0})");
        
        add(Ops.CONTAINS_KEY, "{1} in indices({0})");
        add(Ops.CONTAINS_VALUE, "{1} in elements({0})");

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

    @Override
    public boolean wrapElements(Operator<?> operator){
        return wrapElements.contains(operator);
    }

    @Override
    public boolean isTypeAsString() {
        return true;
    }

    @Override
    public String getExistsProjection(){
        return "1";
    }
    
    @Override
    public boolean isEnumInPathSupported() {
        // related : http://opensource.atlassian.com/projects/hibernate/browse/HHH-5159
        return false;
    }
    
}
