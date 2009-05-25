package com.mysema.query.jdoql;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.query.jdoql.testdomain.QBook;
import com.mysema.query.jdoql.testdomain.QProduct;
import com.mysema.query.types.expr.Expr;

public class JDOQLSerializerTest {

	private QBook book = QBook.book;
	
	private QProduct product = QProduct.product;
	
	@Test
	public void testHandle() {
		assertEquals("this.name == product.name", serialize(book.name.eq(product.name)));
		assertEquals("this == product", serialize(book.eq(product)));
	}

	@Test
	public void booleanTests() {
		// boolean			
		assertEquals("product.name == a1 && product.price <= a2", serialize(product.name.eq("Sony Discman").and(product.price.loe(300.00))));
		assertEquals("product.name == a1 || product.price <= a2", serialize(product.name.eq("Sony Discman").or(product.price.loe(300.00))));
		assertEquals("!(product.name == a1)", serialize(product.name.eq("Sony MP3 player").not()));
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
		assertEquals("product.price == a1", serialize(product.price.eq(200.00)));
		assertEquals("product.price != a1", serialize(product.price.ne(100.00)));
		assertEquals("product.price > a1", serialize(product.price.gt(100.00)));
		assertEquals("product.price < a1", serialize(product.price.lt(300.00)));
		assertEquals("product.price >= a1", serialize(product.price.goe(100.00)));
		assertEquals("product.price <= a1", serialize(product.price.loe(300.00)));
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
		assertEquals("product.name.startsWith(a1)", serialize(product.name.startsWith("Sony Discman")));
		assertEquals("product.name.endsWith(a1)", serialize(product.name.endsWith("Discman")));
//		FIXME assertEquals("like", 1, serialize(product.name.like("Sony %")));
		assertEquals("product.name.toLowerCase() == a1", serialize(product.name.lower().eq("sony discman")));
		assertEquals("product.name.toUpperCase() == a1", serialize(product.name.upper().eq("SONY DISCMAN")));
		assertEquals("product.name.indexOf(a1) == a2", serialize(product.name.indexOf("S").eq(0)));
		// TODO indexOf
		// TODO matches
		assertEquals("product.name.substring(a1,a2) == a3", serialize(product.name.substring(0,4).eq("Sony")));
		assertEquals("product.name.substring(a1) == a2", serialize(product.name.substring(5).eq("Discman")));
	}
	
	private String serialize(Expr<?> expr){
		return new JDOQLSerializer(JDOQLOps.DEFAULT, book).handle(expr).toString();
	}

}
