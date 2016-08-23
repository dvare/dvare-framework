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


package com.dvare.expression.datatype;

import com.dvare.annotations.Type;
import com.dvare.annotations.TypeOperation;
import com.dvare.expression.literal.LiteralExpression;

import java.util.Date;
import java.util.List;

@Type(dataType = DataType.DateTimeType)
public class DateTimeType extends DataTypeExpression {
    public DateTimeType() {
        super(DataType.DateTimeType);

    }

    @TypeOperation(operations = {
            com.dvare.expression.operation.validation.Equals.class
    })
    public boolean equal(LiteralExpression left, LiteralExpression right) {
        Date leftValue = (Date) left.getValue();
        Date rightValue = (Date) right.getValue();
        return leftValue.compareTo(rightValue) == 0;
    }

    @TypeOperation(operations = {
            com.dvare.expression.operation.validation.NotEquals.class
    })
    public boolean notEqual(LiteralExpression left, LiteralExpression right) {
        Date leftValue = (Date) left.getValue();
        Date rightValue = (Date) right.getValue();
        return leftValue.compareTo(rightValue) != 0;
    }

    @TypeOperation(operations = {
            com.dvare.expression.operation.validation.Less.class
    })
    public boolean less(LiteralExpression left, LiteralExpression right) {
        Date leftValue = (Date) left.getValue();
        Date rightValue = (Date) right.getValue();
        return leftValue.compareTo(rightValue) < 0;
    }

    @TypeOperation(operations = {
            com.dvare.expression.operation.validation.LessEqual.class
    })

    public boolean lessEqual(LiteralExpression left, LiteralExpression right) {
        Date leftValue = (Date) left.getValue();
        Date rightValue = (Date) right.getValue();
        return leftValue.compareTo(rightValue) <= 0;
    }

    @TypeOperation(operations = {
            com.dvare.expression.operation.validation.Greater.class
    })
    public boolean greater(LiteralExpression left, LiteralExpression right) {
        Date leftValue = (Date) left.getValue();
        Date rightValue = (Date) right.getValue();
        return leftValue.compareTo(rightValue) > 0;
    }

    @TypeOperation(operations = {
            com.dvare.expression.operation.validation.GreaterEqual.class
    })
    public boolean greaterEqual(LiteralExpression left, LiteralExpression right) {
        Date leftValue = (Date) left.getValue();
        Date rightValue = (Date) right.getValue();
        return leftValue.compareTo(rightValue) >= 0;
    }

    @TypeOperation(operations = {
            com.dvare.expression.operation.validation.In.class
    })
    public boolean in(LiteralExpression left, LiteralExpression right) {
        Date leftValue = (Date) left.getValue();
        List<Date> values = (List<Date>) right.getValue();
        for (Date rightValue : values) {
            if (leftValue.compareTo(rightValue) == 0) {
                return true;
            }
        }
        return false;
    }

    @TypeOperation(operations = {
            com.dvare.expression.operation.validation.Between.class
    })
    public boolean between(LiteralExpression left, LiteralExpression right) {
        Date leftValue = (Date) left.getValue();
        List<Date> values = (List<Date>) right.getValue();
        Date lower = values.get(0);
        Date upper = values.get(1);

        if (lower.compareTo(leftValue) <= 0 && leftValue.compareTo(upper) <= 0) {
            {
                return true;
            }
        }
        return false;
    }


}
