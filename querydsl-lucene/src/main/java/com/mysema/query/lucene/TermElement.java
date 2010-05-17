package com.mysema.query.lucene;

import com.mysema.query.types.Visitor;
import com.mysema.query.types.expr.EString;
import com.mysema.query.types.expr.EStringConst;

/**
 * TermElement represents the embedded String as a term
 * 
 * @author tiwe
 *
 */
public class TermElement extends EString{

    private static final long serialVersionUID = 2350215644019186076L;

    private final EStringConst string;
    
    public TermElement(String str) {
        this.string = (EStringConst) EStringConst.create(str);
    }
    
    @Override
    public void accept(Visitor v) {
        string.accept(v);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this){
            return true;
        }else if (o instanceof TermElement){
            return ((TermElement)o).string.equals(string);
        }else{
            return false;
        }
    }

    @Override
    public int hashCode(){
        return string.hashCode();
    }
}
