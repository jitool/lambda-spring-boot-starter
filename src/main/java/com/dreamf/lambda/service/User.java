package com.dreamf.lambda.service;

import org.springframework.stereotype.Service;

@Service("user")
public class User {

    private String name="我的名字";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
