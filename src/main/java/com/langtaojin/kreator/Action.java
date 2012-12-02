package com.langtaojin.kreator;

import java.lang.reflect.Method;

public class Action {
    public final Object instance;
    public final Method method;
    public final Class<?>[] arguments;

    public Action(Object instance, Method method) {
        this.instance = instance;
        this.method = method;
        this.arguments = method.getParameterTypes();
    }
}
