package com.querydsl.example.dao;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.QBean;
import com.querydsl.example.dto.Supplier;
import com.querydsl.sql.SQLQueryFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

import static com.querydsl.core.types.Projections.bean;
import static com.querydsl.example.sql.QSupplier.supplier;

@Transactional
public class SupplierDaoImpl implements SupplierDao {

    @Inject
    SQLQueryFactory queryFactory;

    final QBean<Supplier> supplierBean = bean(Supplier.class, supplier.all());

    @Override
    public Supplier findById(long id) {
        List<Supplier> suppliers = findAll(supplier.id.eq(id));
        return suppliers.isEmpty() ? null : suppliers.get(0);
    }

    @Override
    public List<Supplier> findAll(Predicate... where) {
        return queryFactory.select(supplierBean)
            .from(supplier)
            .where(where)
            .fetch();
    }

    @Override
    public Supplier save(Supplier s) {
        if (s.getId() == null) {
            Long id = queryFactory.insert(supplier)
                .set(supplier.code, s.getCode())
                .set(supplier.name, s.getName())
                .executeWithKey(supplier.id);
            s.setId(id);
        } else {
            queryFactory.update(supplier)
                .set(supplier.code, s.getCode())
                .set(supplier.name, s.getName())
                .where(supplier.id.eq(s.getId()))
                .execute();
        }

        return s;
    }

    @Override
    public long count() {
        return queryFactory.from(supplier).fetchCount();
    }

    @Override
    public void delete(Supplier s) {
        queryFactory.delete(supplier)
            .where(supplier.id.eq(s.getId()))
            .execute();
    }

}
