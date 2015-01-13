package com.querydsl.apt.domain;

import org.junit.Ignore;

import com.querydsl.apt.domain.custom.CustomNumber;
import com.querydsl.core.annotations.QueryEntity;

@Ignore
public class NumberTest {

    @QueryEntity
    public static class Entity {

        CustomNumber customNumber;

    }
}
