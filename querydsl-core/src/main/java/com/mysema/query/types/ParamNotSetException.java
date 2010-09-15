/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types;

import com.mysema.query.types.expr.Param;

/**
 * @author tiwe
 *
 */
public class ParamNotSetException extends RuntimeException{

    private static final long serialVersionUID = 2019016965590576490L;

    public ParamNotSetException(Param<?> param) {
        super(param.getNotSetMessage());
    }

}
