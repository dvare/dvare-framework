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
import org.dvare.expression.operation.arithmetic.Subtract;
import org.dvare.expression.operation.relational.*;

import java.util.Date;
import java.util.List;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
@Type(dataType = DataType.SimpleDateType)
public class SimpleDateType extends DataTypeExpression {


    public SimpleDateType() {
        super(DataType.SimpleDateType);

    }

    @OperationMapping(operations = {
            Equals.class
    })
    public boolean equal(LiteralExpression left, LiteralExpression right) {
        Date leftValue = toDate(left.getValue());
        Date rightValue = toDate(right.getValue());
        return (leftValue != null && rightValue != null) && leftValue.compareTo(rightValue) == 0;
    }

    @OperationMapping(operations = {
            NotEquals.class
    })
    public boolean notEqual(LiteralExpression left, LiteralExpression right) {
        Date leftValue = toDate(left.getValue());
        Date rightValue = toDate(right.getValue());
        return (leftValue != null && rightValue != null) && leftValue.compareTo(rightValue) != 0;
    }

    @OperationMapping(operations = {
            LessThan.class
    })
    public boolean less(LiteralExpression left, LiteralExpression right) {
        Date leftValue = toDate(left.getValue());
        Date rightValue = toDate(right.getValue());
        return (leftValue != null && rightValue != null) && leftValue.compareTo(rightValue) < 0;
    }

    @OperationMapping(operations = {
            LessEqual.class
    })

    public boolean lessEqual(LiteralExpression left, LiteralExpression right) {
        Date leftValue = toDate(left.getValue());
        Date rightValue = toDate(right.getValue());
        return (leftValue != null && rightValue != null) && leftValue.compareTo(rightValue) <= 0;
    }

    @OperationMapping(operations = {
            GreaterThan.class
    })
    public boolean greater(LiteralExpression left, LiteralExpression right) {
        Date leftValue = toDate(left.getValue());
        Date rightValue = toDate(right.getValue());
        return (leftValue != null && rightValue != null) && leftValue.compareTo(rightValue) > 0;
    }

    @OperationMapping(operations = {
            GreaterEqual.class
    })
    public boolean greaterEqual(LiteralExpression left, LiteralExpression right) {
        Date leftValue = toDate(left.getValue());
        Date rightValue = toDate(right.getValue());
        return (leftValue != null && rightValue != null) && leftValue.compareTo(rightValue) >= 0;
    }

    @OperationMapping(operations = {
            In.class
    })
    public boolean in(LiteralExpression left, LiteralExpression right) {
        Date leftValue = toDate(left.getValue());
        List<Date> rightValues = buildDateList((List<?>) right.getValue());
        return leftValue != null && rightValues.contains(leftValue);
    }

    @OperationMapping(operations = {
            NotIn.class
    })
    public boolean notIn(LiteralExpression left, LiteralExpression right) {
        Date leftValue = toDate(left.getValue());
        List<Date> rightValues = buildDateList((List<?>) right.getValue());
        return leftValue != null && !rightValues.contains(leftValue);
    }

    @OperationMapping(operations = {
            Between.class
    })
    public boolean between(LiteralExpression left, LiteralExpression right) {
        Date leftValue = toDate(left.getValue());
        List<Date> values = buildDateList((List<?>) right.getValue());
        Date lower = values.get(0);
        Date upper = values.get(1);

        return leftValue != null && lower.compareTo(leftValue) <= 0 && leftValue.compareTo(upper) <= 0;
    }


    @OperationMapping(operations = {
            Subtract.class
    })
    public Date sub(LiteralExpression left, LiteralExpression right) {
        Date leftValue = toDate(left.getValue());
        Date rightValue = toDate(right.getValue());
        return new Date(rightValue.getTime() - leftValue.getTime());
    }


}