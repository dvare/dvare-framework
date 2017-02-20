/*The MIT License (MIT)

Copyright (c) 2016-2017 Muhammad Hammad

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
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.operation.validation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Type(dataType = DataType.DateType)
public class DateType extends DataTypeExpression {
    public DateType() {
        super(DataType.DateType);

    }


    @OperationMapping(operations = {
            Equals.class
    })
    public boolean equal(LiteralExpression left, LiteralExpression right) {

        LocalDate leftValue = toLocalDate(left.getValue());
        LocalDate rightValue = toLocalDate(right.getValue());
        return (leftValue != null && rightValue != null) && leftValue.compareTo(rightValue) == 0;
    }

    @OperationMapping(operations = {
            NotEquals.class
    })
    public boolean notEqual(LiteralExpression left, LiteralExpression right) {
        LocalDate leftValue = toLocalDate(left.getValue());
        LocalDate rightValue = toLocalDate(right.getValue());
        return (leftValue != null && rightValue != null) && leftValue.compareTo(rightValue) != 0;
    }

    @OperationMapping(operations = {
            LessThen.class
    })
    public boolean less(LiteralExpression left, LiteralExpression right) {
        LocalDate leftValue = toLocalDate(left.getValue());
        LocalDate rightValue = toLocalDate(right.getValue());
        return (leftValue != null && rightValue != null) && leftValue.compareTo(rightValue) < 0;
    }

    @OperationMapping(operations = {
            LessEqual.class
    })
    public boolean lessEqual(LiteralExpression left, LiteralExpression right) {
        LocalDate leftValue = toLocalDate(left.getValue());
        LocalDate rightValue = toLocalDate(right.getValue());
        return (leftValue != null && rightValue != null) && leftValue.compareTo(rightValue) <= 0;
    }

    @OperationMapping(operations = {
            GreaterThen.class
    })
    public boolean greater(LiteralExpression left, LiteralExpression right) {
        LocalDate leftValue = toLocalDate(left.getValue());
        LocalDate rightValue = toLocalDate(right.getValue());
        return (leftValue != null && rightValue != null) && leftValue.compareTo(rightValue) > 0;
    }

    @OperationMapping(operations = {
            GreaterEqual.class
    })
    public boolean greaterEqual(LiteralExpression left, LiteralExpression right) {
        LocalDate leftValue = toLocalDate(left.getValue());
        LocalDate rightValue = toLocalDate(right.getValue());
        return (leftValue != null && rightValue != null) && leftValue.compareTo(rightValue) >= 0;
    }

    @OperationMapping(operations = {
            In.class
    })
    public boolean in(LiteralExpression left, LiteralExpression right) {
        LocalDate leftValue = toLocalDate(left.getValue());
        List<LocalDate> rightValues = new ArrayList<>();

        for (LocalDate rightValue : buildLocalDateList((List<?>) right.getValue())) {
            rightValues.add(rightValue);
        }

        return rightValues.contains(leftValue);
    }

    @OperationMapping(operations = {
            NotIn.class
    })
    public boolean notIn(LiteralExpression left, LiteralExpression right) {
        LocalDate leftValue = toLocalDate(left.getValue());
        List<LocalDate> rightValues = new ArrayList<>();
        for (LocalDate rightValue : buildLocalDateList((List<?>) right.getValue())) {
            rightValues.add(rightValue);
        }
        return !rightValues.contains(leftValue);
    }

    @OperationMapping(operations = {
            Between.class
    })
    public boolean between(LiteralExpression left, LiteralExpression right) {
        LocalDate leftValue = toLocalDate(left.getValue());
        List<LocalDate> values = buildLocalDateList((List<?>) right.getValue());
        LocalDate lower = values.get(0);
        LocalDate upper = values.get(1);
        return lower.compareTo(leftValue) <= 0 && leftValue.compareTo(upper) <= 0;
    }


    private LocalDate toLocalDate(Object value) {
        if (value instanceof LocalDateTime) {
            LocalDateTime localDateTime = (LocalDateTime) value;
            return localDateTime.toLocalDate();

        } else if (value instanceof LocalDate) {
            return (LocalDate) value;
        }
        return null;
    }

    private List<LocalDate> buildLocalDateList(List<?> objectsList) {
        List<LocalDate> localDateList = new ArrayList<>();
        for (Object object : objectsList) {

            if (object == null) {
                localDateList.add(null);

            } else if (object instanceof LocalDate) {
                localDateList.add((LocalDate) object);
            } else if (object instanceof Date) {
                Date date = (Date) object;
                LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                localDateList.add(localDate);
            } else {
                try {
                    LocalDate localDate = LocalDate.parse(object.toString(), LiteralType.dateFormat);
                    localDateList.add(localDate);
                } catch (Exception e) {
                    try {

                        LocalDate localDate = LocalDate.parse(object.toString(), LiteralType.dateTimeFormat);
                        localDateList.add(localDate);


                    } catch (Exception e2) {
                        try {
                            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("E MMM dd hh:mm:ss Z yyyy");
                            LocalDate localDate = LocalDate.parse(object.toString(), dateTimeFormatter);
                            localDateList.add(localDate);
                        } catch (Exception e3) {
                            localDateList.add(null);
                        }
                    }

                }
            }
        }
        return localDateList;
    }
}
