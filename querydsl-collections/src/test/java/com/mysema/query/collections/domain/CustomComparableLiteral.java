package com.mysema.query.collections.domain;

import com.mysema.query.annotations.Literal;

@Literal
public class CustomComparableLiteral implements
        Comparable<CustomComparableLiteral> {

    @Override
    public int compareTo(CustomComparableLiteral o) {
        // TODO Auto-generated method stub
        return 0;
    }

}
