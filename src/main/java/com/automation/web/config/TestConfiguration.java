package com.automation.web.config;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = {
        "com.automation.web.*"
})
public class TestConfiguration implements InitializingBean {

    @Autowired
    private BeanFactory beanFactory;

    @Override
    public void afterPropertiesSet() {
        beanFactory.getBean(Wait.class).configure(120L, 3L);
    }
}