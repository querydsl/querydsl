package com.mysema.query.lucene;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.QueryMetadata;
import com.mysema.query.types.path.StringPath;

public class PhraseElementTest {

    @Test
    public void test(){
        StringPath title = new StringPath("title");
        LuceneSerializer serializer = new LuceneSerializer(false,false);
        QueryMetadata metadata = new DefaultQueryMetadata();
        assertEquals("title:Hello World", serializer.toQuery(title.eq("Hello World"), metadata).toString());
        assertEquals("title:\"Hello World\"", serializer.toQuery(title.eq(new PhraseElement("Hello World")), metadata).toString());
    }

    @Test
    public void testEqualsAndHashCode(){
        PhraseElement el1 = new PhraseElement("x"), el2 = new PhraseElement("x"), el3 = new PhraseElement("y");
        assertEquals(el1, el2);
        assertFalse(el1.equals(el3));
        assertEquals(el1.hashCode(), el2.hashCode());
    }

}
