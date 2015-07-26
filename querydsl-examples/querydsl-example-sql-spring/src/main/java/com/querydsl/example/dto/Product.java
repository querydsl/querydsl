package com.querydsl.example.dto;

import java.util.Set;

import lombok.Data;

@Data
public class Product {

    private Long id;

    private String name;

    private String otherProductDetails;

    private Double price;

    private Supplier supplier;

    private Set<ProductL10n> localizations;
}

