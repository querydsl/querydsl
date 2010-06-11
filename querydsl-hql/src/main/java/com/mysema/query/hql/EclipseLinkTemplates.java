/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql;

import com.mysema.query.types.Ops;

/**
 * NOTE : this is under construction
 * 
 * @author tiwe
 *
 */
public class EclipseLinkTemplates extends JPQLTemplates{
    
    public static final JPQLTemplates DEFAULT = new EclipseLinkTemplates();    
    
    // TODO : indexed list access
    
    // TODO : cast
    
    protected EclipseLinkTemplates() {
	// LIKE replacements
        add(Ops.STRING_CONTAINS, "locate({1},{0}) > 0");
        add(Ops.STRING_CONTAINS_IC, "locate({1l},{0l}) > 0");
        add(Ops.ENDS_WITH, "locate({1},{0})=length({0})-length({1})+1"); // TODO : simplify
        add(Ops.ENDS_WITH_IC, "locate({1l},{0l})=length({0})-length({1})+1"); // TODO : simplify
        add(Ops.STARTS_WITH, "locate({1},{0})=1");
        add(Ops.STARTS_WITH_IC, "locate({1l},{0l})=1");    
	
	// EclipseLink specific (works at least with Derby, HSQLDB and H2)
        add(Ops.DateTimeOps.SECOND, "func('second',{0})");
        add(Ops.DateTimeOps.MINUTE, "func('minute',{0})");
        add(Ops.DateTimeOps.HOUR, "func('hour',{0})");
        add(Ops.DateTimeOps.DAY_OF_WEEK, "func('dayofweek',{0})");
        add(Ops.DateTimeOps.DAY_OF_MONTH, "func('day',{0})");
        add(Ops.DateTimeOps.DAY_OF_YEAR, "func('dayofyear',{0})");
        add(Ops.DateTimeOps.MONTH, "func('month',{0})");
        add(Ops.DateTimeOps.WEEK, "func('week',{0})");
        add(Ops.DateTimeOps.YEAR, "func('year',{0})");
        add(Ops.DateTimeOps.YEAR_MONTH, "func('year',{0}) * 100 + func('month',{0})");
        
    }
    

}
