/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.binding;

import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.Path;

/**
 * @author tiwe
 *
 * @param <First>
 * @param <Second>
 */
public abstract class Bindings<First extends PEntity<?>,Second extends PEntity<?>> {
    
    public abstract void configure(First first, Second second);

    public <F> void bind(Path<F> f, Path<F> s){
        bind(f).to(s);
        bind(s).from(f);
    }
    
    public <F,S> void bind(Path<F> f, Path<S> s, Converter<F,S> converter){
        bind(f).to(s, converter);
        bind(s).from(f, converter);
    }
    
    public <F> BindingBuilder<F> bind(Path<F> f){
        return new BindingBuilder<F>(f);
    }
}
