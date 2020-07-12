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

import com.querydsl.core.types.Constant;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Visitor;
import com.querydsl.core.types.dsl.NumberExpression;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @author mc_fish
 */
public final class R2DBCDateTimeConstant<D extends LocalDateTime> extends LocalDateTimeExpression<D> implements Constant<D> {

    private static final long serialVersionUID = 4578416585568476532L;

    public static <D extends LocalDateTime> LocalDateTimeExpression<D> create(D date) {
        return new R2DBCDateTimeConstant<D>(date);
    }

    private final LocalDateTime localDateTime;
    private final R2DBCDateConstant<LocalDate> date;
    private final R2DBCTimeConstant<LocalTime> time;

    @SuppressWarnings("unchecked")
    public R2DBCDateTimeConstant(D date) {
        super(ConstantImpl.create(date));
        localDateTime = date;
        this.date = new R2DBCDateConstant<>(date.toLocalDate());
        this.time = new R2DBCTimeConstant<>(date.toLocalTime());
    }

    @Override
    public <R, C> R accept(Visitor<R, C> v, C context) {
        return v.visit(this, context);
    }

    @Override
    public NumberExpression<Integer> dayOfMonth() {
        return date.dayOfMonth();
    }

    @Override
    public NumberExpression<Integer> month() {
        return date.month();
    }

    @Override
    public NumberExpression<Integer> year() {
        return date.year();
    }

    @Override
    public NumberExpression<Integer> yearMonth() {
        return date.yearMonth();
    }

    @Override
    public NumberExpression<Integer> dayOfWeek() {
        return date.dayOfWeek();
    }

    @Override
    public NumberExpression<Integer> dayOfYear() {
        return date.dayOfYear();
    }

    @Override
    public NumberExpression<Integer> week() {
        return date.week();
    }

    @Override
    public NumberExpression<Integer> hour() {
        return time.hour();
    }

    @Override
    public NumberExpression<Integer> minute() {
        return time.minute();
    }

    @Override
    public NumberExpression<Integer> second() {
        return time.second();
    }

    @Override
    public NumberExpression<Integer> milliSecond() {
        return time.milliSecond();
    }

    @Override
    public D getConstant() {
        return (D) localDateTime;
    }

}
