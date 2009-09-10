package com.mysema.query.jdoql.testdomain;

import com.mysema.query.types.path.*;

/**
 * QStore is a Querydsl query type for Store
 *
 */
public class QStore extends PEntity<com.mysema.query.jdoql.testdomain.Store>{
    
    public static final QStore store = new QStore("store");
    
    public final PString name = createString("name");
    
    public final PEntityMap<String,Product> productsByName = createEntityMap("productsByName",String.class,Product.class,"Product");
    
    public final PEntityCollection<Product> products = createEntityCollection("products",Product.class, "Product");
    
    public QProduct productsByName(String key) {
        return new QProduct(PathMetadata.forMapAccess(productsByName,key));
    }
    
    public QProduct productsByName(com.mysema.query.types.expr.Expr<String> key) {
        return new QProduct(PathMetadata.forMapAccess(productsByName,key));
    }
    
    public QStore(String path) {
          this(Store.class, path);        
    }
    public QStore(Class<? extends Store> cl, String path) {
          super(cl, "Store", path);
    }    
    public QStore(PathMetadata<?> metadata) {
         super(Store.class, "Store", metadata);
    }
}
