package org.dvare.expression.datatype;

import org.dvare.annotations.OperationMapping;
import org.dvare.annotations.Type;
import org.dvare.expression.ExpressionVisitor;
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

@Type(dataType = DataType.BooleanType)
public class BooleanType extends DataTypeExpression {
    public BooleanType() {
        super(DataType.BooleanType);
    }

    @OperationMapping(operations = {
            Equals.class
    })
    public boolean equal(LiteralExpression<?> left, LiteralExpression<?> right) {
        Boolean leftValue = (Boolean) left.getValue();
        Boolean rightValue = (Boolean) right.getValue();
        return leftValue.equals(rightValue);
    }

    @OperationMapping(operations = {
            NotEquals.class
    })
    public boolean notEqual(LiteralExpression<?> left, LiteralExpression<?> right) {
        Boolean leftValue = (Boolean) left.getValue();
        Boolean rightValue = (Boolean) right.getValue();
        return !leftValue.equals(rightValue);
    }


    @OperationMapping(operations = {
            In.class
    })
    public boolean in(LiteralExpression left, LiteralExpression right) {
        Boolean leftValue = (Boolean) left.getValue();
        List<Boolean> rightValues = buildIntegerBoolean((List<Object>) right.getValue());
        return rightValues.contains(leftValue);
    }

    @OperationMapping(operations = {
            NotIn.class
    })
    public boolean notIn(LiteralExpression left, LiteralExpression right) {

        Boolean leftValue = (Boolean) left.getValue();
        List<Boolean> rightValues = buildIntegerBoolean((List<Object>) right.getValue());
        return !rightValues.contains(leftValue);
    }

    private List<Boolean> buildIntegerBoolean(List<Object> tempValues) {
        List<Boolean> values = new ArrayList<>();
        for (Object tempValue : tempValues) {

            if (tempValue instanceof Boolean) {
                values.add((Boolean) tempValue);
            } else if (tempValue != null) {
                Boolean value = Boolean.parseBoolean(tempValue.toString());
                values.add(value);
            } else {
                values.add(null);
            }
        }
        return values;
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> v) {
        return v.visit(this);
    }
}