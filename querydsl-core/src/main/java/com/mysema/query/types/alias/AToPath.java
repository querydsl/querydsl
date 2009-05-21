package com.mysema.query.types.alias;

import com.mysema.query.types.path.Path;

/**
 * Alias to path
 */
public interface AToPath extends Alias{        
    Path<?> getTo();
}