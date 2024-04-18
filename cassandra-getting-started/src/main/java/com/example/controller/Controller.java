package com.example.controller;

import com.example.service.CassandraService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class Controller {

    private final CassandraService cassandraService;


    @GetMapping("/cassandra-test")
    public void cassandraTest() {
        cassandraService.casTest();
    }


}
