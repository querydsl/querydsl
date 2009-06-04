/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import static com.mysema.query.collections.utils.QueryIteratorUtils.multiArgFilter;
import static com.mysema.query.collections.utils.QueryIteratorUtils.toArrayIterator;
import static com.mysema.query.collections.utils.QueryIteratorUtils.transform;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections15.IteratorUtils;
import org.apache.commons.collections15.Predicate;
import org.apache.commons.collections15.iterators.FilterIterator;
import org.apache.commons.collections15.iterators.IteratorChain;
import org.apache.commons.collections15.iterators.UniqueFilterIterator;

import com.mysema.query.JoinExpression;
import com.mysema.query.QueryMetadata;
import com.mysema.query.QueryModifiers;
import com.mysema.query.SearchResults;
import com.mysema.query.collections.eval.Evaluator;
import com.mysema.query.collections.iterators.FilteringMultiIterator;
import com.mysema.query.collections.iterators.LimitingIterator;
import com.mysema.query.collections.iterators.MultiIterator;
import com.mysema.query.collections.support.DefaultIndexSupport;
import com.mysema.query.collections.support.DefaultSourceSortingSupport;
import com.mysema.query.collections.support.MultiComparator;
import com.mysema.query.collections.support.SimpleIteratorSource;
import com.mysema.query.collections.utils.EvaluatorUtils;
import com.mysema.query.support.QueryBaseWithProjection;
import com.mysema.query.types.Order;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.expr.EArrayConstructor;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.operation.Operation;
import com.mysema.query.types.operation.Ops;


public class ColQueryImpl extends AbstractColQuery<ColQueryImpl>{

    public ColQueryImpl() {
        super();
    }

    public ColQueryImpl(ColQueryPatterns patterns) {
        super(patterns);
    }

    public ColQueryImpl(QueryMetadata<Object> metadata) {
        super(metadata);
    }

}