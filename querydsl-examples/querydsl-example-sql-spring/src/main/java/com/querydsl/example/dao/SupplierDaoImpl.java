package com.querydsl.example.dao;

import static com.mysema.query.types.Projections.bean;
import static com.querydsl.example.sql.QSupplier.supplier;

import java.util.List;

import javax.inject.Inject;

import org.springframework.transaction.annotation.Transactional;

import com.mysema.query.sql.SQLQueryFactory;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.QBean;
import com.querydsl.example.dto.Supplier;

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
    public List<Supplier> findAll(Predicate where) {
        return queryFactory.from(supplier)
            .where(where)
            .list(supplierBean);
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
        return queryFactory.from(supplier).count();
    }

    @Override
    public void delete(Supplier s) {
        queryFactory.delete(supplier)
            .where(supplier.id.eq(s.getId()))
            .execute();
    }

}
