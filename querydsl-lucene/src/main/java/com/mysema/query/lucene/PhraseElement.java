package com.mysema.query.lucene;

import com.mysema.query.types.Visitor;
import com.mysema.query.types.expr.StringExpression;
import com.mysema.query.types.expr.StringConstant;

/**
 * PhraseElement represents the embedded String as a phrase
 *
 * @author tiwe
 *
 */
public class PhraseElement extends StringExpression{

    private static final long serialVersionUID = 2350215644019186076L;

    private final StringConstant string;

    public PhraseElement(String str) {
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
        }else if (o instanceof PhraseElement){
            return ((PhraseElement)o).string.equals(string);
        }else{
            return false;
        }
    }

    @Override
    public int hashCode(){
        return string.hashCode();
    }
}
