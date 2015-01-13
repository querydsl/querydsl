/*
 * Copyright 2014, Mysema Ltd
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
package com.querydsl.core.group;

/**
 * @param <E>
 * @param <F>
 * @param <R>
 */
public class MixinGroupExpression<E, F, R> extends AbstractGroupExpression<E, R> {

    private static final long serialVersionUID = -5419707469727395643L;

    private class GroupCollectorImpl implements GroupCollector<E, R> {

        private final GroupCollector<F, R> mixinGroupCollector;

        private GroupCollector<E, F> groupCollector;

        public GroupCollectorImpl() {
            mixinGroupCollector = mixin.createGroupCollector();
        }

        @Override
        public void add(E input) {
            if (groupCollector == null) {
                groupCollector = groupExpression.createGroupCollector();
            }
            groupCollector.add(input);
        }


        @Override
        public R get() {
            if (groupCollector != null) {
                F output = groupCollector.get();
                mixinGroupCollector.add(output);
                groupCollector = null;
            }
            return mixinGroupCollector.get();
        }

    }

    private final GroupExpression<F, R> mixin;

    private final GroupExpression<E, F> groupExpression;

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public MixinGroupExpression(GroupExpression<E, F> groupExpression, GroupExpression<F, R> mixin) {
        super((Class) mixin.getType(), groupExpression.getExpression());
        this.mixin = mixin;
        this.groupExpression = groupExpression;
    }

    @Override
    public GroupCollector<E, R> createGroupCollector() {
        return new GroupCollectorImpl();
    }

}
