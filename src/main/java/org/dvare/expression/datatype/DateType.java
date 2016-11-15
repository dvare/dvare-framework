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


package org.dvare.expression.datatype;

import org.dvare.annotations.OperationMapping;
import org.dvare.annotations.Type;
import org.dvare.expression.literal.LiteralDataType;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.operation.validation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Type(dataType = DataType.DateType)
public class DateType extends DataTypeExpression {
    public DateType() {
        super(DataType.DateType);

    }

    private static Date setTimeToMidnight(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    @OperationMapping(operations = {
            Equals.class
    })
    public boolean equal(LiteralExpression left, LiteralExpression right) {
        Date leftValue = setTimeToMidnight((Date) left.getValue());
        Date rightValue = setTimeToMidnight((Date) right.getValue());
        return leftValue.compareTo(rightValue) == 0;
    }

    @OperationMapping(operations = {
            NotEquals.class
    })
    public boolean notEqual(LiteralExpression left, LiteralExpression right) {
        Date leftValue = setTimeToMidnight((Date) left.getValue());
        Date rightValue = setTimeToMidnight((Date) right.getValue());
        return leftValue.compareTo(rightValue) != 0;
    }

    @OperationMapping(operations = {
            Less.class
    })
    public boolean less(LiteralExpression left, LiteralExpression right) {
        Date leftValue = setTimeToMidnight((Date) left.getValue());
        Date rightValue = setTimeToMidnight((Date) right.getValue());
        return leftValue.compareTo(rightValue) < 0;
    }

    @OperationMapping(operations = {
            LessEqual.class
    })
    public boolean lessEqual(LiteralExpression left, LiteralExpression right) {
        Date leftValue = setTimeToMidnight((Date) left.getValue());
        Date rightValue = setTimeToMidnight((Date) right.getValue());
        return leftValue.compareTo(rightValue) <= 0;
    }

    @OperationMapping(operations = {
            Greater.class
    })
    public boolean greater(LiteralExpression left, LiteralExpression right) {
        Date leftValue = setTimeToMidnight((Date) left.getValue());
        Date rightValue = setTimeToMidnight((Date) right.getValue());
        return leftValue.compareTo(rightValue) > 0;
    }

    @OperationMapping(operations = {
            GreaterEqual.class
    })
    public boolean greaterEqual(LiteralExpression left, LiteralExpression right) {
        Date leftValue = setTimeToMidnight((Date) left.getValue());
        Date rightValue = setTimeToMidnight((Date) right.getValue());
        return leftValue.compareTo(rightValue) >= 0;
    }

    @OperationMapping(operations = {
            In.class
    })
    public boolean in(LiteralExpression left, LiteralExpression right) {
        Date leftValue = setTimeToMidnight((Date) left.getValue());
        List<Date> rightValues = new ArrayList<>();
        for (Date rightValue : buildDateList((List<Object>) right.getValue())) {
            rightValues.add(setTimeToMidnight(rightValue));
        }
        return rightValues.contains(leftValue);
    }

    @OperationMapping(operations = {
            Between.class
    })
    public boolean between(LiteralExpression left, LiteralExpression right) {
        Date leftValue = setTimeToMidnight((Date) left.getValue());
        List<Date> values = buildDateList((List<Object>) right.getValue());
        Date lower = setTimeToMidnight(values.get(0));
        Date upper = setTimeToMidnight(values.get(1));
        if (lower.compareTo(leftValue) <= 0 && leftValue.compareTo(upper) <= 0) {
            return true;
        }
        return false;
    }

    private List<Date> buildDateList(List<Object> tempValues) {
        List<Date> values = new ArrayList<>();
        for (Object tempValue : tempValues) {

            if (tempValue instanceof Date) {
                values.add((Date) tempValue);
            } else {
                try {

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(LiteralDataType.date);
                    Date value = simpleDateFormat.parse(tempValue.toString());
                    values.add(value);
                } catch (ParseException e) {
                    try {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(LiteralDataType.dateTime);
                        Date value = simpleDateFormat.parse(tempValue.toString());
                        values.add(value);
                    } catch (ParseException e2) {
                        try {
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
                            Date value = simpleDateFormat.parse(tempValue.toString());
                            values.add(value);
                        } catch (ParseException e3) {
                            values.add(null);
                        }
                    }

                } catch (NumberFormatException e) {
                    values.add(null);
                } catch (NullPointerException e) {
                    values.add(null);
                }
            }
        }
        return values;
    }
}
