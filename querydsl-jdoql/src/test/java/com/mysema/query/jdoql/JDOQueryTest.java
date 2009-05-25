package com.mysema.query.jdoql;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;

import com.mysema.query.jdoql.testdomain.Book;

public class JDOQueryTest extends AbstractJDOTest{

	@Test
	@Ignore
	public void countTests(){
		// FIXME
		assertEquals("count", 2, query().from(product).count()); // returns 1, why?	
	}
	
	@Test
	@Ignore
	public void searchResults(){
		// TODO
	}
		
	@Test
	@Ignore
	public void testOrder(){
		// TODO
	}
		
	@Test
	public void projectionTests(){
		assertEquals("Sony Discman", query().from(product)
				.where(product.name.eq("Sony Discman"))
				.uniqueResult(product.name));
	}
	
	@Test
	public void basicTests() {	
		assertEquals("list", 2, query().from(product).list(product).size());						
		assertEquals("list", 1, query().from(book).list(book).size());
		assertEquals("eq", 1, query(product, product.name.eq("Sony Discman")).size());
		assertEquals("instanceof ", 1, query(product, product.instanceOf(Book.class)).size());
	}
	
	@Test
	public void booleanTests() {
		// boolean			
		assertEquals("and", 1, query(product, product.name.eq("Sony Discman").and(product.price.loe(300.00))).size());
		assertEquals("or", 2, query(product, product.name.eq("Sony Discman").or(product.price.loe(300.00))).size());
		assertEquals("not", 2, query(product, product.name.eq("Sony MP3 player").not()).size());
	}

	@Test
	public void collectionTests(){
		// collection
		// TODO contains
		// TODO get
		// TODO containsKey
		// TODO containsValue
		// TODO isEmpty
		// TODO size		
	}

	@Test
	public void numericTests() {
		// numeric
		assertEquals("eq", 1, query(product, product.price.eq(200.00)).size());
		assertEquals("eq", 0, query(product, product.price.eq(100.00)).size());
		assertEquals("ne", 2, query(product, product.price.ne(100.00)).size());
		assertEquals("gt", 1, query(product, product.price.gt(100.00)).size());
		assertEquals("lt", 2, query(product, product.price.lt(300.00)).size());
		assertEquals("goe", 1, query(product, product.price.goe(100.00)).size());
		assertEquals("loe", 2, query(product, product.price.loe(300.00)).size());
		// TODO +
		// TODO -
		// TODO *
		// TODO /
		// TODO %
		// TODO Math.abs
		// TODO Math.sqrt
	}

	@Test
	public void stringTests() {
		// string
		assertEquals("startsWith", 1, query(product, product.name.startsWith("Sony Discman")).size());
		assertEquals("endsWith", 1, query(product, product.name.endsWith("Discman")).size());
//		FIXME assertEquals("like", 1, query(product, product.name.like("Sony %")).size());
		assertEquals("toLowerCase", 1, query(product, product.name.lower().eq("sony discman")).size());
		assertEquals("toUpperCase", 1, query(product, product.name.upper().eq("SONY DISCMAN")).size());
		assertEquals("indexOf", 1, query(product, product.name.indexOf("S").eq(0)).size());
		// TODO matches
		assertEquals("substring", 1, query(product, product.name.substring(0,4).eq("Sony")).size());
		assertEquals("substring", 1, query(product, product.name.substring(5).eq("Discman")).size());
	}
	
}
