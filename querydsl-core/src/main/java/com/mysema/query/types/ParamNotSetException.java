/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types;

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
