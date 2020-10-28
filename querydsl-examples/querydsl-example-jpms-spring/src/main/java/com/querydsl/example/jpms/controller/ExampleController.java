package com.querydsl.example.jpms.controller;

import com.querydsl.example.jpms.service.ExampleService;
import com.querydsl.example.jpms.entity.ExampleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/example")
public class ExampleController {

    private final ExampleService exampleService;

    @Autowired
    public ExampleController(ExampleService exampleService) {
        this.exampleService = exampleService;
    }

    @GetMapping("/{id}")
    public ExampleEntity getExample(@PathVariable Long id) {
        return exampleService.getExample(id).orElse(null);
    }

    @GetMapping("/all")
    public List<ExampleEntity> getAllExamples() {
        return exampleService.getAllExamples();
    }

    @PostMapping("/add/{name}")
    public ExampleEntity addExample(@PathVariable String name) {
        return exampleService.addExample(name);
    }
}