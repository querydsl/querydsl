/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.mysema.query.grammar.Ops.Op;
import com.mysema.query.grammar.types.PathMetadata;
import com.mysema.query.grammar.types.PathMetadata.PathType;
import com.mysema.query.serialization.OperationPatterns;

/**
 * HqlOps extends OperationPatterns to provide operator patterns for HQL serialization
 * 
 * @author tiwe
 * @version $Id$
 */
public class HqlOps extends OperationPatterns {
    
    public static final List<Op<?>> wrapCollectionsForOp;
    
    static{
        wrapCollectionsForOp = Collections.<Op<?>>unmodifiableList(Arrays.<Op<?>>asList(
                Ops.IN, Ops.NOTIN, 
                OpQuant.ALL, OpQuant.ANY, 
                OpQuant.EXISTS, OpQuant.NOTEXISTS));
    }
    
    public HqlOps(){            
        // boolean
        add(Ops.AND, "%s and %s",36);
        add(Ops.NOT, "not %s",3);
        add(Ops.OR, "%s or %s",38);
        add(Ops.XNOR, "%s xnor %s",39);        
        add(Ops.XOR, "%s xor %s",39);
        
        // comparison
        add(Ops.BETWEEN, "%s between %s and %s",30);
        add(Ops.NOTBETWEEN, "%s not between %s and %s",30);
        
        // numeric
        add(Ops.SQRT, "sqrt(%s)");
                
        // various
        add(Ops.IN, "%s in %s");
        add(Ops.NOTIN, "%s not in %s");        
        add(Ops.ISNULL, "%s is null",26);
        add(Ops.ISNOTNULL, "%s is not null",26);
        
        // string
        add(Ops.CONCAT, "%s || %s",37);
        add(Ops.LIKE, "%s like %s",27);
        add(Ops.LOWER, "lower(%s)");        
        add(Ops.SUBSTR1ARG, "substring(%s,%s)");
        add(Ops.SUBSTR2ARGS, "substring(%s,%s,%s)");
        add(Ops.TRIM, "trim(%s)");
        add(Ops.UPPER, "upper(%s)");
                
        // HQL specific
        add(OpHql.SUM, "sum(%s)");
        add(OpHql.SYSDATE, "sysdate");
        add(OpHql.CURRENT_DATE, "current_date()");
        add(OpHql.CURRENT_TIME, "current_time()");
        add(OpHql.CURRENT_TIMESTAMP, "current_timestamp()");
        add(OpHql.SECOND, "second(%s)");
        add(OpHql.MINUTE, "minute(%s)");
        add(OpHql.HOUR, "hour(%s)");
        add(OpHql.DAY, "day(%s)");
        add(OpHql.MONTH, "month(%s)");
        add(OpHql.YEAR, "year(%s)");
        
        // quantified expressions
        add(OpQuant.AVG_IN_COL, "avg(%s)");
        add(OpQuant.MAX_IN_COL, "max(%s)");
        add(OpQuant.MIN_IN_COL, "min(%s)");
        
        add(OpQuant.ANY, "any %s");
        add(OpQuant.ALL, "all %s");
        add(OpQuant.EXISTS, "exists %s");
        add(OpQuant.NOTEXISTS, "not exists %s");        
        
        // path types
        for (PathType type : new PathType[]{PathMetadata.LISTVALUE, PathMetadata.LISTVALUE_CONSTANT, PathMetadata.MAPVALUE, PathMetadata.MAPVALUE_CONSTANT}){
            add(type,"%s[%s]");    
        }
        add(PathMetadata.PROPERTY,"%s.%s"); 
        add(PathMetadata.SIZE,"%s.size");
        add(PathMetadata.VARIABLE,"%s"); 
        
        // HQL types
        add(HqlPathType.MINELEMENT, "minelement(%s)");
        add(HqlPathType.MAXELEMENT, "max(%s");
        add(HqlPathType.MININDEX, "minelement(%s)");
        add(HqlPathType.MAXINDEX, "minelement(%s)");
        add(HqlPathType.LISTINDICES, "indices(%s)");
        add(HqlPathType.MAPINDICES, "indices(%s)");
    }
        
    /**
     * The Interface OpHql.
     */
    public interface OpHql{
        Op<java.util.Date> CURRENT_DATE = new Op<java.util.Date>();
        Op<java.util.Date> CURRENT_TIME = new Op<java.util.Date>();
        Op<java.util.Date> CURRENT_TIMESTAMP = new Op<java.util.Date>();
        Op<java.util.Date> DAY = new Op<java.util.Date>();
        Op<java.util.Date> HOUR = new Op<java.util.Date>();
        Op<java.lang.Boolean> ISEMPTY = new Op<java.lang.Boolean>();
        Op<java.lang.Boolean> ISNOTEMPTY = new Op<java.lang.Boolean>();
        Op<java.util.Date> MINUTE = new Op<java.util.Date>();
        Op<java.util.Date> MONTH = new Op<java.util.Date>();
        Op<java.util.Date> SECOND = new Op<java.util.Date>();
        Op<java.lang.Number> SUM = new Op<java.lang.Number>();
        Op<java.util.Date> SYSDATE = new Op<java.util.Date>();
        Op<java.util.Date> YEAR = new Op<java.util.Date>();
    }
        
    /**
     * The Interface OpQuant.
     */
    public interface OpQuant{
        Op<java.lang.Number> AVG_IN_COL = new Op<java.lang.Number>();
        Op<java.lang.Number> MAX_IN_COL = new Op<java.lang.Number>();
        Op<java.lang.Number> MIN_IN_COL = new Op<java.lang.Number>();   
        
//        some / any = true for any
//        all        = true for all
//        exists     = true is subselect matches
//        not exists = true if subselect doesn't match
        Op<?> ANY = new Op<Object>();
        Op<?> ALL = new Op<Object>();
        Op<?> EXISTS = new Op<Object>();
        Op<?> NOTEXISTS = new Op<Object>();
    }
    
    /**
     * The Interface HqlPathType.
     */
    public interface HqlPathType{
        PathType MINELEMENT = new PathType(); 
        PathType MAXELEMENT = new PathType();
        PathType MININDEX =  new PathType();
        PathType MAXINDEX = new PathType();
        PathType LISTINDICES = new PathType();
        PathType MAPINDICES = new PathType();    
    } 
    
}
