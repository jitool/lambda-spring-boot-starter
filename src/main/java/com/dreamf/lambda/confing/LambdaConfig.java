package com.dreamf.lambda.confing;

import com.dreamf.lambda.LambdaSpringBootStarterApplication;
import com.dreamf.lambda.springbootfactory.SpringBeanFactory;
import org.springframework.context.annotation.Bean;

public class LambdaConfig {

    @Bean
    public SpringBeanFactory springBeanFactory(){
        return new SpringBeanFactory();
    }

    @Bean(initMethod = "start")
    public LambdaSpringBootStarterApplication LambdaSpringBootStarterApplication(){
        return new LambdaSpringBootStarterApplication();
    }

}
