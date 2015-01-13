package com.querydsl.sql;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.querydsl.sql.Configuration;
import com.querydsl.sql.SQLSerializer;
import com.querydsl.core.types.expr.BooleanExpression;
import com.querydsl.core.types.path.StringPath;

public class PrecedenceTest {
    
    @Test
    public void test() {
        StringPath str1 = new StringPath("str1");
        StringPath str2 = new StringPath("str2");
        BooleanExpression pending = str1.eq("3").and(str2.eq("1"));
        BooleanExpression notNew = str1.ne("1").and(str2.in("2", "3"));       
        BooleanExpression whereClause = str1.eq("a").and(pending.or(notNew));
        String str = new SQLSerializer(Configuration.DEFAULT).handle(whereClause).toString();
        assertEquals("str1 = ? and (str1 = ? and str2 = ? or str1 != ? and str2 in (?, ?))", str);
    }
    
}
