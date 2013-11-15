package com.mysema.query.group;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.google.common.base.Equivalence;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.mysema.commons.lang.CloseableIterator;
import com.mysema.commons.lang.IteratorAdapter;
import com.mysema.query.Projectable;
import com.mysema.query.ResultTransformer;
import com.mysema.query.Tuple;
import com.mysema.query.types.Expression;
import com.mysema.query.types.FactoryExpression;
import com.mysema.query.types.FactoryExpressionUtils;
import com.mysema.query.types.QTuple;

class GroupByResultTransformer<K, V> implements ResultTransformer<CloseableIterator<V>> {

    @SuppressWarnings("serial")
    private static class GEquivalence<T> extends AbstractGroupExpression<T, Boolean> {
        
        @SuppressWarnings({ "unchecked", "rawtypes" })
        private final Equivalence<T> equivalence = (Equivalence) Equivalence.equals();
        
        public GEquivalence(Expression<T> expr) {
            super(Boolean.class, expr);
        }

        @Override
        public GroupCollector<T, Boolean> createGroupCollector() {
            return new GroupCollector<T, Boolean>() {

                private Predicate<T> predicate;
                
                private Boolean equivalent;
                
                @Override
                public void add(T o) {
                    if (predicate == null) {
                        predicate = equivalence.equivalentTo(o);
                    } else if (equivalent == null || equivalent.booleanValue()) {
                        equivalent = predicate.apply(o);
                    }
                }

                @Override
                public Boolean get() {
                    return equivalent;
                }
              
            };
        }
        
    }
    
    private static class ExpressionGroupCollector<T, R> implements GroupCollector<Tuple, R> {
        
        protected final GroupExpression<T, R> groupExpression;
        protected final GroupCollector<T, R> groupCollector;
        
        public ExpressionGroupCollector(GroupExpression<T, R> groupExpression) { 
            this.groupExpression = groupExpression;
            this.groupCollector = groupExpression.createGroupCollector();
        }
        
        @Override
        public void add(Tuple tuple) {
            Expression<T> expression = groupExpression.getExpression();
            T input = tuple.get(expression);
            groupCollector.add(input);
        }

        @Override
        public R get() {
            return groupCollector.get();
        }

        public ExpressionGroupCollector<T, R> clone() {
            return new ExpressionGroupCollector<T, R>(groupExpression);
        }

    }

    private static class FactoryExpressionGroupCollector<T, R> extends ExpressionGroupCollector<T, R> {
        
        private final FactoryExpression<T> factoryExpression;

        public FactoryExpressionGroupCollector(GroupExpression<T, R> groupExpression) { 
            this(groupExpression, groupExpression.getExpression());
        }
        
        private FactoryExpressionGroupCollector(GroupExpression<T, R> groupExpression, Expression<T> expression) { 
            super(groupExpression);
            this.factoryExpression = expression instanceof FactoryExpression<?> ? FactoryExpressionUtils.wrap((FactoryExpression<T>) expression) : null;
        }
        
        @Override
        public void add(Tuple tuple) {
            List<Expression<?>> expressions = factoryExpression.getArgs();
            Object[] args = new Object[expressions.size()];
            for (int i = 0; i < args.length; i++) {
                Expression<?> expression = expressions.get(i);
                args[i] = tuple.get(expression);
            }
            T input = factoryExpression.newInstance(args);
            groupCollector.add(input);
        }

        @Override
        public FactoryExpressionGroupCollector<T, R> clone() {
            return new FactoryExpressionGroupCollector<T, R>(groupExpression, factoryExpression);
        }

    }
    
    private static class IteratorImpl<K, V> extends IteratorAdapter<V> {
        
        private final GroupExpression<K, Boolean> groupExpression;
        
        private final FactoryExpression<V> factoryExpression;
        
        private ExpressionGroupCollector<K, Boolean> groupCollector;
        
        private final List<ExpressionGroupCollector<?, ?>> groupCollectors;
        
        private Object[] args;
        
        @SuppressWarnings({ "unchecked", "rawtypes" })
        private IteratorImpl(Expression<K> keyExpression, FactoryExpression<V> valueExpression, List<Expression<?>> arguments, CloseableIterator<Tuple> iterator) {
            super((Iterator) iterator);
            this.groupExpression = new GEquivalence<K>(keyExpression);
            this.factoryExpression = valueExpression;
            groupCollectors = new ArrayList<ExpressionGroupCollector<?, ?>>(arguments.size());
            for (Expression<?> argument : arguments) {
                Expression<?> expression;
                GroupExpression<?, ?> groupExpression;
                if (argument instanceof GroupExpression<?,?>) {
                    groupExpression = (GroupExpression<?, ?>) argument;
                    expression = groupExpression.getExpression();
                } else {
                    groupExpression = new GOne(argument);
                    expression = argument;
                }
                ExpressionGroupCollector<?, ?> groupCollector;
                if (expression instanceof FactoryExpression<?>) {
                    groupCollector = new FactoryExpressionGroupCollector(groupExpression);
                } else {
                    groupCollector = new ExpressionGroupCollector(groupExpression);
                }
                groupCollectors.add(groupCollector);
            }
        } 
        
