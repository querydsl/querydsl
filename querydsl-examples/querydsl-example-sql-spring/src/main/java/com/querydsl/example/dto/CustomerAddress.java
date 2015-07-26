package com.querydsl.example.dto;

import lombok.Data;

import org.joda.time.LocalDate;

@Data
public class CustomerAddress {

    private Address address;

    private String addressTypeCode;

    private LocalDate dateFrom, dateTo;

}

