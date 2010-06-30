package com.mysema.query.collections;

import static com.mysema.query.alias.Alias.$;
import static com.mysema.query.alias.Alias.alias;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class IterationTest {
    
    public static class Data {
        
        private String data = "data";

        public String getData() {
            return data;
        }
        
    }
    
    private List<Data> allData = Arrays.asList(new Data(), new Data());
    
    private Data lt = alias(Data.class,"Data");
    
    private List<String> expected = Arrays.asList("data","data");
    
    @Test
    public void test(){                
        assertEquals(expected, MiniApi.from ($(lt), allData).list($(lt.getData())));
    }

    @Test
    public void test2(){        
        assertEquals(expected, MiniApi.from ($(lt), Arrays.asList(allData.toArray())).list($(lt.getData())));
    }
    
    @Test
    public void test3(){                
        assertEquals(expected, MiniApi.from (lt, allData).list($(lt.getData())));
    }

    @Test
    public void test4(){        
        assertEquals(expected, MiniApi.from (lt, Arrays.asList(allData.toArray())).list($(lt.getData())));
    }
}
