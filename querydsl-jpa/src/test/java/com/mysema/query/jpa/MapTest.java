package com.mysema.query.jpa;

import org.junit.Test;

import com.mysema.query.jpa.domain.QShow;

public class MapTest  extends AbstractQueryTest{

    private QShow show = QShow.show;
    
    @Test
    public void Contains(){
        assertToString("show.acts[:a1] = :a2", show.acts.contains("x", "y"));
    }
    
    @Test
    public void Contains_Key(){
        assertToString(":a1 in indices(show.acts)", show.acts.containsKey("x"));
    }
    
    @Test
    public void Contains_Value(){
        assertToString(":a1 in elements(show.acts)", show.acts.containsValue("y"));
    }
}
