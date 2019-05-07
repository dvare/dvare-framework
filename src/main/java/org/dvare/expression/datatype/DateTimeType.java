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
import org.dvare.annotations.Type;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.operation.relational.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
@Type(dataType = DataType.DateTimeType)
public class DateTimeType extends DataTypeExpression {
    public DateTimeType() {
        super(DataType.DateTimeType);

    }

    @OperationMapping(operations = {
            Equals.class
    })
    public boolean equal(LiteralExpression left, LiteralExpression right) {
        LocalDateTime leftValue = toLocalDateTime(left.getValue());
        LocalDateTime rightValue = toLocalDateTime(right.getValue());
        return leftValue.compareTo(rightValue) == 0;
    }

    @OperationMapping(operations = {
            NotEquals.class
    })
    public boolean notEqual(LiteralExpression left, LiteralExpression right) {
        LocalDateTime leftValue = toLocalDateTime(left.getValue());
        LocalDateTime rightValue = toLocalDateTime(right.getValue());
        return leftValue.compareTo(rightValue) != 0;
    }

    @OperationMapping(operations = {
            LessThan.class
    })
    public boolean less(LiteralExpression left, LiteralExpression right) {
        LocalDateTime leftValue = toLocalDateTime(left.getValue());
        LocalDateTime rightValue = toLocalDateTime(right.getValue());
        return leftValue.compareTo(rightValue) < 0;
    }

    @OperationMapping(operations = {
            LessEqual.class
    })

    public boolean lessEqual(LiteralExpression left, LiteralExpression right) {
        LocalDateTime leftValue = toLocalDateTime(left.getValue());
        LocalDateTime rightValue = toLocalDateTime(right.getValue());
        return leftValue.compareTo(rightValue) <= 0;
    }

    @OperationMapping(operations = {
            GreaterThan.class
    })
    public boolean greater(LiteralExpression left, LiteralExpression right) {
        LocalDateTime leftValue = toLocalDateTime(left.getValue());
        LocalDateTime rightValue = toLocalDateTime(right.getValue());
        return leftValue.compareTo(rightValue) > 0;
    }

    @OperationMapping(operations = {
            GreaterEqual.class
    })
    public boolean greaterEqual(LiteralExpression left, LiteralExpression right) {
        LocalDateTime leftValue = toLocalDateTime(left.getValue());
        LocalDateTime rightValue = toLocalDateTime(right.getValue());
        return leftValue.compareTo(rightValue) >= 0;
    }

    @OperationMapping(operations = {
            In.class
    })
    public boolean in(LiteralExpression left, LiteralExpression right) {
        LocalDateTime leftValue = toLocalDateTime(left.getValue());
        List<LocalDateTime> rightValues = buildDateTimeList((List<?>) right.getValue());
        return rightValues.contains(leftValue);
    }

    @OperationMapping(operations = {
            NotIn.class
    })
    public boolean notIn(LiteralExpression left, LiteralExpression right) {
        LocalDateTime leftValue = toLocalDateTime(left.getValue());
        List<LocalDateTime> rightValues = buildDateTimeList((List<?>) right.getValue());
        return !rightValues.contains(leftValue);
    }

    @OperationMapping(operations = {
            Between.class
    })
    public boolean between(LiteralExpression left, LiteralExpression right) {
        LocalDateTime leftValue = toLocalDateTime(left.getValue());
        List<LocalDateTime> values = buildDateTimeList((List<?>) right.getValue());
        LocalDateTime lower = values.get(0);
        LocalDateTime upper = values.get(1);
        return lower.compareTo(leftValue) <= 0 && leftValue.compareTo(upper) <= 0;
    }


}
