/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.mysema.query.serialization.OperationPatterns;
import com.mysema.query.types.operation.Ops;
import com.mysema.query.types.operation.Ops.Op;
import com.mysema.query.types.path.PathMetadata;
import com.mysema.query.types.path.PathMetadata.PathType;

/**
 * HqlOps extends OperationPatterns to provide operator patterns for HQL serialization
 * 
 * @author tiwe
 * @version $Id$
 */
public class HQLOps extends OperationPatterns {
    
    public static final HQLOps DEFAULT = new HQLOps();
    
    public static final List<Op<?>> wrapCollectionsForOp;
    
    static{
        wrapCollectionsForOp = Collections.<Op<?>>unmodifiableList(Arrays.<Op<?>>asList(
                Ops.IN, 
                Ops.NOTIN,                
                OpQuant.ALL, 
                OpQuant.ANY,
                OpQuant.AVG_IN_COL,
                OpQuant.EXISTS, 
                OpQuant.NOTEXISTS));
    }
    
    public HQLOps(){            
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
        add(Ops.OpMath.SQRT, "sqrt(%s)");
                
        // various
        add(Ops.IN, "%s in %s");
        add(Ops.NOTIN, "%s not in %s");        
        add(Ops.ISNULL, "%s is null",26);
        add(Ops.ISNOTNULL, "%s is not null",26);
        add(Ops.COL_ISEMPTY, "%s is empty");
        add(Ops.COL_ISNOTEMPTY, "%s is not empty");
        
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
        
        // date time
        add(Ops.OpDateTime.SYSDATE, "sysdate");
        add(Ops.OpDateTime.CURRENT_DATE, "current_date()");
        add(Ops.OpDateTime.CURRENT_TIME, "current_time()");
        add(Ops.OpDateTime.CURRENT_TIMESTAMP, "current_timestamp()");
        add(Ops.OpDateTime.SECOND, "second(%s)");
        add(Ops.OpDateTime.MINUTE, "minute(%s)");
        add(Ops.OpDateTime.HOUR, "hour(%s)");
        add(Ops.OpDateTime.DAY, "day(%s)");
        add(Ops.OpDateTime.MONTH, "month(%s)");
        add(Ops.OpDateTime.YEAR, "year(%s)");
        
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
        add(HqlPathType.MAXELEMENT, "maxelement(%s");
        add(HqlPathType.MININDEX, "minindex(%s)");
        add(HqlPathType.MAXINDEX, "maxindex(%s)");
        add(HqlPathType.LISTINDICES, "indices(%s)");
        add(HqlPathType.MAPINDICES, "indices(%s)");
    }
        
    /**
     * The Interface OpHql.
     */
    public interface OpHql{
        Op<java.lang.Boolean> ISEMPTY = new Op<java.lang.Boolean>(Collection.class);
        Op<java.lang.Boolean> ISNOTEMPTY = new Op<java.lang.Boolean>(Collection.class);
        Op<Number> SUM = new Op<Number>(Number.class);
    }
        
    /**
     * The Interface OpQuant.
     */
    public interface OpQuant{
        Op<java.lang.Number> AVG_IN_COL = new Op<java.lang.Number>(Collection.class);
        Op<java.lang.Number> MAX_IN_COL = new Op<java.lang.Number>(Collection.class);
        Op<java.lang.Number> MIN_IN_COL = new Op<java.lang.Number>(Collection.class);   
        
//        some / any = true for any
//        all        = true for all
//        exists     = true is subselect matches
//        not exists = true if subselect doesn't match
        Op<?> ANY = new Op<Object>(Object.class);
        Op<?> ALL = new Op<Object>(Object.class);
        Op<?> EXISTS = new Op<Object>(Object.class);
        Op<?> NOTEXISTS = new Op<Object>(Object.class);
    }
    
    /**
     * The Interface HqlPathType.
     */
    public interface HqlPathType{
        PathType MINELEMENT = new PathType("min element"); 
        PathType MAXELEMENT = new PathType("max element");
        PathType MININDEX =  new PathType("min index");
        PathType MAXINDEX = new PathType("max index");
        PathType LISTINDICES = new PathType("list indices");
        PathType MAPINDICES = new PathType("map indices");    
    } 
    
}
