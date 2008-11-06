/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

/**
 * Results provides
 *
 * @author tiwe
 * @version $Id$
 */
public interface Result {
    
    public interface Result2<A,B> extends Result{
        A getFirst();
        B getSecond();
    }
    
    public interface Result3<A,B,C> extends Result2<A,B>{
        C getThird();
    }
    
    public interface Result4<A,B,C,D> extends Result3<A,B,C>{
        D getFourth();
    }
    
    public interface Result5<A,B,C,D,E> extends Result4<A,B,C,D>{
        E getFifth();
    }
    
    public interface Result6<A,B,C,D,E,F> extends Result5<A,B,C,D,E>{
        F getSixth();
    }
    
    public interface Result7<A,B,C,D,E,F,G> extends Result6<A,B,C,D,E,F>{
        G getSeventh();
    }
    
    public interface Result8<A,B,C,D,E,F,G,H> extends Result7<A,B,C,D,E,F,G>{
        H getEighth();
    }
    
    public interface Result9<A,B,C,D,E,F,G,H,I> extends Result8<A,B,C,D,E,F,G,H>{
        I getNinth();
    }
    
    public interface Result10<A,B,C,D,E,F,G,H,I,J> extends Result9<A,B,C,D,E,F,G,H,I>{
        J getTenth();
    }

}
