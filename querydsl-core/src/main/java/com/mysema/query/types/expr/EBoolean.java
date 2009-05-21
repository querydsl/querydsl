/**
 * 
 */
package com.mysema.query.types.expr;

import com.mysema.query.types.Grammar;

/**
 * 
 * @author tiwe
 *
 */
public abstract class EBoolean extends EComparable<Boolean>{
    private EBoolean not;        
    public EBoolean() {super(Boolean.class);}
    
    public final EBoolean and(EBoolean right) {return Grammar.and(this, right);}
    public final EBoolean not(){
        if (not == null) not = Grammar.not(this);
        return not;
    }        
    public final EBoolean or(EBoolean right) {return Grammar.or(this, right);}
}