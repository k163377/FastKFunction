package com.mapk.fastkfunction.spreadwrapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ForInnerConstructor<T> implements SpreadWrapper<T> {
    private final Constructor<T> constructor;
    private final Object[] originalArgArray;

    public ForInnerConstructor(Constructor<T> constructor, Object instance, int paramSize) {
        this.constructor = constructor;
        originalArgArray = new Object[paramSize];
        originalArgArray[0] = instance;
    }

    @Override
    public T call(Object[] args) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Object[] argsDst = originalArgArray.clone();
        System.arraycopy(args, 0, argsDst, 1, args.length);

        return constructor.newInstance(argsDst);
    }
}
