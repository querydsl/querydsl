/**
 * 
 */
package com.mysema.query.collections.iterators;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

import org.apache.commons.collections15.Predicate;
import org.apache.commons.collections15.iterators.FilterIterator;
import org.codehaus.janino.ExpressionEvaluator;

/**
 * FilteringIterator provides
 *
 * @author tiwe
 * @version $Id$
 */
public class FilteringIterator<RT> extends FilterIterator<RT>{
    public FilteringIterator(Iterator<?> it,  final ExpressionEvaluator ev) {
        super((Iterator<RT>)it, new Predicate<RT>(){
            public boolean evaluate(RT object) {
                try {
                    return (Boolean) ev.evaluate((Object[])object);
                } catch (InvocationTargetException e) {
                    String error = "Caught " + e.getClass().getName();
                    throw new RuntimeException(error, e);
                }
            }                
        });
    }        
        
}