package com.mysema.query.jpa;

import org.junit.Test;

import com.mysema.query.jpa.domain.QPersonSummaryInteger;
import com.mysema.query.jpa.domain.QPersonSummaryLong;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.expr.NumberExpression;

public class QueryProjectionTest extends AbstractQueryTest {

	@Test
	public void queryProjectionSize() {
		JPAQuery q = new JPAQuery();
		
		q.from(p).list(new QPersonSummaryInteger(p.name, p.children.size()));
	}

	@Test
	public void queryProjectionCount() {
		JPAQuery q = new JPAQuery();
		
		q.from(p).list(new QPersonSummaryLong(p.name, NumberExpression.count(p.children)));
	}
}
