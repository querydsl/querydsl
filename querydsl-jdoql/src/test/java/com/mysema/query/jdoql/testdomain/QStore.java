package com.mysema.query.jdoql.testdomain;

import com.mysema.query.types.path.*;

/**
 * QStore is a Querydsl query type for Store
 *
 */
public class QStore extends PEntity<com.mysema.query.jdoql.testdomain.Store>{
    public static final QStore store = new QStore("store");
    public final PString name = _string("name");
    public final PEntityMap<java.lang.String,com.mysema.query.jdoql.testdomain.Product> productsByName = _entitymap("productsByName",java.lang.String.class,com.mysema.query.jdoql.testdomain.Product.class,"Product");
    public com.mysema.query.jdoql.testdomain.QProduct productsByName(java.lang.String key) {
        return new com.mysema.query.jdoql.testdomain.QProduct(PathMetadata.forMapAccess(productsByName,key));
    }
    public com.mysema.query.jdoql.testdomain.QProduct productsByName(com.mysema.query.types.expr.Expr<java.lang.String> key) {
        return new com.mysema.query.jdoql.testdomain.QProduct(PathMetadata.forMapAccess(productsByName,key));
    }
    public final PEntityCollection<com.mysema.query.jdoql.testdomain.Product> products = _entitycol("products",com.mysema.query.jdoql.testdomain.Product.class, "Product");
    
    public QStore(java.lang.String path) {
          this(com.mysema.query.jdoql.testdomain.Store.class, path);        
    }
    public QStore(Class<? extends com.mysema.query.jdoql.testdomain.Store> cl, java.lang.String path) {
          super(cl, "Store", path);
    }    
    public QStore(PathMetadata<?> metadata) {
         super(com.mysema.query.jdoql.testdomain.Store.class, "Store", metadata);
    }
}
