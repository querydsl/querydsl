package com.mysema.query.sql;

import com.mysema.query.types.Expr;
import com.mysema.query.types.path.PEntity;

public interface Key <E,P>{
    
    PEntity<?> getEntity();
    
    Expr<P> getProperty();

}
