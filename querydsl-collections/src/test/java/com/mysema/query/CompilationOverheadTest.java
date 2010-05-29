package com.mysema.query;

import java.util.Collections;

import org.junit.Test;

import com.mysema.query.animal.Cat;
import com.mysema.query.animal.QCat;
import com.mysema.query.collections.MiniApi;

public class CompilationOverheadTest {
    
    @Test
    public void test(){
        query();
    }
    
    @Test
    public void test2(){
        query();
    }
    
    private void query(){
        QCat cat = QCat.cat;
        MiniApi.from(cat, Collections.<Cat>emptyList())
            .where(cat.mate.isNotNull(), cat.mate.name.eq("Kitty"))
            .list(cat);
    }

}
