package org.dvare.expression.veriable;


import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
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

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */

public class VariableType {
    private static final Logger logger = LoggerFactory.getLogger(VariableType.class);


    public static VariableExpression<?> getVariableExpression(String name, DataType type, String operandType) throws IllegalPropertyException {
        VariableExpression<?> variableExpression = getVariableExpression(name, type);
        variableExpression.setOperandType(operandType);
        return variableExpression;
    }


    public static VariableExpression<?> getVariableExpression(String name, DataType type) throws IllegalPropertyException {
        VariableExpression<?> variableExpression;

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

            case TripleType: {
                variableExpression = new TripleVariable(name);
                break;
            }

            case TripleListType: {
                variableExpression = new ListVariable(name, TripleType.class);
                break;
            }

            case RegexType: {
                variableExpression = new StringVariable(name);
                break;
            }
            default: {
                String message = String.format("Type of Variable Expression %s not found", name);
                logger.error(message);
                throw new IllegalPropertyException(message);
            }
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Variable  Expression Variable: {} [{}]", variableExpression.getClass().getSimpleName(), variableExpression.getName());
        }
        return variableExpression;
    }


    public static VariableExpression<?> setVariableValue(VariableExpression<?> variable, Object object) throws IllegalPropertyValueException {
        Object value = ValueFinder.findValue(variable.getName(), object);
        return setValue(variable, value);
    }

    private static VariableExpression<?> setValue(VariableExpression<?> variable, Object value) throws IllegalPropertyValueException {

        if (variable == null) {
            return null;
        }

        if (value == null) {
            // for reuse rule don't remove
            variable.setValue(null);
            return variable;
        }

        if (variable.getType().isAnnotationPresent(Type.class)) {
            Type type = variable.getType().getAnnotation(Type.class);
            DataType dataType = type.dataType();
            switch (dataType) {
                case BooleanType: {
                    setBooleanValue(variable, value);
                    break;
                }
                case FloatType: {
                    setFloatValue(variable, value);
                    break;
                }
                case IntegerType: {
                    setIntegerValue(variable, value);
                    break;
                }
                case StringType: {
                    setStringValue(variable, value);
                    break;
                }
                case PairType: {
                    setPaitTypeValue(variable, value);
                    break;
                }
                case TripleType: {
                    setTripleTypeValue(variable, value);
                    break;
                }
                case DateTimeType: {
                    setDateTimeValue(variable, value);

                    break;
                }
                case DateType: {
                    setDAteTypeValue(variable, value);
                    break;
                }

                case SimpleDateType: {
                    setSimpleDateTypeValue(variable, value);
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

    private static void setBooleanValue(VariableExpression<?> variable, Object value) {
        if (variable instanceof ListVariable) {
            ListVariable listVariable = (ListVariable) variable;
            if (value instanceof List) {
                listVariable.setValue((List<?>) value);
            } else if (value.getClass().isArray()) {
                listVariable.setValue(Arrays.asList((Boolean[]) value));
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
    }

    private static void setFloatValue(VariableExpression<?> variable, Object value) throws IllegalPropertyValueException {
        if (variable instanceof ListVariable) {
            ListVariable listVariable = (ListVariable) variable;
            if (value instanceof List) {
                listVariable.setValue((List<?>) value);
            } else if (value.getClass().isArray()) {
                listVariable.setValue(Arrays.asList((Float[]) value));
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
    }

    private static void setIntegerValue(VariableExpression<?> variable, Object value) throws IllegalPropertyValueException {
        if (variable instanceof ListVariable) {
            ListVariable listVariable = (ListVariable) variable;
            if (value instanceof List) {
                listVariable.setValue((List<?>) value);
            } else if (value.getClass().isArray()) {
                listVariable.setValue(Arrays.asList((Integer[]) value));
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
    }

    private static void setStringValue(VariableExpression<?> variable, Object value) {
        if (variable instanceof ListVariable) {
            ListVariable listVariable = (ListVariable) variable;
            if (value instanceof List) {
                listVariable.setValue((List<?>) value);
            } else if (value.getClass().isArray()) {
                listVariable.setValue(Arrays.asList((String[]) value));
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
    }

    private static void setPaitTypeValue(VariableExpression<?> variable, Object value) {
        if (variable instanceof ListVariable) {
            ListVariable listVariable = (ListVariable) variable;
            if (value instanceof List) {
                listVariable.setValue((List<?>) value);
            } else if (value.getClass().isArray()) {
                listVariable.setValue(Arrays.asList((Pair<?, ?>[]) value));
            } else {
                List<Object> values = new ArrayList<>();
                values.add(value);
                listVariable.setValue(values);
            }
        } else if (variable instanceof PairVariable) {
            PairVariable pairVariable = (PairVariable) variable;
            if (value instanceof Pair) {
                pairVariable.setValue((Pair<?, ?>) value);
            }
        }
    }

    private static void setTripleTypeValue(VariableExpression<?> variable, Object value) {
        if (variable instanceof ListVariable) {
            ListVariable listVariable = (ListVariable) variable;
            if (value instanceof List) {
                listVariable.setValue((List<?>) value);
            } else if (value.getClass().isArray()) {
                listVariable.setValue(Arrays.asList((Triple<?, ?, ?>[]) value));
            } else {
                List<Object> values = new ArrayList<>();
                values.add(value);
                listVariable.setValue(values);
            }
        } else if (variable instanceof TripleVariable) {
            TripleVariable tripleVariable = (TripleVariable) variable;
            if (value instanceof Triple) {
                tripleVariable.setValue((Triple<?, ?, ?>) value);
            }
        }
    }

    private static void setDateTimeValue(VariableExpression<?> variable, Object value) throws IllegalPropertyValueException {
        if (variable instanceof ListVariable) {
            ListVariable listVariable = (ListVariable) variable;
            if (value instanceof List) {
                listVariable.setValue((List<?>) value);
            } else if (value.getClass().isArray()) {
                listVariable.setValue(Arrays.asList((LocalDateTime[]) value));
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
    }

    private static void setDAteTypeValue(VariableExpression<?> variable, Object value) throws IllegalPropertyValueException {
        if (variable instanceof ListVariable) {
            ListVariable listVariable = (ListVariable) variable;
            if (value instanceof List) {
                listVariable.setValue((List<?>) value);
            } else if (value.getClass().isArray()) {
                listVariable.setValue(Arrays.asList((LocalDate[]) value));
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
    }

    private static void setSimpleDateTypeValue(VariableExpression<?> variable, Object value) throws IllegalPropertyValueException {
        if (variable instanceof ListVariable) {
            ListVariable listVariable = (ListVariable) variable;
            if (value instanceof List) {
                listVariable.setValue((List<?>) value);
            } else if (value.getClass().isArray()) {
                listVariable.setValue(Arrays.asList((Date[]) value));
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
                        date = LiteralType.simpleDateFormat.parse(newValue);
                    } catch (Exception e) {


                        try {
                            date = LiteralType.simpleDateTimeFormat.parse(newValue);
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
    }


}
