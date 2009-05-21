/**
 * 
 */
package com.mysema.query.types.expr;

import com.mysema.query.types.Grammar;
import com.mysema.query.types.OrderSpecifier;

/**
 * 
 * @author tiwe
 *
 * @param <D>
 */
@SuppressWarnings("unchecked")
public abstract class EComparable<D extends Comparable> extends ESimple<D>{
    private OrderSpecifier<D> asc;
    private OrderSpecifier<D> desc;
    private EString stringCast;               
    public EComparable(Class<? extends D> type) {super(type);}
    
    public final EBoolean after(D right) {return Grammar.after(this,right);}
    public final EBoolean after(Expr<D> right) {return Grammar.after(this,right);}  
    public final EBoolean aoe(D right) {return Grammar.aoe(this,right);}
    public final EBoolean aoe(Expr<D> right) {return Grammar.aoe(this,right);}  
            
    public final OrderSpecifier<D> asc() { 
        if (asc == null) asc = Grammar.asc(this);
        return asc;
    }        
    public final EBoolean before(D right) {return Grammar.before(this,right);}
    public final EBoolean before(Expr<D> right) {return Grammar.before(this,right);}        
    public final EBoolean boe(D right) {return Grammar.boe(this,right);}          
    public final EBoolean boe(Expr<D> right) {return Grammar.boe(this,right);}
    
    public final EBoolean between(D first, D second) {return Grammar.between(this,first,second);}       
    public final EBoolean between(Expr<D> first, Expr<D> second) {return Grammar.between(this,first,second);}
    
    // cast methods
    public <A extends Number & Comparable<? super A>> ENumber<A> castToNum(Class<A> type){
        return Grammar.numericCast(this, type);
    }        
    public final OrderSpecifier<D> desc() {
        if (desc == null) desc = Grammar.desc(this);
        return desc ;
    }
    public final EBoolean notBetween(D first, D second) {return Grammar.notBetween(this, first, second);}
    public final EBoolean notBetween(Expr<D> first, Expr<D> second) {return Grammar.notBetween(this,first,second);}

   public EString stringValue(){
       if (stringCast == null) stringCast = Grammar.stringCast(this);
       return stringCast;
   }

}