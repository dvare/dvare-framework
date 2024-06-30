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
@Type(dataType = DataType.FloatListType)
public class FloatListType extends ListType {
    public FloatListType() {

        super(DataType.FloatListType);
    }

    @OperationMapping(operations = {
            Equals.class
    })
    public boolean equal(LiteralExpression<?> left, LiteralExpression<?> right) {
        List<Float> leftValues = buildFloatList((List<?>) left.getValue());
        List<Float> rightValues = buildFloatList((List<?>) right.getValue());
        return leftValues.equals(rightValues);
    }


    @OperationMapping(operations = {
            NotEquals.class
    })
    public boolean notEqual(LiteralExpression<?> left, LiteralExpression<?> right) {
        List<Float> leftValues = buildFloatList((List<?>) left.getValue());
        List<Float> rightValues = buildFloatList((List<?>) right.getValue());
        return !leftValues.equals(rightValues);

    }


    @OperationMapping(operations = {
            In.class
    })
    public boolean in(LiteralExpression<?> left, LiteralExpression<?> right) {
        List<Float> leftValues = buildFloatList((List<?>) left.getValue());
        List<Float> rightValues = buildFloatList((List<?>) right.getValue());
        return rightValues.containsAll(leftValues);
    }

    @OperationMapping(operations = {
            NotIn.class
    })
    public boolean notIn(LiteralExpression<?> left, LiteralExpression<?> right) {
        List<Float> leftValues = buildFloatList((List<?>) left.getValue());
        List<Float> rightValues = buildFloatList((List<?>) right.getValue());
        return !rightValues.containsAll(leftValues);
    }


    private List<Float> buildFloatList(List<?> tempValues) {
        List<Float> values = new ArrayList<>();
        for (Object tempValue : tempValues) {

            if (tempValue instanceof Float) {
                values.add((Float) tempValue);
            } else {
                try {
                    Float value = Float.parseFloat(tempValue.toString());
                    values.add(value);
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
