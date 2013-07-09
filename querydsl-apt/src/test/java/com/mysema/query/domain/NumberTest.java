package com.mysema.query.domain;

import org.junit.Ignore;

import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.domain.custom.CustomNumber;

@Ignore
public class NumberTest {

    @QueryEntity
    public static class Entity {

        CustomNumber customNumber;

    }
}
