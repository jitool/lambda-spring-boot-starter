package com.dreamf.lambda.service.impl;

import com.dreamf.lambda.annotation.Lambda;
import com.dreamf.lambda.service.Test;

import java.util.Iterator;
import java.util.Map;


public class TestImpl {

    //@Autowired
    //private User user ;

    //@Lambda(name = "test",version = 1)
    //private Test test= ()->user.getName();

    @Lambda(name = "test",version = 2)
    private Test test1=()->{return "新版本";};

    @Lambda(name = "test",version = 3)
    private Test test2=()->{return "新版本1";};

    @Lambda(name = "test",version = 10)
    private Test test3=()->{return "跳跃版本";};


}
