/*
 * Copyright 2011, Mysema Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mysema.query.types;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mysema.query.Tuple;

/**
 * QTuple represents a projection of type Tuple
 *
 * @author tiwe
 *
 */
public class QTuple extends ExpressionBase<Tuple> implements FactoryExpression<Tuple>{

    private static final long serialVersionUID = -2640616030595420465L;

    private final List<Expression<?>> args;

    public QTuple(Expression<?>... args) {
        super(Tuple.class);
        this.args = Arrays.asList(args);
    }

    public QTuple(Expression<?>[]... args) {
        super(Tuple.class);
        this.args = new ArrayList<Expression<?>>();
        for (Expression<?>[] exprs: args){
            this.args.addAll(Arrays.asList(exprs));
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Tuple newInstance(final Object... a) {
        return new Tuple() {

            @Override
            public <T> T get(int index, Class<T> type) {
                return (T) a[index];
            }

            @Override
            public <T> T get(Expression<T> expr) {
                int index = QTuple.this.args.indexOf(expr);
                return index != -1 ? (T) a[index] : null;
            }

            @Override
            public Object[] toArray() {
                return a;
            }

            @Override
            public boolean equals(Object obj) {
                if (obj == this) {
                    return true;
                } else if (obj instanceof Tuple) {
                    return Arrays.equals(a, ((Tuple) obj).toArray());
                } else {
                    return false;
                }
            }

            @Override
            public int hashCode() {
                return Arrays.hashCode(a);
            }

        };
    }

    @Override
    public <R,C> R accept(Visitor<R,C> v, C context) {
        return v.visit(this, context);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj instanceof QTuple) {
            QTuple c = (QTuple)obj;
            return args.equals(c.args) && getType().equals(c.getType());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode(){
        return getType().hashCode();
    }

    @Override
    public List<Expression<?>> getArgs() {
        return args;
    }

}
