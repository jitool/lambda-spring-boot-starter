package com.dreamf.lambda.service.impl;

import com.dreamf.lambda.annotation.Lambda;
import com.dreamf.lambda.service.Test;
import com.dreamf.lambda.service.User;
import org.springframework.beans.factory.annotation.Autowired;

public class TestImpl {

    @Autowired
    private User user ;

    @Lambda
    private Test test=()->{return user.getName();};

}
