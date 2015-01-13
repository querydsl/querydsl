package com.querydsl.apt.domain;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.querydsl.apt.domain.QRooEntities_MyEntity;
import com.querydsl.apt.domain.QRooEntities_MyEntity2;

public class RooEntitiesTest {

    @Test
    public void RooJpaEntity() {
        assertNotNull(QRooEntities_MyEntity.myEntity);
    }

    @Test
    public void RooJpaActiveRecord() {
        assertNotNull(QRooEntities_MyEntity2.myEntity2);
    }

}
