package org.dvare.expression.datatype;


import org.dvare.annotations.OperationMapping;
import org.dvare.annotations.Type;
import org.dvare.expression.ExpressionVisitor;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.operation.aggregation.Maximum;
import org.dvare.expression.operation.aggregation.Minimum;
import org.dvare.expression.operation.arithmetic.*;
import org.dvare.expression.operation.relational.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
@Type(dataType = DataType.FloatType)
public class FloatType extends DataTypeExpression {
    public FloatType() {
        super(DataType.FloatType);

    }

    @OperationMapping(operations = {
            Equals.class
    })
    public boolean equal(LiteralExpression<?> left, LiteralExpression<?> right) {
        Float leftValue = (Float) left.getValue();

        if (right.getValue() instanceof Integer) {
            return Math.ceil(leftValue) == (Integer) right.getValue();
        } else {
            Float rightValue = (Float) right.getValue();
            return leftValue.compareTo(rightValue) == 0;
        }

    }

    @OperationMapping(operations = {
            NotEquals.class
    })
    public boolean notEqual(LiteralExpression<?> left, LiteralExpression<?> right) {
        Float leftValue = (Float) left.getValue();
        if (right.getValue() instanceof Integer) {
            return Math.ceil(leftValue) != (Integer) right.getValue();
        } else {
            Float rightValue = (Float) right.getValue();
            return leftValue.compareTo(rightValue) != 0;
        }

    }

    @OperationMapping(operations = {
            LessThan.class
    })
    public boolean less(LiteralExpression<?> left, LiteralExpression<?> right) {
        Float leftValue = (Float) left.getValue();


        if (right.getValue() instanceof Integer) {
            return leftValue < (Integer) right.getValue();
        } else {
            Float rightValue = (Float) right.getValue();
            return leftValue < rightValue;
        }


    }

    @OperationMapping(operations = {
            LessEqual.class
    })
    public boolean lessEqual(LiteralExpression<?> left, LiteralExpression<?> right) {
        Float leftValue = (Float) left.getValue();


        if (right.getValue() instanceof Integer) {
            return leftValue < (Integer) right.getValue();
        } else {
            Float rightValue = (Float) right.getValue();
            return leftValue <= rightValue;
        }

    }

    @OperationMapping(operations = {
            GreaterThan.class
    })
    public boolean greater(LiteralExpression<?> left, LiteralExpression<?> right) {
        Float leftValue = (Float) left.getValue();


        if (right.getValue() instanceof Integer) {
            return leftValue > (Integer) right.getValue();
        } else {
            Float rightValue = (Float) right.getValue();
            return leftValue > rightValue;
        }

    }

    @OperationMapping(operations = {
            GreaterEqual.class
    })
    public boolean greaterEqual(LiteralExpression<?> left, LiteralExpression<?> right) {
        Float leftValue = (Float) left.getValue();

        if (right.getValue() instanceof Integer) {
            return leftValue >= (Integer) right.getValue();
        } else {
            Float rightValue = (Float) right.getValue();
            return leftValue >= rightValue;
        }

    }

    @OperationMapping(operations = {
            In.class
    })
    public boolean in(LiteralExpression<?> left, LiteralExpression<?> right) {
        Float leftValue = (Float) left.getValue();
        List<Float> rightValues = buildFloatList((List<Object>) right.getValue());
        return rightValues.contains(leftValue);
    }

    @OperationMapping(operations = {
            NotIn.class
    })
    public boolean notIn(LiteralExpression<?> left, LiteralExpression<?> right) {
        Float leftValue = (Float) left.getValue();
        List<Float> rightValues = buildFloatList((List<Object>) right.getValue());
        return !rightValues.contains(leftValue);
    }

    @OperationMapping(operations = {
            Between.class
    })
    public boolean between(LiteralExpression<?> left, LiteralExpression<?> right) {
        Float leftValue = (Float) left.getValue();
        List<Float> rightValues = buildFloatList((List<Object>) right.getValue());
        Float lower = rightValues.get(0);
        Float upper = rightValues.get(1);

        return lower <= leftValue && leftValue <= upper;
    }

    @OperationMapping(operations = {
            org.dvare.expression.operation.aggregation.Sum.class,
            Add.class
    })
    public Float sum(LiteralExpression<?> left, LiteralExpression<?> right) {
        Float leftValue = (Float) left.getValue();
        Float rightValue = (Float) right.getValue();
        return leftValue + rightValue;
    }

    @OperationMapping(operations = {
            Subtract.class
    })
    public Float sub(LiteralExpression<?> left, LiteralExpression<?> right) {
        Float leftValue = (Float) left.getValue();
        Float rightValue = (Float) right.getValue();
        return leftValue - rightValue;
    }

    @OperationMapping(operations = {
            Multiply.class
    })
    public Float mul(LiteralExpression<?> left, LiteralExpression<?> right) {
        Float leftValue = (Float) left.getValue();
        Float rightValue = (Float) right.getValue();
        return leftValue * rightValue;
    }

    @OperationMapping(operations = {
            Divide.class
    })
    public Float div(LiteralExpression<?> left, LiteralExpression<?> right) {
        Float leftValue = (Float) left.getValue();
        Float rightValue = (Float) right.getValue();
        return leftValue / rightValue;
    }

    @OperationMapping(operations = {
            Power.class
    })
    public Float pow(LiteralExpression<?> left, LiteralExpression<?> right) {
        Float leftValue = (Float) left.getValue();
        if (right.getValue() instanceof Integer) {
            return (float) Math.pow(leftValue, (Integer) right.getValue());
        } else if (right.getValue() instanceof Float) {
            return (float) Math.pow(leftValue, (Float) right.getValue());
        }
        return null;
    }

    @OperationMapping(operations = {
            Minimum.class,
            Min.class
    })
    public Float min(LiteralExpression<?> left, LiteralExpression<?> right) {
        Float leftValue = (Float) left.getValue();
        Float rightValue = (Float) right.getValue();
        return Float.min((leftValue), rightValue);
    }

    @OperationMapping(operations = {
            Maximum.class,
            Max.class
    })
    public Float max(LiteralExpression<?> left, LiteralExpression<?> right) {

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
                    values.add(Float.parseFloat(tempValue.toString()));
                } catch (NumberFormatException | NullPointerException e) {
                    values.add(null);
                }
            }
        }
        return values;
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> v) {
        return v.visit(this);
    }
}


