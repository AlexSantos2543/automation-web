package com.automation.web.helpers;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Extract generic type arguments from this class or the first parent to have type arguments
 */
public class GenericsHelper {

    private GenericsHelper() {
    }

    public static Type[] extractGenericArguments(Class c) {
        Class parent = c;

        while (parent != Object.class) {
            if (parent.getGenericSuperclass() instanceof ParameterizedType) {
                return ((ParameterizedType) parent.getGenericSuperclass()).getActualTypeArguments();
            }
            parent = parent.getSuperclass();
        }
        return new Type[]{};
    }
}
