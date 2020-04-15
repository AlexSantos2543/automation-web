package com.automation.web.pageobjects.factory;

import org.openqa.selenium.SearchContext;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;

public abstract class FieldInitializer {
    @Autowired
    protected BeanFactory beanFactory;

    public abstract void initFields(SearchContext context, Object page);

    public abstract void initField(SearchContext context, Object page, Field field);
}

