/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.binding;

/**
 * @author tiwe
 *
 * @param <First>
 * @param <Second>
 */
public interface Converter<Source,Target> {
    
    Target toTarget(Source f);
    
    Source toSource(Target f);

}
