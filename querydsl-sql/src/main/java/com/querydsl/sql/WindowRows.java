/*
 * Copyright 2013, Mysema Ltd
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
package com.querydsl.sql;

import java.util.List;

import com.google.common.collect.Lists;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Expression;

/**
 * WindowRows provides the building of the rows/range part of the window function expression
 *
 * @author tiwe
 *
 * @param <A>
 */
public class WindowRows<A> {

    private static final String AND = " and";

    private static final String BETWEEN = " between";

    private static final String CURRENT_ROW = " current row";

    private static final String FOLLOWING = " following";

    private static final String PRECEDING = " preceding";

    private static final String UNBOUNDED = " unbounded";

    public class Between {

        public BetweenAnd unboundedPreceding() {
            str.append(UNBOUNDED);
            str.append(PRECEDING);
            return new BetweenAnd();
        }

        public BetweenAnd currentRow() {
            str.append(CURRENT_ROW);
            return new BetweenAnd();
        }

        public BetweenAnd preceding(Expression<Integer> expr) {
            args.add(expr);
            str.append(PRECEDING);
            str.append(" {" + (offset++) + "}");
            return new BetweenAnd();
        }

        public BetweenAnd preceding(int i) {
            return preceding(ConstantImpl.create(i));
        }

        public BetweenAnd following(Expression<Integer> expr) {
            args.add(expr);
            str.append(FOLLOWING);
            str.append(" {" + (offset++) + "}");
            return new BetweenAnd();
        }

        public BetweenAnd following(int i) {
            return following(ConstantImpl.create(i));
        }
    }

    public class BetweenAnd {

        public BetweenAnd() {
            str.append(AND);
        }

        public WindowFunction<A> unboundedFollowing() {
            str.append(UNBOUNDED);
            str.append(FOLLOWING);
            return rv.withRowsOrRange(str.toString(), args);
        }

        public WindowFunction<A> currentRow() {
            str.append(CURRENT_ROW);
            return rv.withRowsOrRange(str.toString(), args);
        }

        public WindowFunction<A> preceding(Expression<Integer> expr) {
            args.add(expr);
            str.append(PRECEDING);
            str.append(" {" + (offset++) + "}");
            return rv.withRowsOrRange(str.toString(), args);
        }

        public WindowFunction<A> preceding(int i) {
            return preceding(ConstantImpl.create(i));
        }

        public WindowFunction<A> following(Expression<Integer> expr) {
            args.add(expr);
            str.append(FOLLOWING);
            str.append(" {" + (offset++) + "}");
            return rv.withRowsOrRange(str.toString(), args);
        }

        public WindowFunction<A> following(int i) {
            return following(ConstantImpl.create(i));
        }
    }

    private final WindowFunction<A> rv;

    private final StringBuilder str = new StringBuilder();

    private final List<Expression<?>> args = Lists.newArrayList();

    private int offset;

    public WindowRows(WindowFunction<A> windowFunction, String prefix, int offset) {
        this.rv = windowFunction;
        this.offset = offset;
        str.append(prefix);
    }

    public Between between() {
        str.append(BETWEEN);
        return new Between();
    }

    public WindowFunction<A> unboundedPreceding() {
        str.append(UNBOUNDED);
        str.append(PRECEDING);
        return rv.withRowsOrRange(str.toString(), args);
    }

    public WindowFunction<A> currentRow() {
        str.append(CURRENT_ROW);
        return rv.withRowsOrRange(str.toString(), args);
    }

    public WindowFunction<A> preceding(Expression<Integer> expr) {
        args.add(expr);
        str.append(PRECEDING);
        str.append(" {" + (offset++) + "}");
        return rv.withRowsOrRange(str.toString(), args);
    }

    public WindowFunction<A> preceding(int i) {
        return preceding(ConstantImpl.create(i));
    }

}
