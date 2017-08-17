/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2016-2017 DVARE (Data Validation and Aggregation Rule Engine)
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Sogiftware.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

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

                    String fields[] = name.split("\\.");

                    Iterator<String> iterator = Arrays.asList(fields).iterator();

                    Class childType = type;
                    Object chieldValue = object;
                    while (iterator.hasNext()) {
                        String field = iterator.next();

                        if (iterator.hasNext()) {

                            Field newType = FieldUtils.getDeclaredField(childType, field, true);
                            childType = newType.getType();
                            chieldValue = FieldUtils.readField(newType, chieldValue, true);
                        } else {
                            Field newType = FieldUtils.getDeclaredField(childType, field, true);
                            if (newType != null) {

                                return FieldUtils.readField(newType, chieldValue, true);
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
