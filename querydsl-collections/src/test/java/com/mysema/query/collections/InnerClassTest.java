package com.mysema.query.collections;

import static com.mysema.query.alias.Alias.$;
import static com.mysema.query.alias.Alias.alias;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

public class InnerClassTest {

    public static class Example {

        public String getId(){
            return null;
        }
    }

    @Test
    public void Query(){
        Example example = alias(Example.class);
        assertFalse(MiniApi.from($(example), Arrays.asList(new Example()))
                .where($(example.getId()).isNull())
                .list($(example)).isEmpty());
        assertTrue(MiniApi.from($(example), Arrays.asList(new Example()))
                .where($(example.getId()).isNotNull())
                .list($(example)).isEmpty());
    }

}
