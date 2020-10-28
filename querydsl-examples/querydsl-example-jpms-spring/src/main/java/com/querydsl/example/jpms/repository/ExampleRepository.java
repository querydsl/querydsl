package com.querydsl.example.jpms.repository;

import com.querydsl.example.jpms.entity.ExampleEntity;
import com.querydsl.example.jpms.entity.QExampleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

@Repository
public interface ExampleRepository extends JpaRepository<ExampleEntity, Long>,
        QuerydslPredicateExecutor<ExampleEntity>, QuerydslBinderCustomizer<QExampleEntity> {

    @Override
    default public void customize(QuerydslBindings bindings, QExampleEntity root) {
    }

}