        public IteratorImpl(Expression<K> keyExpression, Expression<V> valueExpression, CloseableIterator<Tuple> iterator) {
            this(keyExpression, null, Collections.<Expression<?>>singletonList(valueExpression), iterator);
        }
        
        public IteratorImpl(Expression<K> keyExpression, FactoryExpression<V> valueExpression, CloseableIterator<Tuple> iterator) {
            this(keyExpression, valueExpression, valueExpression.getArgs(), iterator);
        } 

        @Override
        public boolean hasNext() {
            if (args == null) {
                if (super.hasNext()) {
                    if (groupCollector == null) {
                        Expression<?> expression = groupExpression.getExpression();
                        if (expression instanceof FactoryExpression<?>) {
                            this.groupCollector = new FactoryExpressionGroupCollector<K, Boolean>(groupExpression);
                        } else {
                            this.groupCollector = new ExpressionGroupCollector<K, Boolean>(groupExpression);
                        }
                        Tuple tuple = (Tuple) super.next();
                        groupCollector.add(tuple);
                        for (ExpressionGroupCollector<?, ?> groupCollector : groupCollectors) {
                            groupCollector.add(tuple);
                        }
                    }
                    while (args == null && super.hasNext()) {
                        Tuple tuple = (Tuple) super.next();
                        groupCollector.add(tuple);
                        boolean equivalent = groupCollector.get();
                        if (!equivalent) {
                            groupCollector = groupCollector.clone();
                            groupCollector.add(tuple);
                            args = new Object[groupCollectors.size()];
                            for (int i = 0; i < args.length; i++) {
                                ExpressionGroupCollector<?, ?> groupCollector = groupCollectors.get(i);
                                args[i] = groupCollector.get();
                                groupCollectors.set(i, groupCollector.clone());
                            }
                        }
                        for (ExpressionGroupCollector<?, ?> groupCollector : groupCollectors) {
                            groupCollector.add(tuple);
                        }
                    }
                } else if (groupCollector != null) {
                    groupCollector = null;
                    args = new Object[groupCollectors.size()];
                    for (int i = 0; i < args.length; i++) {
                        ExpressionGroupCollector<?, ?> groupCollector = groupCollectors.get(i);
                        args[i] = groupCollector.get();
                    }
                    groupCollectors.clear();
                }
            }
            return args != null;
        }

        @Override
        public V next() {
            if (!this.hasNext()) {
                throw new IllegalStateException();
            }
            @SuppressWarnings("unchecked")
            V value = factoryExpression == null ? (V) args[0] : factoryExpression.newInstance(args);
            args = null;
            return value;
        }

    }
    
    private final Expression<K> key;
    private final Expression<V> value;
    private final FactoryExpression<Tuple> factoryExpression;
    
    private static Expression<?> expand(Expression<?> expression) {
        while (expression instanceof GroupExpression<?, ?>) {
            GroupExpression<?, ?> groupExpression = (GroupExpression<?, ?>) expression;
            expression = groupExpression.getExpression(); 
        }
        return expression;
    }
    
    public GroupByResultTransformer(Expression<K> key, FactoryExpression<V> value) {
        this.key = key;
        this.value = value;
        ImmutableList.Builder<Expression<?>> builder = ImmutableList.builder();
        builder.add(expand(key)); 
        value = FactoryExpressionUtils.wrap(value);
        List<Expression<?>> arguments = value.getArgs();
        for (Expression<?> argument : arguments) {
            builder.add(expand(argument));
        }
        this.factoryExpression = FactoryExpressionUtils.wrap(new QTuple(builder.build()));
    }
    
    public GroupByResultTransformer(Expression<K> key, Expression<V> value) {
        this.key = key;
        this.value = value;
        this.factoryExpression = FactoryExpressionUtils.wrap(new QTuple(expand(key), expand(value)));
    }

    @Override
    public CloseableIterator<V> transform(Projectable projectable) { 
        CloseableIterator<Tuple> iterator = projectable.iterate(factoryExpression);
        return value instanceof FactoryExpression<?> ? new IteratorImpl<K, V>(key, (FactoryExpression<V>) value, iterator) : new IteratorImpl<K, V>(key, value, iterator);
    }

}