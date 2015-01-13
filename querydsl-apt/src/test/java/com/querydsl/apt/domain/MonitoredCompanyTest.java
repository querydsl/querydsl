package com.querydsl.apt.domain;

import static org.junit.Assert.*;

import org.junit.Test;

import com.querydsl.apt.domain.QMonitoredCompany;

public class MonitoredCompanyTest {
    
    @Test
    public void test() {
        QMonitoredCompany monitoredCompany = QMonitoredCompany.monitoredCompany;
        assertNotNull(monitoredCompany.companyGroup);
        assertNotNull(monitoredCompany.companyGroup.mainCompany);
    }

}
