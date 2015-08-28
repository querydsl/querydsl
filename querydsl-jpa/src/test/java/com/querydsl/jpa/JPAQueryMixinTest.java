package com.querydsl.jpa;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

import com.querydsl.core.JoinExpression;
import com.querydsl.core.JoinType;
import com.querydsl.core.QueryMetadata;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.domain.QCat;
import com.querydsl.jpa.domain.QCompany;
import com.querydsl.jpa.domain.QDepartment;
import com.querydsl.jpa.domain.QEmployee;
import com.querydsl.jpa.domain4.QBookMark;
import com.querydsl.jpa.domain4.QBookVersion;

public class JPAQueryMixinTest {

    private JPAQueryMixin<?> mixin = new JPAQueryMixin<Object>();

    @Test
    public void Where_Null() {
        mixin.where((Predicate) null);
    }

    @Test
    public void OrderBy() {
        QCat cat = QCat.cat;
        QCat catMate = new QCat("cat_mate");
        mixin.from(cat);
        mixin.orderBy(cat.mate.name.asc());

        QueryMetadata md = mixin.getMetadata();
        assertEquals(Arrays.asList(
                new JoinExpression(JoinType.DEFAULT, cat),
                new JoinExpression(JoinType.LEFTJOIN, cat.mate.as(catMate))),
                md.getJoins());
        assertEquals(Arrays.asList(catMate.name.asc()),
                md.getOrderBy());
    }

    @Test
    public void OrderBy_NonRoot_Twice() {
        QDepartment department = QDepartment.department;
        QCompany departmentCompany = new QCompany("department_company");
        QEmployee departmentCompanyCeo = new QEmployee("department_company_ceo");
        mixin.from(department);
        mixin.orderBy(department.company.ceo.firstName.asc(), department.company.ceo.lastName.asc());

        QueryMetadata md = mixin.getMetadata();
        assertEquals(Arrays.asList(
                        new JoinExpression(JoinType.DEFAULT, department),
                        new JoinExpression(JoinType.LEFTJOIN, department.company.as(departmentCompany)),
                        new JoinExpression(JoinType.LEFTJOIN, departmentCompany.ceo.as(departmentCompanyCeo))),
                md.getJoins());
        assertEquals(Arrays.asList(departmentCompanyCeo.firstName.asc(), departmentCompanyCeo.lastName.asc()),
                md.getOrderBy());
    }

    @Test
    public void OrderBy_Where() {
        QCat cat = QCat.cat;
        mixin.from(cat);
        mixin.where(cat.mate.name.isNotNull());
        mixin.orderBy(cat.mate.name.asc());

        QueryMetadata md = mixin.getMetadata();
        assertEquals(Arrays.asList(new JoinExpression(JoinType.DEFAULT, cat)), md.getJoins());
        assertEquals(Arrays.asList(cat.mate.name.asc()), md.getOrderBy());
    }

    @Test
    public void OrderBy_GroupBy() {
        QCat cat = QCat.cat;
        mixin.from(cat);
        mixin.groupBy(cat.mate.name);
        mixin.orderBy(cat.mate.name.asc());

        QueryMetadata md = mixin.getMetadata();
        assertEquals(Arrays.asList(new JoinExpression(JoinType.DEFAULT, cat)), md.getJoins());
        assertEquals(Arrays.asList(cat.mate.name.asc()), md.getOrderBy());
    }

    @Test
    public void OrderBy_Operation() {
        QCat cat = QCat.cat;
        QCat catMate = new QCat("cat_mate");
        mixin.from(cat);
        mixin.orderBy(cat.mate.name.lower().asc());

        QueryMetadata md = mixin.getMetadata();
        assertEquals(Arrays.asList(
                        new JoinExpression(JoinType.DEFAULT, cat),
                        new JoinExpression(JoinType.LEFTJOIN, cat.mate.as(catMate))),
                md.getJoins());
        assertEquals(Arrays.asList(catMate.name.lower().asc()),
                md.getOrderBy());
    }

