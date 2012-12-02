package com.mysema.query.jpa;

import org.junit.Test;

import com.mysema.query.jpa.domain.QPersonSummary;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.ExpressionUtils;

public class QueryProjectiontTest extends AbstractQueryTest {

	@Test
	public void queryProjection() {
		JPAQuery q = new JPAQuery();
		
		q.from(p).list(new QPersonSummary(p.name, ExpressionUtils.count(p.children)));
	}
}
