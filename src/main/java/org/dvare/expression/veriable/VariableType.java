/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016-2017 DVARE (Data Validation and Aggregation Rule Engine)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Sogiftware.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.dvare.expression.veriable;


import javafx.util.Pair;
import org.dvare.annotations.Type;
import org.dvare.exceptions.interpreter.IllegalPropertyValueException;
import org.dvare.exceptions.parser.IllegalPropertyException;
import org.dvare.expression.datatype.*;
import org.dvare.expression.literal.LiteralType;
import org.dvare.util.ValueFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class VariableType {
    private static Logger logger = LoggerFactory.getLogger(VariableType.class);


    public static VariableExpression getVariableExpression(String name, DataType type, String operandType) throws IllegalPropertyException {
        VariableExpression variableExpression = getVariableExpression(name, type);
        variableExpression.setOperandType(operandType);
        return variableExpression;
    }


    public static VariableExpression getVariableExpression(String name, DataType type) throws IllegalPropertyException {
        VariableExpression variableExpression;

        if (name == null)
            throw new IllegalPropertyException("The provided string must not be null");

        if (type == null) {
            throw new IllegalPropertyException("The provided type must not be null");
        }


        switch (type) {
            case BooleanType: {
                variableExpression = new BooleanVariable(name);
                break;
            }
            case BooleanListType: {
                variableExpression = new ListVariable(name, BooleanType.class);
                break;
            }
            case FloatType: {
                variableExpression = new FloatVariable(name);
                break;
            }
            case FloatListType: {
                variableExpression = new ListVariable(name, FloatType.class);
                break;
            }
            case IntegerType: {
                variableExpression = new IntegerVariable(name);
                break;
            }
            case IntegerListType: {
                variableExpression = new ListVariable(name, IntegerType.class);
                break;
            }
            case StringType: {
                variableExpression = new StringVariable(name);
                break;
            }
            case StringListType: {
                variableExpression = new ListVariable(name, StringType.class);
                break;
            }
            case DateTimeType: {
                variableExpression = new DateTimeVariable(name);
                break;
            }
            case DateTimeListType: {
                variableExpression = new ListVariable(name, DateTimeType.class);
                break;
            }
            case DateType: {
                variableExpression = new DateVariable(name);
                break;
            }
            case DateListType: {
                variableExpression = new ListVariable(name, DateType.class);
                break;
            }
            case SimpleDateType: {
                variableExpression = new SimpleDateVariable(name);
                break;
            }
            case SimpleDateListType: {
                variableExpression = new ListVariable(name, SimpleDateType.class);
                break;
            }
            case PairType: {
                variableExpression = new PairVariable(name);
                break;
            }

            case PairListType: {
                variableExpression = new ListVariable(name, PairType.class);
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

        if (logger.isDebugEnabled()) {
            logger.debug("Variable  Expression Variable: {} [{}]", variableExpression.getClass().getSimpleName(), variableExpression.getName());
        }
        return variableExpression;
    }


    public static VariableExpression setVariableValue(VariableExpression variable, Object object) throws IllegalPropertyValueException {
        Object value = ValueFinder.findValue(variable.getName(), object);

        return setValue(variable, value);
    }

    private static VariableExpression setValue(VariableExpression variable, Object value) throws IllegalPropertyValueException {

        if (value == null) {
            variable.setValue(null);
            return variable;
        }

        if (variable.getType().isAnnotationPresent(Type.class)) {
            Type type = (Type) variable.getType().getAnnotation(Type.class);


            DataType dataType = type.dataType();
            switch (dataType) {
                case BooleanType: {
                    if (variable instanceof ListVariable) {
                        ListVariable listVariable = (ListVariable) variable;
                        if (value instanceof List) {
                            listVariable.setValue((List) value);
                        } else if (value.getClass().isArray()) {
                            listVariable.setValue(Arrays.asList(Boolean[].class.cast(value)));
                        } else {
                            List<Object> values = new ArrayList<>();
                            if (value instanceof Boolean) {
                                values.add(value);
                            }
                            listVariable.setValue(values);
                        }
                    } else if (variable instanceof BooleanVariable) {
                        BooleanVariable booleanVariable = (BooleanVariable) variable;
                        if (value instanceof Boolean) {
                            booleanVariable.setValue((Boolean) value);
                        } else {
                            if (value.toString().equals("")) {
                                booleanVariable.setValue(false);
                            } else {
                                booleanVariable.setValue(Boolean.parseBoolean((String) value));
                            }
                        }
                    }
                    break;
                }

                case FloatType: {
                    if (variable instanceof ListVariable) {
                        ListVariable listVariable = (ListVariable) variable;
                        if (value instanceof List) {
                            listVariable.setValue((List) value);
                        } else if (value.getClass().isArray()) {
                            listVariable.setValue(Arrays.asList(Float[].class.cast(value)));
                        } else {
                            List<Object> values = new ArrayList<>();
                            if (value instanceof Float) {
                                values.add(value);
                            } else {
                                values.add(Float.parseFloat(value.toString()));
                            }
                            listVariable.setValue(values);
                        }
                    } else if (variable instanceof FloatVariable) {
                        FloatVariable floatVariable = (FloatVariable) variable;
                        try {
                            if (value instanceof Float) {
                                floatVariable.setValue((Float) value);
                            } else {

                                if (value.toString().equals("")) {
                                    floatVariable.setValue(0f);
                                } else {
                                    floatVariable.setValue(Float.parseFloat(value.toString()));
                                }

                            }

                        } catch (NumberFormatException e) {
                            String message = String.format("Unable to Parse literal %s to Float", value);
                            logger.error(message);
                            throw new IllegalPropertyValueException(message);
                        }
                    }
                    break;
                }
                case IntegerType: {
                    if (variable instanceof ListVariable) {
                        ListVariable listVariable = (ListVariable) variable;
                        if (value instanceof List) {
                            listVariable.setValue((List) value);
                        } else if (value.getClass().isArray()) {
                            listVariable.setValue(Arrays.asList(Integer[].class.cast(value)));
                        } else {
                            List<Object> values = new ArrayList<>();
                            if (value instanceof Integer) {
                                values.add(value);
                            } else {
                                values.add(Integer.parseInt((String) value));
                            }
                            listVariable.setValue(values);
                        }
                    } else if (variable instanceof IntegerVariable) {
                        IntegerVariable integerVariable = (IntegerVariable) variable;
                        try {
                            if (value instanceof Integer) {
                                integerVariable.setValue((Integer) value);
                            } else {

                                if (value.toString().equals("")) {
                                    integerVariable.setValue(0);
                                } else {
                                    integerVariable.setValue(Integer.parseInt((String) value));
                                }
                            }

                        } catch (NumberFormatException e) {
                            String message = String.format("Unable to Parse literal %s to Integer", value);
                            logger.error(message);
                            throw new IllegalPropertyValueException(message);
                        }
                    }
                    break;
                }
                case StringType: {
                    if (variable instanceof ListVariable) {
                        ListVariable listVariable = (ListVariable) variable;
                        if (value instanceof List) {
                            listVariable.setValue((List) value);
                        } else if (value.getClass().isArray()) {
                            listVariable.setValue(Arrays.asList(String[].class.cast(value)));
                        } else {
                            List<Object> values = new ArrayList<>();
                            values.add(value);
                            listVariable.setValue(values);
                        }
                    } else if (variable instanceof StringVariable) {
                        StringVariable stringVariable = (StringVariable) variable;
                        if (value instanceof String) {
                            stringVariable.setValue((String) value);
                        } else {
                            stringVariable.setValue(value.toString());
                        }
                    }
                    break;
                }


                case PairType: {
                    if (variable instanceof ListVariable) {
                        ListVariable listVariable = (ListVariable) variable;
                        if (value instanceof List) {
                            listVariable.setValue((List) value);
                        } else if (value.getClass().isArray()) {
                            listVariable.setValue(Arrays.asList(Pair[].class.cast(value)));
                        } else {
                            List<Object> values = new ArrayList<>();
                            values.add(value);
                            listVariable.setValue(values);
                        }
                    } else if (variable instanceof PairVariable) {
                        PairVariable pairVariable = (PairVariable) variable;
                        if (value instanceof Pair) {
                            pairVariable.setValue((Pair) value);
                        }
                    }
                    break;
                }
                case DateTimeType: {
                    if (variable instanceof ListVariable) {
                        ListVariable listVariable = (ListVariable) variable;
                        if (value instanceof List) {
                            listVariable.setValue((List) value);
                        } else if (value.getClass().isArray()) {
                            listVariable.setValue(Arrays.asList(LocalDateTime[].class.cast(value)));
                        } else {
                            List<Object> values = new ArrayList<>();
                            if (value instanceof LocalDateTime) {
                                values.add(value);
                            }
                            listVariable.setValue(values);
                        }
                    } else if (variable instanceof DateTimeVariable) {
                        DateTimeVariable dateTimeVariable = (DateTimeVariable) variable;
                        LocalDateTime localDateTime = null;
                        if (value instanceof LocalDateTime) {
                            localDateTime = (LocalDateTime) value;

                        } else if (value instanceof Date) {
                            Date date = (Date) value;
                            localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

                        } else {
                            String newValue = (String) value;
                            if (!newValue.isEmpty()) {
                                try {
                                    localDateTime = LocalDateTime.parse(newValue, LiteralType.dateTimeFormat);
                                } catch (Exception e) {
                                    try {
                                        localDateTime = LocalDateTime.parse(newValue, LiteralType.defaultFormat);
                                    } catch (Exception ex) {
                                        String message = String.format("Unable to Parse literal %s to DateTime", newValue);
                                        logger.error(message);
                                        throw new IllegalPropertyValueException(message);
                                    }
                                }

                            }
                        }
                        dateTimeVariable.setValue(localDateTime);
                    }

                    break;
                }
                case DateType: {
                    if (variable instanceof ListVariable) {
                        ListVariable listVariable = (ListVariable) variable;
                        if (value instanceof List) {
                            listVariable.setValue((List) value);
                        } else if (value.getClass().isArray()) {
                            listVariable.setValue(Arrays.asList(LocalDate[].class.cast(value)));
                        } else {
                            List<Object> values = new ArrayList<>();
                            if (value instanceof LocalDate) {
                                values.add(value);
                            }
                            listVariable.setValue(values);
                        }
                    } else if (variable instanceof DateVariable) {
                        DateVariable dateVariable = (DateVariable) variable;

                        LocalDate localDate = null;
                        if (value instanceof LocalDate) {
                            localDate = (LocalDate) value;
                        } else if (value instanceof Date) {
                            Date date = (Date) value;
                            localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        } else {
                            String newValue = (String) value;
                            if (!newValue.isEmpty()) {
                                try {
                                    localDate = LocalDate.parse(newValue, LiteralType.dateFormat);
                                } catch (Exception e) {


                                    try {
                                        localDate = LocalDate.parse(newValue, LiteralType.defaultFormat);
                                    } catch (Exception ex) {
                                        String message = String.format("Unable to Parse literal %s to Date", newValue);
                                        logger.error(message);
                                        throw new IllegalPropertyValueException(message);
                                    }


                                }

                            }
                        }
                        dateVariable.setValue(localDate);
                    }
                    break;
                }

                case SimpleDateType: {
                    if (variable instanceof ListVariable) {
                        ListVariable listVariable = (ListVariable) variable;
                        if (value instanceof List) {
                            listVariable.setValue((List) value);
                        } else if (value.getClass().isArray()) {
                            listVariable.setValue(Arrays.asList(Date[].class.cast(value)));
                        } else {
                            List<Object> values = new ArrayList<>();
                            if (value instanceof Date) {
                                values.add(value);
                            }
                            listVariable.setValue(values);
                        }
                    } else if (variable instanceof SimpleDateVariable) {
                        SimpleDateVariable dateVariable = (SimpleDateVariable) variable;

                        Date date = null;
                        if (value instanceof LocalDate) {
                            LocalDate localDate = (LocalDate) value;
                            date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                        } else if (value instanceof LocalDateTime) {
                            LocalDateTime localDateTime = (LocalDateTime) value;
                            date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
                        } else if (value instanceof Date) {
                            date = (Date) value;

                        } else {
                            String newValue = (String) value;
                            if (!newValue.isEmpty()) {
                                try {
                                    date = SimpleDateType.dateFormat.parse(newValue);
                                } catch (Exception e) {


                                    try {
                                        date = SimpleDateType.dateTimeFormat.parse(newValue);
                                    } catch (ParseException ex) {
                                        String message = String.format("Unable to Parse literal %s to Date", newValue);
                                        logger.error(message);
                                        throw new IllegalPropertyValueException(message);
                                    }


                                }

                            }
                        }
                        dateVariable.setValue(date);
                    }
                    break;
                }
                case RegexType: {
                    if (variable instanceof RegexVariable) {
                        RegexVariable regexVariable = (RegexVariable) variable;
                        regexVariable.setValue(value.toString());
                    }
                    break;
                }


            }

        }
        return variable;
    }


}
