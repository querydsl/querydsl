package com.mysema.query;

import com.mysema.query.jpa.domain.Cat;
import com.mysema.query.jpa.domain.QCat;
import com.mysema.query.types.ConstructorExpression;
import com.mysema.query.types.Expression;
import com.mysema.query.types.expr.StringExpression;

public class QProjection extends ConstructorExpression<Projection>{

    private static final long serialVersionUID = -5866362075090550839L;

    public QProjection(StringExpression str, QCat cat) {
        super(Projection.class, 
                new Class[]{String.class, Cat.class}, new Expression[]{str, cat});
    }

}