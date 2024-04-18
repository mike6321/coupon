package com.example.entity;

import lombok.RequiredArgsConstructor;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

@RequiredArgsConstructor
@PrimaryKeyClass
public class EmployeePrimaryKey {

    @PrimaryKeyColumn(name = "location", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    public final String location;

    @PrimaryKeyColumn(name = "department", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
    public final String department;

    @PrimaryKeyColumn(name = "name", ordinal = 2, type = PrimaryKeyType.CLUSTERED)
    public final String name;

}
