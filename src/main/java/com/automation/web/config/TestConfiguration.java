package com.automation.web.config;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;

@Lazy
@Configuration
@ComponentScan(basePackages = {
        "com.automation.web.*"

})
@Import(WebDriverConfiguration.class)
public class TestConfiguration implements InitializingBean {

    @Autowired private BeanFactory beanFactory;

    @Override
    public void afterPropertiesSet() {
        beanFactory.getBean(Wait.class).configure(120L, 3L);
    }
//
//    @Bean
//    public I18n i18n() {
//        return (I18n) new DefaultI18n(new JsonStorageFactory(
//                "src/main/resources/i18n/translations-%s.json"));
//    }
}