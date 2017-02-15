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
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.operation.validation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Type(dataType = DataType.DateTimeType)
public class DateTimeType extends DataTypeExpression {
    public DateTimeType() {
        super(DataType.DateTimeType);

    }

    @OperationMapping(operations = {
            Equals.class
    })
    public boolean equal(LiteralExpression left, LiteralExpression right) {
        LocalDateTime leftValue = (LocalDateTime) left.getValue();
        LocalDateTime rightValue = (LocalDateTime) right.getValue();
        return leftValue.compareTo(rightValue) == 0;
    }

    @OperationMapping(operations = {
            NotEquals.class
    })
    public boolean notEqual(LiteralExpression left, LiteralExpression right) {
        LocalDateTime leftValue = (LocalDateTime) left.getValue();
        LocalDateTime rightValue = (LocalDateTime) right.getValue();
        return leftValue.compareTo(rightValue) != 0;
    }

    @OperationMapping(operations = {
            Less.class
    })
    public boolean less(LiteralExpression left, LiteralExpression right) {
        LocalDateTime leftValue = (LocalDateTime) left.getValue();
        LocalDateTime rightValue = (LocalDateTime) right.getValue();
        return leftValue.compareTo(rightValue) < 0;
    }

    @OperationMapping(operations = {
            LessEqual.class
    })

    public boolean lessEqual(LiteralExpression left, LiteralExpression right) {
        LocalDateTime leftValue = (LocalDateTime) left.getValue();
        LocalDateTime rightValue = (LocalDateTime) right.getValue();
        return leftValue.compareTo(rightValue) <= 0;
    }

    @OperationMapping(operations = {
            Greater.class
    })
    public boolean greater(LiteralExpression left, LiteralExpression right) {
        LocalDateTime leftValue = (LocalDateTime) left.getValue();
        LocalDateTime rightValue = (LocalDateTime) right.getValue();
        return leftValue.compareTo(rightValue) > 0;
    }

    @OperationMapping(operations = {
            GreaterEqual.class
    })
    public boolean greaterEqual(LiteralExpression left, LiteralExpression right) {
        LocalDateTime leftValue = (LocalDateTime) left.getValue();
        LocalDateTime rightValue = (LocalDateTime) right.getValue();
        return leftValue.compareTo(rightValue) >= 0;
    }

    @OperationMapping(operations = {
            In.class
    })
    public boolean in(LiteralExpression left, LiteralExpression right) {
        LocalDateTime leftValue = (LocalDateTime) left.getValue();
        List<LocalDateTime> rightValues = buildDateList((List<?>) right.getValue());
        return rightValues.contains(leftValue);
    }

    @OperationMapping(operations = {
            NotIn.class
    })
    public boolean notIn(LiteralExpression left, LiteralExpression right) {
        LocalDateTime leftValue = (LocalDateTime) left.getValue();
        List<LocalDateTime> rightValues = buildDateList((List<?>) right.getValue());
        return !rightValues.contains(leftValue);
    }

    @OperationMapping(operations = {
            Between.class
    })
    public boolean between(LiteralExpression left, LiteralExpression right) {
        LocalDateTime leftValue = (LocalDateTime) left.getValue();
        List<LocalDateTime> values = buildDateList((List<?>) right.getValue());
        LocalDateTime lower = values.get(0);
        LocalDateTime upper = values.get(1);
        return lower.compareTo(leftValue) <= 0 && leftValue.compareTo(upper) <= 0;
    }


    private List<LocalDateTime> buildDateList(List<?> objects) {
        List<LocalDateTime> localDateTimeList = new ArrayList<>();
        for (Object object : objects) {

            if (object instanceof LocalDateTime) {
                localDateTimeList.add((LocalDateTime) object);
            } else if (object instanceof Date) {
                Date date = (Date) object;
                LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                localDateTimeList.add(localDateTime);
            } else {
                try {

                    LocalDateTime localDateTime = LocalDateTime.parse(object.toString(), LiteralType.dateTimeFormat);
                    localDateTimeList.add(localDateTime);

                } catch (Exception e) {
                    try {
                        LocalDateTime localDateTime = LocalDateTime.parse(object.toString(), LiteralType.dateFormat);
                        localDateTimeList.add(localDateTime);
                    } catch (Exception e2) {
                        try {
                            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("E MMM dd hh:mm:ss Z yyyy");
                            LocalDateTime localDateTime = LocalDateTime.parse(object.toString(), dateTimeFormatter);
                            localDateTimeList.add(localDateTime);
                        } catch (Exception e3) {
                            localDateTimeList.add(null);
                        }
                    }

                }
            }
        }
        return localDateTimeList;
    }
}
