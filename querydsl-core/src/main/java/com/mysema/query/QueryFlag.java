package com.mysema.query;

import net.jcip.annotations.Immutable;

import com.mysema.commons.lang.Assert;
import com.mysema.query.types.Expr;
import com.mysema.query.types.custom.CString;

/**
 * Defines a positioned flag in a query for customization of query serialization
 * 
 * @author tiwe
 *
 */
@Immutable
public class QueryFlag {
    
    public enum Position {        
        
        START,
        
        AFTER_SELECT,
        
        AFTER_PROJECTION,
        
        BEFORE_FILTERS,
        
        AFTER_FILTERS,
        
        BEFORE_GROUP_BY,
        
        AFTER_GROUP_BY,
        
        BEFORE_HAVING,
        
        AFTER_HAVING,
        
        BEFORE_ORDER,
        
        AFTER_ORDER,
        
        END        
        
    }
    
    private final Position position;
    
    private final Expr<?> flag;
    
    public QueryFlag(Position position, String flag) {
        this(position, CString.create(flag));
    }
    
    public QueryFlag(Position position, Expr<?> flag) {
        this.position = Assert.notNull(position,"position");
        this.flag = Assert.notNull(flag,"flag");        
    }

    public Position getPosition() {
        return position;
    }

    public Expr<?> getFlag() {
        return flag;
    }

    @Override
    public int hashCode() {
        return flag.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this){
            return true;
        }else if (obj instanceof QueryFlag){
            QueryFlag other = (QueryFlag)obj;
            return other.position.equals(position) && other.flag.equals(flag);
        }else{
            return false;
        }
    }

    @Override
    public String toString(){
        return position + " : " + flag;
    }
}
