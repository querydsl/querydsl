package com.querydsl.collections;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.querydsl.core.types.Predicate;

public class DocumentTest {

    private Document doc1, doc2, doc3;

    private QDocument qDoc = QDocument.document;

    @Before
    public void setUp() {
        doc1 = new Document();
        doc1.setId(1L);
        doc1.getMeshThesaurusTerms().add("x");

        doc2 = new Document();
        doc2.setId(2L);

        doc3 = new Document();
        doc3.setId(3L);
    }

    @Test
    public void test1() {
        Predicate crit = qDoc.id.eq(3L);
        List<Document> expResult = CollQueryFactory.from(qDoc, doc1, doc2, doc3).where(crit).list(qDoc);
        assertTrue(expResult.contains(doc3)); //ok
    }

    @Test
    public void test2() {
        Predicate crit = qDoc.meshThesaurusTerms.any().eq("x");
        List<Document> expResult = CollQueryFactory.from(qDoc, doc1, doc2, doc3).where(crit).list(qDoc);
        assertTrue(expResult.contains(doc1)); //ok
    }

    @Test
    public void test3() {
        Predicate crit = qDoc.meshThesaurusTerms.any().eq("x").or(qDoc.id.eq(3L));
        List<Document> expResult =  CollQueryFactory.from(qDoc, doc1, doc2, doc3).where(crit).list(qDoc);
        assertTrue(expResult.contains(doc1));
        assertTrue(expResult.contains(doc3)); //fails, expResult contains only doc1, but should contain doc1 and doc3!
    }

    @Test
    public void test4() {
        Predicate crit = qDoc.id.eq(3L).or(qDoc.meshThesaurusTerms.any().eq("x"));
        List<Document> expResult =  CollQueryFactory.from(qDoc, doc1, doc2, doc3).where(crit).list(qDoc);
        assertTrue(expResult.contains(doc1));
        assertTrue(expResult.contains(doc3)); //fails, expResult contains only doc1, but should contain doc1 and doc3!
    }

}
