package com.querydsl.example.dao;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.QBean;
import com.querydsl.example.dto.Person;
import com.querydsl.r2dbc.R2DBCQuery;
import com.querydsl.r2dbc.R2DBCQueryFactory;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.inject.Inject;

import static com.querydsl.core.types.Projections.bean;
import static com.querydsl.example.r2dbc.QPerson.person;

@Transactional
public class PersonDaoImpl implements PersonDao {

    @Inject
    R2DBCQueryFactory queryFactory;

    final QBean<Person> personBean = bean(Person.class, person.all());

    @Override
    public Mono<Person> findById(long id) {
        return getBaseQuery(person.id.eq(id)).fetchOne();
    }

    @Override
    public Flux<Person> findAll(Predicate... where) {
        return getBaseQuery(where).fetch();
    }

    @Override
    public Mono<Person> save(Person p) {
        Long id = p.getId();

        if (id == null) {
            return insert(p);
        }

        return update(p);
    }

    public Mono<Person> insert(Person p) {
        return queryFactory
                .insert(person)
                .populate(p)
                .executeWithKey(person.id)
                .map(id -> {
                    p.setId(id);

                    return p;
                });
    }

    public Mono<Person> update(Person p) {
        long id = p.getId();

        return queryFactory
                .update(person)
                .populate(p)
                .where(person.id.eq(id))
                .execute()
                .map(__ -> p);
    }

    @Override
    public Mono<Long> count() {
        return queryFactory.from(person).fetchCount();
    }

    @Override
    public Mono<Void> delete(Person p) {
        return queryFactory
                .delete(person)
                .where(person.id.eq(p.getId()))
                .execute()
                .then();
    }

    private R2DBCQuery<Person> getBaseQuery(Predicate... where) {
        return queryFactory
                .select(personBean)
                .from(person)
                .where(where);
    }

}
