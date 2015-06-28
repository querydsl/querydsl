package com.querydsl.example.dao;

import com.querydsl.core.dml.StoreClause;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.QBean;
import com.querydsl.example.dto.Product;
import com.querydsl.example.dto.ProductL10n;
import com.querydsl.example.dto.Supplier;
import com.querydsl.sql.SQLQueryFactory;
import com.querydsl.sql.dml.SQLInsertClause;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

import static com.querydsl.core.types.Projections.bean;
import static com.querydsl.example.sql.QProduct.product;
import static com.querydsl.example.sql.QProductL10n.productL10n;
import static com.querydsl.example.sql.QSupplier.supplier;

@Transactional
public class ProductDaoImpl implements ProductDao {

    @Inject
    SQLQueryFactory queryFactory;
    
    final QBean<ProductL10n> productL10nBean = bean(ProductL10n.class, 
            productL10n.description, productL10n.lang, productL10n.name);
    
    final QBean<Product> productBean = bean(Product.class, 
            product.id, product.name, product.otherProductDetails, product.price,
            bean(Supplier.class, supplier.all()).as("supplier"),
            GroupBy.set(productL10nBean).as("localizations"));
    
    @Override
    public Product findById(long id) {
        List<Product> persons = findAll(product.id.eq(id));
        return persons.isEmpty() ? null : persons.get(0);
    }    

    @Override
    public List<Product> findAll(Predicate... where) {
        return queryFactory.from(product)
            .innerJoin(product.supplierFk, supplier)
            .innerJoin(product._productFk, productL10n)
            .where(where)
            .transform(GroupBy.groupBy(product.id).list(productBean));
    }

    private <T extends StoreClause<T>> T populate(T dml, Product p) {
        return dml.set(product.name, p.getName())
            .set(product.otherProductDetails, p.getOtherProductDetails())
            .set(product.price, p.getPrice())
            .set(product.supplierId, p.getSupplier().getId());
    }
    
    @Override
    public Product save(Product p) {
        Long id = p.getId();
        
        if (id == null) {
            id = populate(queryFactory.insert(product), p)
                .executeWithKey(product.id);
            p.setId(id);
        } else {
            populate(queryFactory.update(product), p)
                .where(product.id.eq(id))
                .execute();
            
            // delete l10n rows
            queryFactory.delete(productL10n)
                .where(productL10n.productId.eq(id))
                .execute();
        }
        
        SQLInsertClause insert = queryFactory.insert(productL10n);
        for (ProductL10n l10n : p.getLocalizations()) {
            insert.set(productL10n.productId, id)
                .set(productL10n.description, l10n.getDescription())
                .set(productL10n.lang, l10n.getLang())
                .set(productL10n.name, l10n.getName())
                .addBatch();
        }
        insert.execute();
        
        return p;
    }

    @Override
    public long count() {
        return queryFactory.from(product).fetchCount();
    }    
    
    @Override
    public void delete(Product p) {
        // TODO use combined delete clause        
        queryFactory.delete(productL10n)
            .where(productL10n.productId.eq(p.getId()))
            .execute();
        
        queryFactory.delete(product)
            .where(product.id.eq(p.getId()))
            .execute();
    }
    
}
