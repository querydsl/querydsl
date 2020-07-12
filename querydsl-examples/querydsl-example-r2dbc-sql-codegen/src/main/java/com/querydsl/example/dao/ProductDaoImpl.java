package com.querydsl.example.dao;

import com.querydsl.core.dml.ReactiveStoreClause;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.QBean;
import com.querydsl.example.dto.Product;
import com.querydsl.example.dto.ProductL10n;
import com.querydsl.example.dto.Supplier;
import com.querydsl.r2dbc.R2DBCQuery;
import com.querydsl.r2dbc.R2DBCQueryFactory;
import com.querydsl.r2dbc.dml.R2DBCInsertClause;
import com.querydsl.r2dbc.group.ReactiveGroupBy;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.inject.Inject;

import static com.querydsl.core.types.Projections.bean;
import static com.querydsl.example.r2dbc.QProduct.product;
import static com.querydsl.example.r2dbc.QProductL10n.productL10n;
import static com.querydsl.example.r2dbc.QSupplier.supplier;

@Transactional
public class ProductDaoImpl implements ProductDao {

    @Inject
    R2DBCQueryFactory queryFactory;

    final QBean<ProductL10n> productL10nBean = bean(ProductL10n.class,
            productL10n.description, productL10n.lang, productL10n.name);

    final QBean<Product> productBean = bean(Product.class,
            product.id, product.name, product.otherProductDetails, product.price,
            bean(Supplier.class, supplier.all()).as("supplier"),
            GroupBy.set(productL10nBean).as("localizations"));

    @Override
    public Mono<Product> findById(long id) {
        return getBaseQuery(product.id.eq(id)).singleOrEmpty();
    }

    @Override
    public Flux<Product> findAll(Predicate... where) {
        return getBaseQuery(where);
    }

    private <T extends ReactiveStoreClause<T>> T populate(T dml, Product p) {
        return dml
                .set(product.name, p.getName())
                .set(product.otherProductDetails, p.getOtherProductDetails())
                .set(product.price, p.getPrice())
                .set(product.supplierId, p.getSupplier().getId());
    }

    @Override
    public Mono<Product> save(Product p) {
        Long id = p.getId();

        if (id == null) {
            return insert(p);
        }

        return update(p);
    }

    public Mono<Product> insert(Product p) {
        return populate(queryFactory.insert(product), p)
                .executeWithKey(product.id)
                .flatMap(id -> {
                    R2DBCInsertClause insert = queryFactory.insert(productL10n);
                    for (ProductL10n l10n : p.getLocalizations()) {
                        insert
                                .set(productL10n.productId, id)
                                .set(productL10n.description, l10n.getDescription())
                                .set(productL10n.lang, l10n.getLang())
                                .set(productL10n.name, l10n.getName())
                                .addBatch();
                    }

                    return insert
                            .execute()
                            .map(__ -> {
                                p.setId(id);

                                return p;
                            });
                });

    }

    public Mono<Product> update(Product p) {
        Long id = p.getId();

        return populate(queryFactory.update(product), p)
                .where(product.id.eq(id))
                .execute()
                .flatMap(__ -> {
                    // delete l10n rows
                    return queryFactory
                            .delete(productL10n)
                            .where(productL10n.productId.eq(id))
                            .execute()
                            .flatMap(___ -> {
                                R2DBCInsertClause insert = queryFactory.insert(productL10n);
                                for (ProductL10n l10n : p.getLocalizations()) {
                                    insert
                                            .set(productL10n.productId, id)
                                            .set(productL10n.description, l10n.getDescription())
                                            .set(productL10n.lang, l10n.getLang())
                                            .set(productL10n.name, l10n.getName())
                                            .addBatch();
                                }

                                return insert
                                        .execute()
                                        .map(____ -> p);
                            });
                });
    }

    @Override
    public Mono<Long> count() {
        return queryFactory.from(product).fetchCount();
    }

    @Override
    public Mono<Void> delete(Product p) {
        // TODO use combined delete clause
        return queryFactory
                .delete(productL10n)
                .where(productL10n.productId.eq(p.getId()))
                .execute()
                .then(queryFactory
                        .delete(product)
                        .where(product.id.eq(p.getId()))
                        .execute()
                )
                .then();
    }

    private Flux<Product> getBaseQuery(Predicate... where) {
        return (Flux<Product>) queryFactory
                .select(productBean)
                .from(product)
                .innerJoin(product.supplierFk, supplier)
                .innerJoin(product._productFk, productL10n)
                .where(where)
                .transform(ReactiveGroupBy.groupBy(product.id).flux(productBean));

    }

}
