package com.mysema.query.lucene;

import com.mysema.query.types.Visitor;
import com.mysema.query.types.expr.StringExpression;
import com.mysema.query.types.expr.StringConstant;

/**
 * TermElement represents the embedded String as a term
 *
 * @author tiwe
 *
 */
public class TermElement extends StringExpression{

    private static final long serialVersionUID = 2350215644019186076L;

    private final StringConstant string;

    public TermElement(String str) {
        this.string = (StringConstant) StringConstant.create(str);
    }

    @Override
    public <R,C> R accept(Visitor<R,C> v, C context) {
        return string.accept(v, context);
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
