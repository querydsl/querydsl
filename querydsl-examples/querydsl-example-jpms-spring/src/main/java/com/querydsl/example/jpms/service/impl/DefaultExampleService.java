package com.querydsl.example.jpms.service.impl;

import com.google.common.collect.Lists;
import com.querydsl.example.jpms.entity.ExampleEntity;
import com.querydsl.example.jpms.repository.ExampleRepository;
import com.querydsl.example.jpms.service.ExampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.querydsl.example.jpms.entity.QExampleEntity.exampleEntity;

@Service
public class DefaultExampleService implements ExampleService {

    private final ExampleRepository exampleRepository;

    @Autowired
    public DefaultExampleService(ExampleRepository exampleRepository) {
        this.exampleRepository = exampleRepository;
    }

    @Override
    public Optional<ExampleEntity> getExample(long id) {
        return exampleRepository.findById(id);
    }

    @Override
    public List<ExampleEntity> getAllExamples() {
        return Lists.newArrayList(exampleRepository.findAll(exampleEntity.id.gt(2)));
    }

    @Override
    public ExampleEntity addExample(String name) {
        ExampleEntity exampleEntity = new ExampleEntity();
        exampleEntity.setName(name);
        return exampleRepository.save(exampleEntity);
    }

}