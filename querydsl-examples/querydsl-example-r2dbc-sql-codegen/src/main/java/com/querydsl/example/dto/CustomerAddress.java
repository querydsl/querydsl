package com.querydsl.example.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CustomerAddress {

    private Address address;

    private String addressTypeCode;

    private LocalDate fromDate, toDate;

}

