package com.mysema.query.jdoql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.mysema.query.jdoql.testdomain.Product;
import com.mysema.query.jdoql.testdomain.QProduct;

public class QueryOrderingTest extends AbstractJDOTest{
	
	private QProduct product = QProduct.product;
	
	@Test
	public void testOrderAsc(){
		List<String> namesAsc = query().from(product)
			.orderBy(product.name.asc(), product.description.desc())
			.list(product.name);
		assertEquals(30, namesAsc.size());
		String prev = null;
		for (String name : namesAsc){
			if (prev != null){
				assertTrue(prev.compareTo(name) < 0);
			}
			prev = name;
		}
	}
	
	@Test
	public void testOrderDesc(){
		List<String> namesDesc = query().from(product)
			.orderBy(product.name.desc())
			.list(product.name);
		assertEquals(30, namesDesc.size());
		String prev = null;		
		for (String name : namesDesc){
			if (prev != null){
				assertTrue(prev.compareTo(name) > 0);
			}
			prev = name;
		}
	}
	
	@Test
	@Ignore
	public void searchResults(){
		// TODO
	}
	
	@Test
	@Ignore
	public void testDistinct(){
		// TODO
	}
	
	@BeforeClass
	public static void doPersist() {
		// Persistence of a Product and a Book.
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			for (int i=0; i < 10; i++){				
				pm.makePersistent(new Product("C"+i,"F"+i, 200.00));
				pm.makePersistent(new Product("B"+i,"E"+i, 200.00));
				pm.makePersistent(new Product("A"+i,"D"+i, 200.00));				
			}
			tx.commit();
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
		System.out.println("");

	}

}
