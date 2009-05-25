package com.mysema.query.jdoql;

import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import javax.jdo.Transaction;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import com.mysema.query.jdoql.testdomain.Book;
import com.mysema.query.jdoql.testdomain.Product;
import com.mysema.query.jdoql.testdomain.QBook;
import com.mysema.query.jdoql.testdomain.QProduct;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.path.PEntity;

public abstract class AbstractJDOTest {

	protected static PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory("datanucleus.properties");
	
	protected QBook book = QBook.book;
	
	protected PersistenceManager pm;
	
	protected QProduct product = QProduct.product;
	
	protected Transaction tx;
	
	protected JDOQLQuery query(){
		return new JDOQLQueryImpl(pm);
	}
	
	protected <T> List<T> query(PEntity<T> source, EBoolean condition){
		return new JDOQLQueryImpl(pm).from(source).where(condition).list(source);
	}
	
	@Before
	public void setUp(){
		pm = pmf.getPersistenceManager();
		tx = pm.currentTransaction();
		tx.begin();
	}

	@After
	public void tearDown(){
		if (tx.isActive()) {
			tx.rollback();
		}
		pm.close();
	}
	
	@AfterClass
	public static void doCleanUp() {
		// Clean out the database
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
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

	@BeforeClass
	public static void doPersist() {
		// Persistence of a Product and a Book.
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
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
