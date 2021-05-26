package org.dvare.util;

import org.dvare.binding.data.DataRow;
import org.dvare.exceptions.interpreter.IllegalPropertyValueException;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Iterator;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
public class ValueFinder {
    public static Object findValue(String name, Object object) throws IllegalPropertyValueException {

        if (object == null) {
            return null;
        }
        if (object instanceof DataRow) {
            DataRow dataRow = (DataRow) object;
            return dataRow.getValue(name);
        } else {


            try {
                Class<?> type = object.getClass();
                if (name.contains(".")) {

                    String[] fields = name.split("\\.");

                    Iterator<String> iterator = Arrays.asList(fields).iterator();

                    Class<?> childType = type;
                    Object chieldValue = object;
                    while (iterator.hasNext()) {
                        String field = iterator.next();

                        if (iterator.hasNext()) {

                            Field newType = ClassUtils.getDeclaredField(childType, field, true);
                            childType = newType.getType();
                            chieldValue = ClassUtils.readField(newType, chieldValue, true);
                        } else {
                            Field newType = ClassUtils.getDeclaredField(childType, field, true);
                            if (newType != null) {

                                return ClassUtils.readField(newType, chieldValue, true);
                            }
                        }

                    }


                } else {
                    Field newType = ClassUtils.getDeclaredField(type, name, true);
                    if (newType != null) {
                        return ClassUtils.readField(newType, object, true);
                    }
                }
            } catch (IllegalAccessException e) {
                throw new IllegalPropertyValueException("Variable value not found ", e);
            }
            return null;
        }

    }


    public static Object updateValue(Object object, String name, Object value) throws IllegalPropertyValueException {

        if (object instanceof DataRow) {
            DataRow dataRow = (DataRow) object;
            dataRow.getData().put(name, value);
            return dataRow;
        } else {


            try {
                Class<?> type = object.getClass();
                if (name.contains(".")) {

                    String[] fields = name.split("\\.");

                    Iterator<String> iterator = Arrays.asList(fields).iterator();

                    Class<?> childType = type;
                    while (iterator.hasNext()) {
                        String field = iterator.next();

                        if (iterator.hasNext()) {

                            Field newType = ClassUtils.getDeclaredField(childType, field, true);
                            childType = newType.getType();

                        } else {
                            Field newType = ClassUtils.getDeclaredField(childType, field, true);
                            if (newType != null) {
                                ClassUtils.writeField(newType, object, value, true);
                                return object;
                            }
                        }

                    }


                } else {
                    Field newType = ClassUtils.getDeclaredField(type, name, true);
                    if (newType != null) {
                        ClassUtils.writeField(newType, object, value, true);
                        return object;
                    }
                }
            } catch (IllegalAccessException e) {
                throw new IllegalPropertyValueException("Variable value not found ", e);
            }

            return null;
        }

    }
}
