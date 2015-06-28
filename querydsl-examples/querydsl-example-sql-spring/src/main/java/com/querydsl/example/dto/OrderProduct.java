package com.querydsl.example.dto;

import lombok.Data;

@Data
public class OrderProduct {

    private Long productId;
    
    private String comments;

    private Integer quantity;

}

