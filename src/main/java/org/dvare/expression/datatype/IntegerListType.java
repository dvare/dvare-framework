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
import org.dvare.expression.operation.relational.Equals;
import org.dvare.expression.operation.relational.In;
import org.dvare.expression.operation.relational.NotEquals;
import org.dvare.expression.operation.relational.NotIn;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
@Type(dataType = DataType.IntegerListType)
public class IntegerListType extends ListType {
    public IntegerListType() {

        super(DataType.IntegerListType);
    }

    @OperationMapping(operations = {
            Equals.class
    })
    public boolean equal(LiteralExpression left, LiteralExpression right) {
        List<Integer> leftValues = buildIntegerList((List<?>) left.getValue());
        List<Integer> rightValues = buildIntegerList((List<?>) right.getValue());
        return leftValues.equals(rightValues);
    }


    @OperationMapping(operations = {
            NotEquals.class
    })
    public boolean notEqual(LiteralExpression left, LiteralExpression right) {
        List<Integer> leftValues = buildIntegerList((List<?>) left.getValue());
        List<Integer> rightValues = buildIntegerList((List<?>) right.getValue());
        return !leftValues.equals(rightValues);

    }


    @OperationMapping(operations = {
            In.class
    })
    public boolean in(LiteralExpression left, LiteralExpression right) {
        List<Integer> leftValues = buildIntegerList((List<?>) left.getValue());
        List<Integer> rightValues = buildIntegerList((List<?>) right.getValue());
        return rightValues.containsAll(leftValues);
    }

    @OperationMapping(operations = {
            NotIn.class
    })
    public boolean notIn(LiteralExpression left, LiteralExpression right) {
        List<Integer> leftValues = buildIntegerList((List<?>) left.getValue());
        List<Integer> rightValues = buildIntegerList((List<?>) right.getValue());
        return !rightValues.containsAll(leftValues);
    }


    private List<Integer> buildIntegerList(List<?> tempValues) {
        List<Integer> values = new ArrayList<>();
        for (Object tempValue : tempValues) {

            if (tempValue instanceof Integer) {
                values.add((Integer) tempValue);
            } else {
                try {
                    Integer value = Integer.parseInt(tempValue.toString());
                    values.add(value);
                } catch (NumberFormatException | NullPointerException e) {
                    values.add(null);
                }
            }
        }
        return values;
    }
}
