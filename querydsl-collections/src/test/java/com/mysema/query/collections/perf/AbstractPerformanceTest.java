package com.mysema.query.collections.perf;


import java.util.Arrays;
import java.util.List;

import com.mysema.query.collections.impl.AbstractQueryTest;
import com.mysema.query.types.expr.EBoolean;

public abstract class AbstractPerformanceTest extends AbstractQueryTest{
    
    protected List<EBoolean> conditionsFor1Source = Arrays.asList(
            cat.name.eq("Kate5"), cat.name.eq("Kate5").not(), 
            cat.name.matches("Kate5.*"),
            cat.bodyWeight.eq(0),

            // and
            cat.bodyWeight.eq(0).and(cat.name.eq("Kate5")), 
            cat.bodyWeight.eq(0).not().and(cat.name.eq("Kate5")), 
            cat.bodyWeight.eq(0).and(cat.name.eq("Kate5").not()),

            // or
            cat.bodyWeight.eq(0).or(cat.name.eq("Kate5")), 
            cat.bodyWeight.eq(0).not().or(cat.name.eq("Kate5")), 
            cat.bodyWeight.eq(0).or(cat.name.eq("Kate5").not()));

    protected List<EBoolean> conditionsFor2Sources = Arrays.asList(
            cat.ne(otherCat),
            cat.eq(otherCat),
            cat.name.eq(otherCat.name),

            // and
            cat.name.eq(otherCat.name).and(otherCat.name.eq("Kate5")), 
            cat.name.eq(otherCat.name).not().and(otherCat.name.eq("Kate5")),
            cat.name.eq(otherCat.name).and(otherCat.name.eq("Kate5").not()),
            cat.name.ne(otherCat.name).and(otherCat.name.eq("Kate5")),
            cat.name.ne(otherCat.name).and(otherCat.name.matches("Kate5.*")),
            cat.bodyWeight.eq(0).and(otherCat.name.eq("Kate5")),
            cat.name.matches("Bob5%").and(otherCat.name.matches("Kate5.*")),

            // or
            cat.name.eq(otherCat.name).or(otherCat.name.eq("Kate5")), 
            cat.name.eq(otherCat.name).or(otherCat.name.eq("Kate5").not()),
            cat.name.ne(otherCat.name).or(otherCat.name.eq("Kate5")), 
            cat.name.ne(otherCat.name).or(otherCat.name.matches(".*ate5")),
            cat.bodyWeight.eq(0).or(otherCat.name.eq("Kate5")), 
            cat.bodyWeight.eq(0).or(cat.name.eq("Kate5")), 
            cat.bodyWeight.gt(0).and(otherCat.name.eq("Bob5")).or(cat.name.eq("Kate5")),
            cat.bodyWeight.gt(0).and(otherCat.name.eq(cat.name)).or(
                    cat.name.eq("Kate5")), 
            cat.bodyWeight.gt(0).or(
                    otherCat.name.eq(cat.name)), 
                    cat.bodyWeight.eq(0).not().or(
                    otherCat.name.eq("Kate5")), cat.bodyWeight.eq(0).or(
                    otherCat.name.eq("Kate5").not()), cat.name.matches("Bob5.*")
                    .or(otherCat.name.matches(".*ate5")));

}
