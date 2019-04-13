package com.dreamf.lambda.controller;


import com.dreamf.lambda.annotation.LambdaInject;
import com.dreamf.lambda.service.Test;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {

    @LambdaInject(name="test")
    private Test test;

    @ResponseBody
    @GetMapping("test")
    public Object show(){
        return test.name();
    }

}
