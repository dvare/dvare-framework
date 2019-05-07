/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016-2019 DVARE (Data Validation and Aggregation Rule Engine)
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
package org.dvare.expression.datatype;

import org.dvare.annotations.OperationMapping;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.IllegalValueException;
import org.dvare.expression.Expression;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.literal.NullLiteral;
import org.dvare.expression.operation.OperationExpression;
import org.dvare.util.DataTypeMapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

public abstract class DataTypeExpression extends Expression {

    protected DataType dataType;

    public DataTypeExpression(DataType dataType) {
        this.dataType = dataType;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }


    public LiteralExpression evaluate(OperationExpression operationExpression,
                                      LiteralExpression left, LiteralExpression right) throws InterpretException {


        try {
            String methodName = getMethodName(operationExpression.getClass());
            if (methodName != null) {
                Method method = this.getClass().getMethod(methodName, LiteralExpression.class, LiteralExpression.class);
                Object result = method.invoke(this, left, right);

                if (result != null) {
                    DataType type = DataTypeMapping.getTypeMapping(result.getClass());

                    if (type == null) {
                        type = LiteralType.computeDataType(result.toString());
                        if (type == null) {
                            type = this.getDataType();
                        }
                    }
                    return LiteralType.getLiteralExpression(result, type);
                }
            }
            return new NullLiteral();

        } catch (Exception m) {
            if (m instanceof InvocationTargetException && m.getCause() != null) {
                throw new InterpretException(m.getCause());
            } else {
                throw new InterpretException(m);
            }
        }
    }


    private String getMethodName(Class operation) {
        for (Method method : this.getClass().getMethods()) {

            if (method.isAnnotationPresent(OperationMapping.class)) {
                Annotation annotation = method.getAnnotation(OperationMapping.class);
                OperationMapping operationMapping = (OperationMapping) annotation;
                if (Arrays.asList(operationMapping.operations()).contains(operation)) {
                    return method.getName();
                }

            }
        }
        return null;
    }


    /*public boolean equal(LiteralExpression left, LiteralExpression right) {
        throw new UnsupportedOperationException();
    }

    public boolean notEqual(LiteralExpression left, LiteralExpression right) {
        throw new UnsupportedOperationException();
    }


    public boolean less(LiteralExpression left, LiteralExpression right) {
        throw new UnsupportedOperationException();
    }


    public boolean lessEqual(LiteralExpression left, LiteralExpression right) {
        throw new UnsupportedOperationException();
    }


    public boolean greater(LiteralExpression left, LiteralExpression right) {
        throw new UnsupportedOperationException();
    }


    public boolean greaterEqual(LiteralExpression left, LiteralExpression right) {
        throw new UnsupportedOperationException();
    }


    public boolean in(LiteralExpression left, LiteralExpression right) {
        throw new UnsupportedOperationException();
    }


    public boolean notIn(LiteralExpression left, LiteralExpression right) {
        throw new UnsupportedOperationException();
    }


    public boolean between(LiteralExpression left, LiteralExpression right) {
        throw new UnsupportedOperationException();
    }*/

    Date toDate(Object value) {

        if (value instanceof LocalDate) {
            LocalDate localDate = (LocalDate) value;
            return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        } else if (value instanceof LocalDateTime) {
            LocalDateTime localDateTime = (LocalDateTime) value;
            return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        } else if (value instanceof Date) {
            return (Date) value;
        }

        try {
            LiteralExpression literalExpression = LiteralType.getLiteralExpression(value.toString());
            if (literalExpression.getType().equals(DateType.class)
                    || literalExpression.getType().equals(DateTimeType.class)
                    || literalExpression.getType().equals(SimpleDateType.class)) {
                return toDate(literalExpression.getValue());
            }
        } catch (IllegalValueException ignored) {

        }
        return null;
    }


    List<Date> buildDateList(List<?> tempValues) {

        List<Date> values = new ArrayList<>();
        for (Object tempValue : tempValues) {
            values.add(toDate(tempValue));
        }
        return values;
    }


    LocalDate toLocalDate(Object value) {
        if (value instanceof LocalDateTime) {
            return LocalDateTime.class.cast(value).toLocalDate();
        } else if (value instanceof LocalDate) {
            return (LocalDate) value;
        } else if (value instanceof Date) {
            return Date.class.cast(value).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
        try {
            LiteralExpression literalExpression = LiteralType.getLiteralExpression(value.toString());
            if (literalExpression.getType().equals(DateType.class)
                    || literalExpression.getType().equals(DateTimeType.class)
                    || literalExpression.getType().equals(SimpleDateType.class)) {
                return toLocalDate(literalExpression.getValue());
            }
        } catch (IllegalValueException ignored) {

        }
        return null;
    }


    List<LocalDate> buildLocalDateList(List<?> objectsList) {
        List<LocalDate> localDateList = new ArrayList<>();
        for (Object object : objectsList) {
            localDateList.add(toLocalDate(object));
        }
        return localDateList;
    }


    LocalDateTime toLocalDateTime(Object value) {
        if (value instanceof LocalDateTime) {
            return LocalDateTime.class.cast(value);
        } else if (value instanceof LocalDate) {
            return LocalDate.class.cast(value).atStartOfDay(ZoneId.systemDefault()).toLocalDateTime();
        } else if (value instanceof Date) {
            return Date.class.cast(value).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        } else {
            try {
                LiteralExpression literalExpression = LiteralType.getLiteralExpression(value.toString());
                if (literalExpression.getType().equals(DateType.class)
                        || literalExpression.getType().equals(DateTimeType.class)
                        || literalExpression.getType().equals(SimpleDateType.class)) {
                    return toLocalDateTime(literalExpression.getValue());
                }
            } catch (IllegalValueException ignored) {

            }
        }
        return null;
    }

    List<LocalDateTime> buildDateTimeList(List<?> objects) {
        List<LocalDateTime> localDateTimeList = new ArrayList<>();
        for (Object object : objects) {

            localDateTimeList.add(toLocalDateTime(object));
        }
        return localDateTimeList;
    }
}