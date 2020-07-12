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

import java.time.LocalDate;
import java.time.temporal.ChronoField;

/**
 * @author mc_fish
 */
public final class R2DBCDateConstant<D extends LocalDate> extends LocalDateExpression<D> implements Constant<D> {

    private static final long serialVersionUID = -5745611667058255826L;

    public static <D extends LocalDate> LocalDateExpression<D> create(D date) {
        return new R2DBCDateConstant<D>(date);
    }

    private final D date;

    @SuppressWarnings("unchecked")
    public R2DBCDateConstant(D date) {
        super(ConstantImpl.create(date));
        this.date = (D) date;
    }

    @Override
    public <R, C> R accept(Visitor<R, C> v, C context) {
        return v.visit(this, context);
    }

    @Override
    public NumberExpression<Integer> dayOfMonth() {
        return NumberConstant.create(date.get(ChronoField.DAY_OF_MONTH));
    }

    @Override
    public NumberExpression<Integer> month() {
        return NumberConstant.create(date.get(ChronoField.MONTH_OF_YEAR));
    }

    @Override
    public NumberExpression<Integer> year() {
        return NumberConstant.create(date.get(ChronoField.YEAR));
    }

    @Override
    public NumberExpression<Integer> yearMonth() {
        return NumberConstant.create(date.get(ChronoField.PROLEPTIC_MONTH));
    }

    @Override
    public NumberExpression<Integer> dayOfWeek() {
        return NumberConstant.create(date.get(ChronoField.DAY_OF_WEEK));
    }

    @Override
    public NumberExpression<Integer> dayOfYear() {
        return NumberConstant.create(date.get(ChronoField.DAY_OF_YEAR));
    }

    @Override
    public NumberExpression<Integer> week() {
        return NumberConstant.create(date.get(ChronoField.ALIGNED_WEEK_OF_YEAR));
    }

    @Override
    public D getConstant() {
        return date;
    }

}
