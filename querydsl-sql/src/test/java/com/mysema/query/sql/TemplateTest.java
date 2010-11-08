package com.mysema.query.sql;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;

import com.mysema.query.types.ConstantImpl;
import com.mysema.query.types.Expression;
import com.mysema.query.types.expr.DateExpression;
import com.mysema.query.types.expr.StringExpression;
import com.mysema.query.types.path.DatePath;
import com.mysema.query.types.path.StringPath;
import com.mysema.query.types.template.DateTemplate;
import com.mysema.query.types.template.StringTemplate;

public class TemplateTest {

    @Test
    public void ToDate(){
        StringExpression str = new StringPath("str");
        assertEquals("to_date(str,'DD-MON-YYYY')", to_date(str, "DD-MON-YYYY").toString());
    }

    @Test
    public void ToChar(){
        DateExpression<Date> date = new DatePath<Date>(Date.class,"date");
        assertEquals("to_char(date,'DD-MON-YYYY')", to_char(date, "DD-MON-YYYY").toString());
    }

    private DateExpression<Date> to_date(Expression<String> expr, String pattern){
        return DateTemplate.create(Date.class, "to_date({0},'{1s}')", expr, ConstantImpl.create(pattern));
    }

    private StringExpression to_char(Expression<?> expr, String pattern){
        return StringTemplate.create("to_char({0},'{1s}')", expr, ConstantImpl.create(pattern));
    }
        
}
