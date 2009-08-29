/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.binding;

import com.mysema.query.types.path.Path;

/**
 * @author tiwe
 *
 * @param <Base>
 */
public class BindingBuilder<Base> {

    private Path<Base> first;
    
    public BindingBuilder(Path<Base> first) {
        this.first = first;
    }

    public <Source> void from(Path<Source> to) {
        // TODO
    }

    public <Target> void to(Path<Target> to) {
        // TODO
    }

    public <Source> void from(Path<Source> from, Converter<Source, Base> c) {
        // TODO
    }

    public <Target> void to(Path<Target> to, Converter<Base,Target> c) {
        // TODO
    }

    
}
