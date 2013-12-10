package com.mysema.query.jpa;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

import com.mysema.query.JoinExpression;
import com.mysema.query.JoinType;
import com.mysema.query.QueryMetadata;
import com.mysema.query.jpa.domain.QCat;
import com.mysema.query.types.PathMetadataFactory;
import com.mysema.query.types.Predicate;

public class JPAQueryMixinTest {

    private JPAQueryMixin mixin = new JPAQueryMixin();

    @Test
    public void Where_Null() {
        mixin.where((Predicate)null);
    }

    @Test
    public void OrderBy() {
        QCat cat = QCat.cat;
        QCat cat_mate = new QCat("cat_mate");
        mixin.from(cat);
        mixin.orderBy(cat.mate.name.asc());

        QueryMetadata md = mixin.getMetadata();
        assertEquals(Arrays.asList(
                new JoinExpression(JoinType.DEFAULT, cat),
                new JoinExpression(JoinType.LEFTJOIN, cat.mate.as(cat_mate))),
                md.getJoins());
        assertEquals(Arrays.asList(cat_mate.name.asc()),
                md.getOrderBy());
    }

    @Test
    public void OrderBy_Long() {
        QCat cat = QCat.cat;
        QCat catMate = new QCat(PathMetadataFactory.forProperty(cat, "mate"));
        QCat cat_mate = new QCat("cat_mate");
        QCat cat_mate_mate = new QCat("cat_mate_mate");
        mixin.from(cat);
        mixin.orderBy(cat.mate.mate.name.asc());

        QueryMetadata md = mixin.getMetadata();
        assertEquals(Arrays.asList(
                new JoinExpression(JoinType.DEFAULT, cat),
                new JoinExpression(JoinType.LEFTJOIN, cat.mate.as(cat_mate)),
                new JoinExpression(JoinType.LEFTJOIN, cat_mate.mate.as(cat_mate_mate))),
                md.getJoins());
        assertEquals(Arrays.asList(cat_mate_mate.name.asc()),
                md.getOrderBy());
    }

    @Test
    public void OrderBy_Reuse() {
        QCat cat = QCat.cat;
        QCat mate = new QCat("mate");
        mixin.from(cat);
        mixin.leftJoin(cat.mate, mate);
        mixin.orderBy(cat.mate.name.asc());

        QueryMetadata md = mixin.getMetadata();
        assertEquals(Arrays.asList(
                new JoinExpression(JoinType.DEFAULT, cat),
                new JoinExpression(JoinType.LEFTJOIN, cat.mate.as(mate))),
                md.getJoins());
        assertEquals(Arrays.asList(mate.name.asc()),
                md.getOrderBy());
    }

    @Test
    public void OrderBy_Long_Reuse() {
        QCat cat = QCat.cat;
        QCat mate = new QCat("mate");
        QCat mate_mate = new QCat("mate_mate");
        mixin.from(cat);
        mixin.leftJoin(cat.mate, mate);
        mixin.orderBy(cat.mate.mate.name.asc());

        QueryMetadata md = mixin.getMetadata();
        assertEquals(Arrays.asList(
                new JoinExpression(JoinType.DEFAULT, cat),
                new JoinExpression(JoinType.LEFTJOIN, cat.mate.as(mate)),
                new JoinExpression(JoinType.LEFTJOIN, mate.mate.as(mate_mate))),
                md.getJoins());
        assertEquals(Arrays.asList(mate_mate.name.asc()),
                md.getOrderBy());
    }

    // TODO test path.any() behaviour
}