    @Test
    public void OrderBy_Long() {
        QCat cat = QCat.cat;
        QCat catMate = new QCat("cat_mate");
        QCat catMateMate = new QCat("cat_mate_mate");
        mixin.from(cat);
        mixin.orderBy(cat.mate.mate.name.asc());

        QueryMetadata md = mixin.getMetadata();
        assertEquals(Arrays.asList(
                new JoinExpression(JoinType.DEFAULT, cat),
                new JoinExpression(JoinType.LEFTJOIN, cat.mate.as(catMate)),
                new JoinExpression(JoinType.LEFTJOIN, catMate.mate.as(catMateMate))),
                md.getJoins());
        assertEquals(Arrays.asList(catMateMate.name.asc()),
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
        QCat mateMate = new QCat("mate_mate");
        mixin.from(cat);
        mixin.leftJoin(cat.mate, mate);
        mixin.orderBy(cat.mate.mate.name.asc());

        QueryMetadata md = mixin.getMetadata();
        assertEquals(Arrays.asList(
                new JoinExpression(JoinType.DEFAULT, cat),
                new JoinExpression(JoinType.LEFTJOIN, cat.mate.as(mate)),
                new JoinExpression(JoinType.LEFTJOIN, mate.mate.as(mateMate))),
                md.getJoins());
        assertEquals(Arrays.asList(mateMate.name.asc()),
                md.getOrderBy());
    }

    @Test
    public void OrderBy_Any() {
        QCat cat = QCat.cat;
        QCat catKittens = new QCat("cat_kittens");
        mixin.from(cat);
        mixin.orderBy(cat.kittens.any().name.asc());

        QueryMetadata md = mixin.getMetadata();
        assertEquals(Arrays.asList(
                new JoinExpression(JoinType.DEFAULT, cat),
                new JoinExpression(JoinType.LEFTJOIN, cat.kittens.as(catKittens))),
                md.getJoins());
        assertEquals(Arrays.asList(catKittens.name.asc()),
                md.getOrderBy());
    }

    @Test
    public void OrderBy_Embeddable() {
        QBookVersion bookVersion = QBookVersion.bookVersion;
        mixin.from(bookVersion);
        mixin.orderBy(bookVersion.definition.name.asc());

        QueryMetadata md = mixin.getMetadata();
        assertEquals(Arrays.asList(new JoinExpression(JoinType.DEFAULT, bookVersion)),
                md.getJoins());
        assertEquals(Arrays.asList(bookVersion.definition.name.asc()),
                md.getOrderBy());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void OrderBy_Embeddable2() {
        QArticle article = QArticle.article;
        QArticle articleContentArticle = new QArticle("article_content_article");
        mixin.from(article);
        mixin.orderBy(article.content.article.name.asc());

        QueryMetadata md = mixin.getMetadata();
        assertEquals(Arrays.asList(
                new JoinExpression(JoinType.DEFAULT, article),
                new JoinExpression(JoinType.LEFTJOIN, article.content.article.as(articleContentArticle))),
                md.getJoins());
        assertEquals(Arrays.asList(articleContentArticle.name.asc()),
                md.getOrderBy());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void OrderBy_Embeddable_Collection() {
        QBookVersion bookVersion = QBookVersion.bookVersion;
        QBookMark bookMark = new QBookMark("bookVersion_definition_bookMarks");
        mixin.from(bookVersion);
        mixin.orderBy(bookVersion.definition.bookMarks.any().comment.asc());

        QueryMetadata md = mixin.getMetadata();
        assertEquals(Arrays.asList(new JoinExpression(JoinType.DEFAULT, bookVersion)),
                md.getJoins());
        assertEquals(Arrays.asList(Expressions.stringPath(bookVersion.definition.bookMarks, "comment").asc()),
                md.getOrderBy());
    }
}
