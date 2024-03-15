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
@Type(dataType = DataType.IntegerType)
public class IntegerType extends DataTypeExpression {
    public IntegerType() {
        super(DataType.IntegerType);
    }

    @OperationMapping(operations = {
            Equals.class
    })
    public boolean equal(LiteralExpression<?> left, LiteralExpression<?> right) {
        Integer leftValue = (Integer) left.getValue();

        if (right.getValue() instanceof Float) {
            return leftValue == Math.round((Float) right.getValue());
        } else {
            Integer rightValue = (Integer) right.getValue();
            return leftValue.equals(rightValue);
        }

    }


    @OperationMapping(operations = {
            NotEquals.class
    })
    public boolean notEqual(LiteralExpression<?> left, LiteralExpression<?> right) {
        Integer leftValue = (Integer) left.getValue();


        if (right.getValue() instanceof Float) {
            return leftValue != Math.ceil((Float) right.getValue());
        } else {
            Integer rightValue = (Integer) right.getValue();
            return !leftValue.equals(rightValue);
        }

    }

    @OperationMapping(operations = {
            LessThan.class
    })
    public boolean less(LiteralExpression<?> left, LiteralExpression<?> right) {
        Integer leftValue = (Integer) left.getValue();
        if (right.getValue() instanceof Float) {
            return leftValue < (Float) right.getValue();
        } else {
            Integer rightValue = (Integer) right.getValue();
            return leftValue < rightValue;
        }

    }

    @OperationMapping(operations = {
            LessEqual.class
    })
    public boolean lessEqual(LiteralExpression<?> left, LiteralExpression<?> right) {
        Integer leftValue = (Integer) left.getValue();


        if (right.getValue() instanceof Float) {
            return leftValue <= (Float) right.getValue();
        } else {
            Integer rightValue = (Integer) right.getValue();
            return leftValue <= rightValue;
        }


    }

    @OperationMapping(operations = {
            GreaterThan.class
    })
    public boolean greater(LiteralExpression<?> left, LiteralExpression<?> right) {
        Integer leftValue = (Integer) left.getValue();

        if (right.getValue() instanceof Float) {
            return leftValue > (Float) right.getValue();
        } else {
            Integer rightValue = (Integer) right.getValue();
            return leftValue > rightValue;

        }

    }

    @OperationMapping(operations = {
            GreaterEqual.class
    })
    public boolean greaterEqual(LiteralExpression<?> left, LiteralExpression<?> right) {
        Integer leftValue = (Integer) left.getValue();

        if (right.getValue() instanceof Float) {
            return leftValue >= (Float) right.getValue();
        } else {
            Integer rightValue = (Integer) right.getValue();
            return leftValue >= rightValue;
        }

    }

    @OperationMapping(operations = {
            In.class
    })
    public boolean in(LiteralExpression<?> left, LiteralExpression<?> right) {
        Integer leftValue = (Integer) left.getValue();
        List<Integer> rightValues = buildIntegerList((List<?>) right.getValue());
        return rightValues.contains(leftValue);
    }

    @OperationMapping(operations = {
            NotIn.class
    })
    public boolean notIn(LiteralExpression<?> left, LiteralExpression<?> right) {
        Integer leftValue = (Integer) left.getValue();
        List<Integer> rightValues = buildIntegerList((List<?>) right.getValue());
        return !rightValues.contains(leftValue);
    }

    @OperationMapping(operations = {
            Between.class
    })
    public boolean between(LiteralExpression<?> left, LiteralExpression<?> right) {
        Integer leftValue = (Integer) left.getValue();
        List<Integer> rightValues = buildIntegerList((List<?>) right.getValue());
        Integer lower = rightValues.get(0);
        Integer upper = rightValues.get(1);

        return lower <= leftValue && leftValue <= upper;
    }


    @OperationMapping(operations = {
            org.dvare.expression.operation.aggregation.Sum.class,
            Add.class
    })
    public Integer sum(LiteralExpression<?> left, LiteralExpression<?> right) {
        Integer leftValue = (Integer) left.getValue();
        Integer rightValue = (Integer) right.getValue();
        return leftValue + rightValue;
    }

    @OperationMapping(operations = {
            Subtract.class
    })
    public Integer sub(LiteralExpression<?> left, LiteralExpression<?> right) {
        Integer leftValue = (Integer) left.getValue();
        Integer rightValue = (Integer) right.getValue();
        return leftValue - rightValue;
    }

    @OperationMapping(operations = {
            Multiply.class
    })
    public Integer mul(LiteralExpression<?> left, LiteralExpression<?> right) {
        Integer leftValue = (Integer) left.getValue();
        Integer rightValue = (Integer) right.getValue();
        return leftValue * rightValue;
    }

    @OperationMapping(operations = {
            Divide.class
    })
    public Integer div(LiteralExpression<?> left, LiteralExpression<?> right) {
        Integer leftValue = (Integer) left.getValue();
        Integer rightValue = (Integer) right.getValue();
        return leftValue / rightValue;
    }

    @OperationMapping(operations = {
            Power.class
    })
    public Integer pow(LiteralExpression<?> left, LiteralExpression<?> right) {
        Integer leftValue = (Integer) left.getValue();
        if (right.getValue() instanceof Integer) {
            return (int) Math.pow(leftValue, (Integer) right.getValue());
        } else if (right.getValue() instanceof Float) {
            return (int) Math.pow(leftValue, (Float) right.getValue());
        }
        return null;
    }

    @OperationMapping(operations = {
            Minimum.class,
            Min.class
    })
    public Integer min(LiteralExpression<?> left, LiteralExpression<?> right) {
        Integer leftValue = (Integer) left.getValue();
        Integer rightValue = (Integer) right.getValue();
        return Integer.min((leftValue), rightValue);
    }

    @OperationMapping(operations = {
            Maximum.class,
            Max.class
    })
    public Integer max(LiteralExpression<?> left, LiteralExpression<?> right) {
        Integer leftValue = (Integer) left.getValue();
        Integer rightValue = (Integer) right.getValue();
        return Integer.max((leftValue), rightValue);
    }


    private List<Integer> buildIntegerList(List<?> tempValues) {
        List<Integer> values = new ArrayList<>();
        for (Object tempValue : tempValues) {

            if (tempValue instanceof Integer) {
                values.add((Integer) tempValue);
            } else {
                try {
                    values.add(Integer.parseInt(tempValue.toString()));
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
