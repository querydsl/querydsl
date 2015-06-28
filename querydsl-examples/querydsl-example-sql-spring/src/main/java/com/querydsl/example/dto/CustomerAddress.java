package com.querydsl.example.dto;

import org.joda.time.LocalDate;

import lombok.Data;

@Data
public class CustomerAddress {

    private Address address;

    private String addressTypeCode;

    private LocalDate fromDate, toDate;

}

