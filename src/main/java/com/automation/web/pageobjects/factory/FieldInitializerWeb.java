package com.automation.web.pageobjects.factory;

import java.lang.reflect.Field;

import com.automation.web.browsers.Chrome;
import com.automation.web.browsers.Safari;
import com.automation.web.pageobjects.decorator.ElementFieldDecorator;
import com.automation.web.pageobjects.decorator.FragmentFieldDecorator;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Chrome
@Safari
@Lazy
@Service
public class FieldInitializerWeb extends FieldInitializer {

    @Override
    public void initFields(SearchContext context, Object page) {
        // init WebElement fields
        PageFactory.initElements(new ElementFieldDecorator(
                new DefaultElementLocatorFactory(context)), page);

        // init Fragment fields
        PageFactory.initElements(new FragmentFieldDecorator(
                new DefaultElementLocatorFactory(context), beanFactory), page);
    }

    @Override
    public void initField(SearchContext context, Object page, Field field) {
        // try initializing a WebElement field
        Object value = new ElementFieldDecorator(
                new DefaultElementLocatorFactory(context))
                .decorate(page.getClass().getClassLoader(), field);

        // try initializing a Fragment field
        if (value == null) {
            value = new FragmentFieldDecorator(
                    new DefaultElementLocatorFactory(context), beanFactory)
                    .decorate(page.getClass().getClassLoader(), field);
        }

        if (value != null) {
            try {
                field.setAccessible(true);
                field.set(page, value);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
