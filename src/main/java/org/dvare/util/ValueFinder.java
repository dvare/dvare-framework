package org.dvare.util;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.dvare.binding.data.DataRow;
import org.dvare.exceptions.interpreter.IllegalPropertyValueException;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Iterator;

public class ValueFinder {
    public static Object findValue(String name, Object object) throws IllegalPropertyValueException {

        if (object instanceof DataRow) {
            DataRow dataRow = (DataRow) object;
            return dataRow.getValue(name);
        } else {


            try {
                Class type = object.getClass();
                if (name.contains(".")) {

                    String fields[] = name.split(".");

                    Iterator<String> iterator = Arrays.asList(fields).iterator();

                    Class childType = type;
                    while (iterator.hasNext()) {
                        String field = iterator.next();

                        if (iterator.hasNext()) {

                            Field newType = FieldUtils.getDeclaredField(childType, field, true);
                            childType = newType.getType();

                        } else {
                            Field newType = FieldUtils.getDeclaredField(childType, field, true);
                            if (newType != null) {

                                return FieldUtils.readField(newType, object, true);
                            }
                        }

                    }


                } else {
                    String field = name;
                    Field newType = FieldUtils.getDeclaredField(type, field, true);
                    if (newType != null) {
                        return FieldUtils.readField(newType, object, true);
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
                Class type = object.getClass();
                if (name.contains(".")) {

                    String fields[] = name.split(".");

                    Iterator<String> iterator = Arrays.asList(fields).iterator();

                    Class childType = type;
                    while (iterator.hasNext()) {
                        String field = iterator.next();

                        if (iterator.hasNext()) {

                            Field newType = FieldUtils.getDeclaredField(childType, field, true);
                            childType = newType.getType();

                        } else {
                            Field newType = FieldUtils.getDeclaredField(childType, field, true);
                            if (newType != null) {
                                FieldUtils.writeField(newType, object, value, true);
                                return object;
                            }
                        }

                    }


                } else {
                    String field = name;
                    Field newType = FieldUtils.getDeclaredField(type, field, true);
                    if (newType != null) {
                        FieldUtils.writeField(newType, object, value, true);
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
