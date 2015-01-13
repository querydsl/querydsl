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
package com.querydsl.apt.inheritance;

import org.junit.Ignore;

import com.querydsl.core.annotations.QueryEntity;

@Ignore
public class Inheritance2Test {

    @QueryEntity
    public abstract class Base<T extends Base<T>> {
        @SuppressWarnings("unchecked")
        Base2 base;
        Base2<?,?> base2;
    }

    @QueryEntity
    public abstract class Base2<T extends Base2<T,U>,U extends IFace> {

    }

    @QueryEntity
    public abstract class BaseSub extends Base<BaseSub> {

    }

    @QueryEntity
    public abstract class BaseSub2<T extends BaseSub2<T>> extends Base<T> {

    }

    @QueryEntity
    public abstract class Base2Sub<T extends IFace> extends Base2<Base2Sub<T>,T> {

    }

    public interface IFace {

    }

}
