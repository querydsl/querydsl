package com.querydsl.example.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class OrderDelivery {

    private String deliveryStatusCode;

    private LocalDate reportedDate;

}

