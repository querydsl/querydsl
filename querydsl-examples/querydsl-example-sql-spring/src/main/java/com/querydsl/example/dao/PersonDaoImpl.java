package com.querydsl.example.dao;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.QBean;
import com.querydsl.example.dto.Person;
import com.querydsl.sql.SQLQueryFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

import static com.querydsl.core.types.Projections.bean;
import static com.querydsl.example.sql.QPerson.person;

@Transactional
public class PersonDaoImpl implements PersonDao {
    
    @Inject
    SQLQueryFactory queryFactory;

    final QBean<Person> personBean = bean(Person.class, person.all());
    
    @Override
    public Person findById(long id) {
        return queryFactory.select(personBean)
                .from(person)
                .where(person.id.eq(id))
                .fetchOne();
    }
    
    @Override
    public List<Person> findAll(Predicate... where) {
        return queryFactory.select(personBean)
                .from(person)
                .where(where)
                .fetch();
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
        return queryFactory.from(person).fetchCount();
    }

    @Override
    public void delete(Person p) {
        queryFactory.delete(person)
            .where(person.id.eq(p.getId()))
            .execute();        
    }

}
