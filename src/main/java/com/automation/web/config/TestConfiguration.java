package com.automation.web.config;

import com.automation.web.setup.I18n;
import com.automation.web.translationi18n.DefaultI18n;
import com.automation.web.translationi18n.JsonStorageFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

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

    @Bean
    public I18n i18n() {
        return (I18n) new DefaultI18n(new JsonStorageFactory(
                "src/main/resources/i18n/translations-%s.json"));
    }
}