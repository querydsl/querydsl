package com.querydsl.example.jpms.service;

import com.querydsl.example.jpms.entity.ExampleEntity;

import java.util.List;
import java.util.Optional;

public interface ExampleService {
    Optional<ExampleEntity> getExample(long id);

    List<ExampleEntity> getAllExamples();

    ExampleEntity addExample(String name);
}