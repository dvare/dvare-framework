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

package org.dvare.expression.datatype;

import org.dvare.annotations.OperationMapping;
import org.dvare.annotations.Type;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.operation.relational.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Type(dataType = DataType.SimpleDateType)
public class SimpleDateType extends DataTypeExpression {
    public final static SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd-MM-yyyy-HH:mm:ss");
    public final static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    public SimpleDateType() {
        super(DataType.SimpleDateType);

    }

    @OperationMapping(operations = {
            Equals.class
    })
    public boolean equal(LiteralExpression left, LiteralExpression right) {
        Date leftValue = toLocalDate(left.getValue());
        Date rightValue = toLocalDate(right.getValue());
        return (leftValue != null && rightValue != null) && leftValue.compareTo(rightValue) == 0;
    }

    @OperationMapping(operations = {
            NotEquals.class
    })
    public boolean notEqual(LiteralExpression left, LiteralExpression right) {
        Date leftValue = toLocalDate(left.getValue());
        Date rightValue = toLocalDate(right.getValue());
        return (leftValue != null && rightValue != null) && leftValue.compareTo(rightValue) != 0;
    }

    @OperationMapping(operations = {
            LessThan.class
    })
    public boolean less(LiteralExpression left, LiteralExpression right) {
        Date leftValue = toLocalDate(left.getValue());
        Date rightValue = toLocalDate(right.getValue());
        return (leftValue != null && rightValue != null) && leftValue.compareTo(rightValue) < 0;
    }

    @OperationMapping(operations = {
            LessEqual.class
    })

    public boolean lessEqual(LiteralExpression left, LiteralExpression right) {
        Date leftValue = toLocalDate(left.getValue());
        Date rightValue = toLocalDate(right.getValue());
        return (leftValue != null && rightValue != null) && leftValue.compareTo(rightValue) <= 0;
    }

    @OperationMapping(operations = {
            GreaterThan.class
    })
    public boolean greater(LiteralExpression left, LiteralExpression right) {
        Date leftValue = toLocalDate(left.getValue());
        Date rightValue = toLocalDate(right.getValue());
        return (leftValue != null && rightValue != null) && leftValue.compareTo(rightValue) > 0;
    }

    @OperationMapping(operations = {
            GreaterEqual.class
    })
    public boolean greaterEqual(LiteralExpression left, LiteralExpression right) {
        Date leftValue = toLocalDate(left.getValue());
        Date rightValue = toLocalDate(right.getValue());
        return (leftValue != null && rightValue != null) && leftValue.compareTo(rightValue) >= 0;
    }

    @OperationMapping(operations = {
            In.class
    })
    public boolean in(LiteralExpression left, LiteralExpression right) {
        Date leftValue = toLocalDate(left.getValue());
        List<Date> rightValues = buildDateList((List<?>) right.getValue());
        return leftValue != null && rightValues.contains(leftValue);
    }

    @OperationMapping(operations = {
            NotIn.class
    })
    public boolean notIn(LiteralExpression left, LiteralExpression right) {
        Date leftValue = toLocalDate(left.getValue());
        List<Date> rightValues = buildDateList((List<?>) right.getValue());
        return leftValue != null && !rightValues.contains(leftValue);
    }

    @OperationMapping(operations = {
            Between.class
    })
    public boolean between(LiteralExpression left, LiteralExpression right) {
        Date leftValue = toLocalDate(left.getValue());
        List<Date> values = buildDateList((List<?>) right.getValue());
        Date lower = values.get(0);
        Date upper = values.get(1);
        return leftValue != null && lower.compareTo(leftValue) <= 0 && leftValue.compareTo(upper) <= 0;
    }

    private Date toLocalDate(Object value) {
        if (value instanceof LocalDate) {
            LocalDate localDate = (LocalDate) value;
            return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());

        } else if (value instanceof LocalDateTime) {
            LocalDateTime localDateTime = (LocalDateTime) value;
            return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());

        } else if (value instanceof Date) {
            return (Date) value;
        }
        return null;
    }

    private List<Date> buildDateList(List<?> tempValues) {
        List<Date> values = new ArrayList<>();
        for (Object tempValue : tempValues) {

            if (tempValue instanceof LocalDate) {
                LocalDate localDate = (LocalDate) tempValue;
                Date date = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
                values.add(date);
            } else if (tempValue instanceof LocalDateTime) {
                LocalDateTime localDateTime = (LocalDateTime) tempValue;
                Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
                values.add(date);
            } else if (tempValue instanceof Date) {
                values.add((Date) tempValue);
            } else {
                try {


                    Date value = dateFormat.parse(tempValue.toString());
                    values.add(value);
                } catch (ParseException e) {
                    try {
                        Date value = dateTimeFormat.parse(tempValue.toString());
                        values.add(value);
                    } catch (ParseException e2) {
                        try {
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E MMM dd hh:mm:ss Z yyyy");
                            Date value = simpleDateFormat.parse(tempValue.toString());
                            values.add(value);
                        } catch (ParseException e3) {
                            values.add(null);
                        }
                    }

                } catch (Exception e) {
                    values.add(null);
                }
            }
        }
        return values;
    }
}