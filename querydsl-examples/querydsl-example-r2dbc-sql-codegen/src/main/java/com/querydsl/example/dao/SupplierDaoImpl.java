package com.querydsl.example.dao;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.QBean;
import com.querydsl.example.dto.Supplier;
import com.querydsl.r2dbc.R2DBCQuery;
import com.querydsl.r2dbc.R2DBCQueryFactory;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.inject.Inject;

import static com.querydsl.core.types.Projections.bean;
import static com.querydsl.example.r2dbc.QSupplier.supplier;

@Transactional
public class SupplierDaoImpl implements SupplierDao {

    @Inject
    R2DBCQueryFactory queryFactory;

    final QBean<Supplier> supplierBean = bean(Supplier.class, supplier.all());

    @Override
    public Mono<Supplier> findById(long id) {
        return getBaseQuery(supplier.id.eq(id)).fetchOne();
    }

    @Override
    public Flux<Supplier> findAll(Predicate... where) {
        return getBaseQuery(where).fetch();
    }

    @Override
    public Mono<Supplier> save(Supplier p) {
        Long id = p.getId();

        if (id == null) {
            return insert(p);
        }

        return update(p);
    }

    public Mono<Supplier> insert(Supplier s) {
        return queryFactory
                .insert(supplier)
                .set(supplier.code, s.getCode())
                .set(supplier.name, s.getName())
                .executeWithKey(supplier.id)
                .map(id -> {
                    s.setId(id);

                    return s;
                });
    }

    public Mono<Supplier> update(Supplier s) {
        return queryFactory
                .update(supplier)
                .set(supplier.code, s.getCode())
                .set(supplier.name, s.getName())
                .where(supplier.id.eq(s.getId()))
                .execute()
                .map(__ -> s);
    }

    @Override
    public Mono<Long> count() {
        return queryFactory.from(supplier).fetchCount();
    }

    @Override
    public Mono<Void> delete(Supplier s) {
        return queryFactory
                .delete(supplier)
                .where(supplier.id.eq(s.getId()))
                .execute()
                .then();
    }

    private R2DBCQuery<Supplier> getBaseQuery(Predicate... where) {
        return queryFactory
                .select(supplierBean)
                .from(supplier)
                .where(where);
    }

}
