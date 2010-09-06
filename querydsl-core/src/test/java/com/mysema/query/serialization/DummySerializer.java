/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.serialization;

import com.mysema.query.types.FactoryExpression;
import com.mysema.query.types.Param;
import com.mysema.query.types.SerializerBase;
import com.mysema.query.types.SubQueryExpression;
import com.mysema.query.types.Templates;

public class DummySerializer extends SerializerBase<DummySerializer>{

    public DummySerializer(Templates patterns) {
        super(patterns);
    }

    @Override
    public Void visit(SubQueryExpression<?> query, Void context) {
        return null;
    }

    @Override
    public Void visit(FactoryExpression<?> expr, Void context) {
        return null;
    }

    @Override
    public Void visit(Param<?> expr, Void context) {
        return null;
    }

}
