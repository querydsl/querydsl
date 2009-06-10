package com.mysema.query.hql;

import org.junit.Test;

public class ComparableTest extends AbstractQueryTest{
    
    @Test
    public void testBinaryComparisonOperations() {
        // binary comparison operators =, >=, <=, <>, !=, like
        toString("cat.bodyWeight = kitten.bodyWeight", cat.bodyWeight
                .eq(kitten.bodyWeight));
        toString("cat.bodyWeight >= kitten.bodyWeight", cat.bodyWeight
                .goe(kitten.bodyWeight));
        toString("cat.bodyWeight > kitten.bodyWeight", cat.bodyWeight
                .gt(kitten.bodyWeight));
        toString("cat.bodyWeight <= kitten.bodyWeight", cat.bodyWeight
                .loe(kitten.bodyWeight));
        toString("cat.bodyWeight < kitten.bodyWeight", cat.bodyWeight
                .lt(kitten.bodyWeight));
        toString("cat.bodyWeight != kitten.bodyWeight", cat.bodyWeight
                .ne(kitten.bodyWeight));
//        toString("cat.name like :a1", cat.name.like("Kitty"));
    }

}
