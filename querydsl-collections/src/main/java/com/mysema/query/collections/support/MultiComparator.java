/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections.support;

import java.util.Comparator;

import org.apache.commons.collections15.comparators.ComparableComparator;

import com.mysema.query.collections.eval.Evaluator;

/**
 * MultiComparator compares 
 *
 * @author tiwe
 * @version $Id$
 */

public class MultiComparator implements Comparator<Object[]> {

    private Comparator<Object> naturalOrder = ComparableComparator.getInstance();

    private Evaluator ev;
    
    private boolean[] asc;
    
    public MultiComparator(Evaluator ev, boolean[] directions) {
        this.ev = ev;
        this.asc = directions.clone();
    }

    public int compare(Object[] o1, Object[] o2) {
        o1 = (Object[]) ev.evaluate(o1);
        o2 = (Object[]) ev.evaluate(o2);
        for (int i = 0; i < o1.length; i++){
            int res = naturalOrder.compare(o1[i], o2[i]);
            if (res != 0){
                return asc[i] ? res : -res;
            }
        }
        return 0;          
    }

}
