/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
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
package com.querydsl.r2dbc;

import com.querydsl.core.NumberConstant;
import com.querydsl.core.types.Constant;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Visitor;
import com.querydsl.core.types.dsl.NumberExpression;

import java.time.LocalTime;
import java.time.temporal.ChronoField;

/**
 * @author mc_fish
 */
public final class R2DBCTimeConstant<D extends LocalTime> extends LocalTimeExpression<D> implements Constant<D> {

    private static final long serialVersionUID = -7835941761930555480L;

    public static <D extends LocalTime> LocalTimeExpression<D> create(D time) {
        return new R2DBCTimeConstant<D>(time);
    }

    private final D time;

    @SuppressWarnings("unchecked")
    public R2DBCTimeConstant(D time) {
        super(ConstantImpl.create(time));
        this.time = (D) time;
    }

    @Override
    public <R, C> R accept(Visitor<R, C> v, C context) {
        return v.visit(this, context);
    }

    @Override
    public NumberExpression<Integer> hour() {
        return NumberConstant.create(time.get(ChronoField.HOUR_OF_DAY));
    }

    @Override
    public NumberExpression<Integer> minute() {
        return NumberConstant.create(time.get(ChronoField.MINUTE_OF_HOUR));
    }

    @Override
    public NumberExpression<Integer> second() {
        return NumberConstant.create(time.get(ChronoField.SECOND_OF_MINUTE));
    }

    @Override
    public NumberExpression<Integer> milliSecond() {
        return NumberConstant.create(time.get(ChronoField.MILLI_OF_SECOND));
    }

    @Override
    public D getConstant() {
        return time;
    }

}
