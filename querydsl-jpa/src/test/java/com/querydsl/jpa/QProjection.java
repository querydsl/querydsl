package com.querydsl.jpa;

import com.querydsl.jpa.domain.Cat;
import com.querydsl.jpa.domain.QCat;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.expr.StringExpression;

public class QProjection extends ConstructorExpression<Projection>{

    private static final long serialVersionUID = -5866362075090550839L;

    public QProjection(StringExpression str, QCat cat) {
        super(Projection.class, 
                new Class[]{String.class, Cat.class}, new Expression[]{str, cat});
    }

}