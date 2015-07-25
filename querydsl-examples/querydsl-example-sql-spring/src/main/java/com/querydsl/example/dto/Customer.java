package com.querydsl.example.dto;

import java.util.Set;

import lombok.Data;

@Data
public class Customer {

    private Long id;

    private Person contactPerson;

    private String name;

    private Set<CustomerAddress> addresses;

}

