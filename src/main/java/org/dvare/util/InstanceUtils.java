package org.dvare.util;

public class InstanceUtils<T> {

    public T newInstance(Class<? extends T> aClass) throws Exception {
        return aClass.getDeclaredConstructor().newInstance();
    }

}
