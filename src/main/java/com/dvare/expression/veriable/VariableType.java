/*The MIT License (MIT)

Copyright (c) 2016 Muhammad Hammad

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Sogiftware.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.*/


package com.dvare.expression.veriable;


import com.dvare.exceptions.interpreter.IllegalPropertyValueException;
import com.dvare.exceptions.parser.IllegalPropertyException;
import com.dvare.expression.datatype.DataType;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;

public class VariableType {
    static Logger logger = LoggerFactory.getLogger(VariableType.class);
    static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    static SimpleDateFormat datTimeFormat = new SimpleDateFormat("dd-MM-yyyy-HH:mm:ss");


    public static VariableExpression getVariableType(String name, String type) throws IllegalPropertyException {

        return getVariableType(name, DataType.valueOf(type));
    }


    public static VariableExpression getVariableType(String name, DataType type) throws IllegalPropertyException {
        VariableExpression variableExpression = null;

        if (name == null)
            throw new IllegalPropertyException("The provided string must not be null");

        if (type == null) {
            throw new IllegalPropertyException("The provided type must not be null");
        }


        switch (type) {
            case BooleanType: {
                variableExpression = new com.dvare.expression.veriable.BooleanVariable(name);
                break;
            }
            case FloatType: {
                variableExpression = new FloatVariable(name);
                break;
            }
            case IntegerType: {
                variableExpression = new IntegerVariable(name);
                break;
            }
            case StringType: {
                variableExpression = new StringVariable(name);
                break;
            }
            case DateTimeType: {
                variableExpression = new DateTimeVariable(name);
                break;
            }
            case DateType: {
                variableExpression = new DateVariable(name);
                break;
            }
            case RegexType: {
                variableExpression = new StringVariable(name);
                break;
            }
            default: {
                String message = String.format("Type of Variable Expression {} not found", name);
                logger.error(message);
                throw new IllegalPropertyException(message);
            }
        }


        logger.debug("Variable  Expression Veriable: {} [{}]", variableExpression.getClass().getSimpleName(), variableExpression.getName());
        return variableExpression;
    }


    public static VariableExpression setVariableValue(VariableExpression variable, Object object) throws IllegalPropertyValueException {

        Object value = findValue(variable.getName(), object);

        return setValue(variable, value);
    }

    private static VariableExpression setValue(VariableExpression variable, Object value) throws IllegalPropertyValueException {

        if (value == null)
            return variable;


        switch (variable.getType().getDataType()) {
            case BooleanType: {
                if (value instanceof Boolean) {
                    variable.setValue((Boolean) value);
                } else {
                    variable.setValue(Boolean.parseBoolean((String) value));
                }

                break;
            }
            case FloatType: {
                try {
                    if (value instanceof Float) {
                        variable.setValue((Float) value);
                    } else {
                        variable.setValue(Float.parseFloat(value.toString()));
                    }

                } catch (NumberFormatException e) {
                    String message = String.format("Unable to Parse literal %s to Float", value);
                    logger.error(message);
                    throw new IllegalPropertyValueException(message);
                }
                break;
            }
            case IntegerType: {
                try {
                    if (value instanceof Integer) {
                        variable.setValue((Integer) value);
                    } else {
                        variable.setValue(Integer.parseInt((String) value));
                    }

                } catch (NumberFormatException e) {
                    String message = String.format("Unable to Parse literal %s to Integer", value);
                    logger.error(message);
                    throw new IllegalPropertyValueException(message);
                }
                break;
            }
            case StringType: {
                variable.setValue((String) value);
                break;
            }
            case DateTimeType: {

                Date date = null;
                if (value instanceof Date) {
                    date = (Date) value;
                } else {
                    String newValue = (String) value;
                    try {
                        date = datTimeFormat.parse(newValue);
                    } catch (ParseException e) {
                        String message = String.format("Unable to Parse literal %s to Date Time", newValue);
                        logger.error(message);
                        throw new IllegalPropertyValueException(message);
                    }
                }

                variable.setValue(date);
                break;
            }
            case DateType: {


                Date date = null;
                if (value instanceof Date) {
                    date = (Date) value;
                } else {
                    String newValue = (String) value;
                    try {
                        date = dateFormat.parse(newValue);
                    } catch (ParseException e) {
                        String message = String.format("Unable to Parse literal %s to Date", newValue);
                        logger.error(message);
                        throw new IllegalPropertyValueException(message);

                    }
                }
                variable.setValue(date);
                break;
            }
            case RegexType: {
                variable.setValue((String) value);
                break;
            }
        }


        return variable;
    }


    private static Object findValue(String name, Object object) throws IllegalPropertyValueException {
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
            throw new IllegalPropertyValueException("Variable value not found ");
        }
        return null;
    }


}
