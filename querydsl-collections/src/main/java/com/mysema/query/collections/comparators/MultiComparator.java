/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections.comparators;

import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;

import org.apache.commons.collections15.comparators.ComparableComparator;
import org.codehaus.janino.ExpressionEvaluator;

/**
 * MultiComparator provides
 *
 * @author tiwe
 * @version $Id$
 */

public class MultiComparator implements Comparator<Object[]> {

    private Comparator<Object> naturalOrder = ComparableComparator.getInstance();

    private ExpressionEvaluator ev;
    
    private boolean[] asc;
    
    public MultiComparator(ExpressionEvaluator ev, boolean[] directions) {
        this.ev = ev;
        this.asc = directions;
    }

    public int compare(Object[] o1, Object[] o2) {
        try{
            o1 = (Object[]) ev.evaluate(o1);
            o2 = (Object[]) ev.evaluate(o2);
            for (int i = 0; i < o1.length; i++){
                int res = naturalOrder.compare(o1[i], o2[i]);
                if (res != 0){
                    return asc[i] ? res : -res;
                }
            }
            return 0;    
        }catch(InvocationTargetException ite){
            throw new RuntimeException("Caught " + ite.getClass().getName());
        }         
    }

}
