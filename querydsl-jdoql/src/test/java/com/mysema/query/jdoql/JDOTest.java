package com.mysema.query.jdoql;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import javax.jdo.Transaction;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mysema.query.jdoql.testdomain.Book;
import com.mysema.query.jdoql.testdomain.Product;
import com.mysema.query.jdoql.testdomain.QBook;
import com.mysema.query.jdoql.testdomain.QProduct;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.path.PEntity;

public class JDOTest {

	private PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory("datanucleus.properties");

	private PersistenceManager pm;

	private Transaction tx;
	
	private QBook book = new QBook("this");
	
	private QProduct product = new QProduct("this");

	@Test
	public void testQuerydslQueries(){
		// Perform some query operations
		pm = pmf.getPersistenceManager();
		tx = pm.currentTransaction();
		try {
			tx.begin();			
			// START
			
			assertEquals("list", 2, query().from(product).list(product).size());						
			assertEquals("list", 1, query().from(book).list(book).size());
			assertEquals("eq", 1, query(product, product.name.eq("Sony Discman")).size());
//			FIXME assertEquals("instanceof ", 1, query(product, product.typeOf(Book.class)).size());
			
			// numeric
			assertEquals("eq", 1, query(product, product.price.eq(200.00)).size());
			assertEquals("eq", 0, query(product, product.price.eq(100.00)).size());
			assertEquals("ne", 2, query(product, product.price.ne(100.00)).size());
			assertEquals("gt", 1, query(product, product.price.gt(100.00)).size());
			assertEquals("lt", 2, query(product, product.price.lt(300.00)).size());
			assertEquals("goe", 1, query(product, product.price.goe(100.00)).size());
			assertEquals("loe", 2, query(product, product.price.loe(300.00)).size());
			// +
			// -
			// *
			// /
			// %
			// Math.abs
			// Math.sqrt
			
			// boolean			
			assertEquals("and", 1, query(product, product.name.eq("Sony Discman").and(product.price.loe(300.00))).size());
			assertEquals("or", 2, query(product, product.name.eq("Sony Discman").or(product.price.loe(300.00))).size());
			assertEquals("not", 2, query(product, product.name.eq("Sony MP3 player").not()).size());
			
			// string
			assertEquals("startsWith", 1, query(product, product.name.startsWith("Sony Discman")).size());
			assertEquals("endsWith", 1, query(product, product.name.endsWith("Discman")).size());
//			FIXME assertEquals("like", 1, query(product, product.name.like("Sony %")).size());
			// toLowerCase
			// toUpperCase
			// indexOf
			// matches
			// substring
			
			
			// TODO : continue on page 168
			
			// END
			
			tx.commit();
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
		System.out.println("");
	}
	
	private JDOQLQuery query(){
		return new JDOQLQueryImpl(pm);
	}
	
	private <T> List<T> query(PEntity<T> source, EBoolean condition){
		return new JDOQLQueryImpl(pm).from(source).where(condition).list(source);
	}
	

	@After
	public void doCleanUp() {
		// Clean out the database
		pm = pmf.getPersistenceManager();
		tx = pm.currentTransaction();
		try {
			tx.begin();
			System.out.println("Deleting all products from persistence");
			Query q = pm.newQuery(Product.class);
			long numberInstancesDeleted = q.deletePersistentAll();
			System.out.println("Deleted " + numberInstancesDeleted+ " products");

			tx.commit();
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}

	@Before
	public void doPersist() {
		// Persistence of a Product and a Book.
		pm = pmf.getPersistenceManager();
		tx = pm.currentTransaction();
		try {
			tx.begin();
			System.out.println("Persisting products");
			pm.makePersistent(new Product(
					"Sony Discman",
					"A standard discman from Sony", 
					200.00));
			pm.makePersistent(new Book(
					"Lord of the Rings by Tolkien",
					"The classic story", 
					49.99, 
					"JRR Tolkien", 
					"12345678",
					"MyBooks Factory"));
			tx.commit();
			System.out.println("Product and Book have been persisted");
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
		System.out.println("");

	}
}
