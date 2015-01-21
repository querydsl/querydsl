package com.querydsl.jpa;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.querydsl.core.types.Ops;
import com.querydsl.core.types.Templates;

public class JPQLTemplatesTest {

    @Test
    public void Escape() {
        List<Templates> templates = Arrays.<Templates>asList(
            new JPQLTemplates(), new HQLTemplates(),
            new EclipseLinkTemplates(), new OpenJPATemplates()
        );
        
        for (Templates t : templates) {
            assertEquals("{0} like {1} escape '!'", t.getTemplate(Ops.LIKE).toString());
        }
    }

    
    @Test
    public void Custom_Escape() {
        List<Templates> templates = Arrays.<Templates>asList(
            new JPQLTemplates('X'), new HQLTemplates('X'),
            new EclipseLinkTemplates('X'), new OpenJPATemplates('X')
        );
        
        for (Templates t : templates) {
            assertEquals("{0} like {1} escape 'X'", t.getTemplate(Ops.LIKE).toString());
        }
    }

}
