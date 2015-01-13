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
package com.querydsl.jpa;

import com.querydsl.core.types.Operator;
import com.querydsl.core.types.OperatorImpl;

/**
 * @author tiwe
 *
 */
public final class JPQLOps {

    private static final String NS = JPQLOps.class.getName();

    public static final Operator<Object> TREAT = new OperatorImpl<Object>(NS, "TREAT");

    public static final Operator<Integer> INDEX = new OperatorImpl<Integer>(NS, "INDEX");

    public static final Operator<String> TYPE = new OperatorImpl<String>(NS, "TYPE");

    public static final Operator<Object> CAST = new OperatorImpl<Object>(NS, "CAST");

    public static final Operator<Boolean> MEMBER_OF = new OperatorImpl<Boolean>(NS, "MEMBER_OF");

    public static final Operator<Boolean> NOT_MEMBER_OF = new OperatorImpl<Boolean>(NS, "NOT_MEMBER_OF");

    public static final Operator<Object> KEY = new OperatorImpl<Object>(NS, "KEY");

    public static final Operator<Object> VALUE = new OperatorImpl<Object>(NS, "VALUE");

    private JPQLOps(){}
}
