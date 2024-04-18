package com.example.entity;

import lombok.AllArgsConstructor;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@AllArgsConstructor
@Table
public class Employee {

    @PrimaryKey
    public final EmployeePrimaryKey key;

    @Column
    public String phoneNumber;

}
