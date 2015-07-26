package com.querydsl.example.dao;

import static com.mysema.query.types.Projections.bean;
import static com.querydsl.example.sql.QPerson.person;

import java.util.List;

import javax.inject.Inject;

import org.springframework.transaction.annotation.Transactional;

import com.mysema.query.sql.SQLQueryFactory;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.QBean;
import com.querydsl.example.dto.Person;

@Transactional
public class PersonDaoImpl implements PersonDao {

    @Inject
    SQLQueryFactory queryFactory;

    final QBean<Person> personBean = bean(Person.class, person.all());

    @Override
    public Person findById(long id) {
        return queryFactory.from(person)
                .where(person.id.eq(id))
                .singleResult(personBean);
    }

    @Override
    public List<Person> findAll(Predicate where) {
        return queryFactory.from(person)
                .where(where)
                .list(personBean);
    }

    @Override
    public Person save(Person p) {
        Long id = p.getId();

        if (id == null) {
            id = queryFactory.insert(person)
                .populate(p)
                .executeWithKey(person.id);
            p.setId(id);
        } else {
            queryFactory.update(person)
                .populate(p)
                .where(person.id.eq(id)).execute();
        }

        return p;
    }

    @Override
    public long count() {
        return queryFactory.from(person).count();
    }

    @Override
    public void delete(Person p) {
        queryFactory.delete(person)
            .where(person.id.eq(p.getId()))
            .execute();
    }

}
