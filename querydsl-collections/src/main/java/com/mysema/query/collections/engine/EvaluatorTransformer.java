/**
 * 
 */
package com.mysema.query.collections.engine;

import org.apache.commons.collections15.Transformer;

import com.mysema.codegen.Evaluator;

/**
 * @author tiwe
 *
 * @param <S>
 * @param <T>
 */
public class EvaluatorTransformer<S, T> implements Transformer<S, T> {
    
    private final Evaluator<T> ev;

    public EvaluatorTransformer(Evaluator<T> ev) {
        this.ev = ev;
    }
    
    @Override
    public T transform(S input) {         
        if (input.getClass().isArray()){
            return ev.evaluate((Object[]) input);    
        }else{
            return ev.evaluate(new Object[]{input});
        }            
    }
}