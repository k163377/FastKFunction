package com.mapk.fastkfunction.spreadwrapper;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ForMethod<T> implements SpreadWrapper<T> {
    private final Method method;
    private final Object instance;

    public ForMethod(@NotNull Method method, @Nullable Object instance) {
        this.method = method;
        this.instance = instance;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T call(Object[] args) throws InvocationTargetException, IllegalAccessException {
        return (T) method.invoke(instance, args);
    }
}
