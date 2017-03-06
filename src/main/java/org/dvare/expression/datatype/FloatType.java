/*The MIT License (MIT)

Copyright (c) 2016-2017 DVARE (Data Validation and Aggregation Rule Engine)

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
import org.dvare.expression.operation.aggregation.Maximum;
import org.dvare.expression.operation.aggregation.Minimum;
import org.dvare.expression.operation.arithmetic.*;
import org.dvare.expression.operation.validation.*;

import java.util.ArrayList;
import java.util.List;

@Type(dataType = DataType.FloatType)
public class FloatType extends DataTypeExpression {
    public FloatType() {
        super(DataType.FloatType);

    }

    @OperationMapping(operations = {
            Equals.class
    })
    public boolean equal(LiteralExpression left, LiteralExpression right) {
        Float leftValue = (Float) left.getValue();
        Float rightValue = (Float) right.getValue();
        return leftValue.compareTo(rightValue) == 0;
    }

    @OperationMapping(operations = {
            NotEquals.class
    })
    public boolean notEqual(LiteralExpression left, LiteralExpression right) {
        Float leftValue = (Float) left.getValue();
        Float rightValue = (Float) right.getValue();
        return leftValue.compareTo(rightValue) != 0;
    }

    @OperationMapping(operations = {
            LessThen.class
    })
    public boolean less(LiteralExpression left, LiteralExpression right) {
        Float leftValue = (Float) left.getValue();
        Float rightValue = (Float) right.getValue();
        return leftValue < rightValue;
    }

    @OperationMapping(operations = {
            LessEqual.class
    })
    public boolean lessEqual(LiteralExpression left, LiteralExpression right) {
        Float leftValue = (Float) left.getValue();
        Float rightValue = (Float) right.getValue();
        return leftValue <= rightValue;
    }

    @OperationMapping(operations = {
            GreaterThen.class
    })
    public boolean greater(LiteralExpression left, LiteralExpression right) {
        Float leftValue = (Float) left.getValue();
        Float rightValue = (Float) right.getValue();
        return leftValue > rightValue;
    }

    @OperationMapping(operations = {
            GreaterEqual.class
    })
    public boolean greaterEqual(LiteralExpression left, LiteralExpression right) {
        Float leftValue = (Float) left.getValue();
        Float rightValue = (Float) right.getValue();
        return leftValue >= rightValue;
    }

    @OperationMapping(operations = {
            In.class
    })
    public boolean in(LiteralExpression left, LiteralExpression right) {
        Float leftValue = (Float) left.getValue();
        List<Float> rightValues = buildFloatList((List<Object>) right.getValue());
        return rightValues.contains(leftValue);
    }

    @OperationMapping(operations = {
            NotIn.class
    })
    public boolean notIn(LiteralExpression left, LiteralExpression right) {
        Float leftValue = (Float) left.getValue();
        List<Float> rightValues = buildFloatList((List<Object>) right.getValue());
        return !rightValues.contains(leftValue);
    }

    @OperationMapping(operations = {
            Between.class
    })
    public boolean between(LiteralExpression left, LiteralExpression right) {
        Float leftValue = (Float) left.getValue();
        List<Float> rightValues = buildFloatList((List<Object>) right.getValue());
        Float lower = rightValues.get(0);
        Float upper = rightValues.get(1);

        if (lower <= leftValue && leftValue <= upper) {
            {
                return true;
            }
        }
        return false;
    }

    @OperationMapping(operations = {
            org.dvare.expression.operation.aggregation.Sum.class,
            Add.class
    })
    public Float sum(LiteralExpression left, LiteralExpression right) {
        Float leftValue = (Float) left.getValue();
        Float rightValue = (Float) right.getValue();
        return leftValue + rightValue;
    }

    @OperationMapping(operations = {
            Subtract.class
    })
    public Float sub(LiteralExpression left, LiteralExpression right) {
        Float leftValue = (Float) left.getValue();
        Float rightValue = (Float) right.getValue();
        return leftValue - rightValue;
    }

    @OperationMapping(operations = {
            Multiply.class
    })
    public Float mul(LiteralExpression left, LiteralExpression right) {
        Float leftValue = (Float) left.getValue();
        Float rightValue = (Float) right.getValue();
        return leftValue * rightValue;
    }

    @OperationMapping(operations = {
            Divide.class
    })
    public Float div(LiteralExpression left, LiteralExpression right) {
        Float leftValue = (Float) left.getValue();
        Float rightValue = (Float) right.getValue();
        return leftValue / rightValue;
    }

    @OperationMapping(operations = {
            Power.class
    })
    public Float pow(LiteralExpression left, LiteralExpression right) {
        Float leftValue = (Float) left.getValue();
        if (right.getValue() instanceof Integer) {
            return new Float(Math.pow(leftValue, (Integer) right.getValue()));
        } else if (right.getValue() instanceof Float) {
            return new Float(Math.pow(leftValue, (Float) right.getValue()));
        }
        return null;
    }

    @OperationMapping(operations = {
            Minimum.class,
            Min.class
    })
    public Float min(LiteralExpression left, LiteralExpression right) {
        Float leftValue = (Float) left.getValue();
        Float rightValue = (Float) right.getValue();
        return Float.min((leftValue), rightValue);
    }

    @OperationMapping(operations = {
            Maximum.class,
            Max.class
    })
    public Float max(LiteralExpression left, LiteralExpression right) {

        Float leftValue = (Float) left.getValue();
        Float rightValue = (Float) right.getValue();
        return Float.max((leftValue), rightValue);
    }


    private List<Float> buildFloatList(List<Object> tempValues) {
        List<Float> values = new ArrayList<>();
        for (Object tempValue : tempValues) {

            if (tempValue instanceof Float) {
                values.add((Float) tempValue);
            } else {
                try {
                    Float value = Float.parseFloat(tempValue.toString());
                    values.add(value);
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


