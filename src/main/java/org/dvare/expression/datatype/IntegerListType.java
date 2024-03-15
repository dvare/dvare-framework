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
    public boolean equal(LiteralExpression<?> left, LiteralExpression<?> right) {
        List<Integer> leftValues = buildIntegerList((List<?>) left.getValue());
        List<Integer> rightValues = buildIntegerList((List<?>) right.getValue());
        return leftValues.equals(rightValues);
    }


    @OperationMapping(operations = {
            NotEquals.class
    })
    public boolean notEqual(LiteralExpression<?> left, LiteralExpression<?> right) {
        List<Integer> leftValues = buildIntegerList((List<?>) left.getValue());
        List<Integer> rightValues = buildIntegerList((List<?>) right.getValue());
        return !leftValues.equals(rightValues);

    }


    @OperationMapping(operations = {
            In.class
    })
    public boolean in(LiteralExpression<?> left, LiteralExpression<?> right) {
        List<Integer> leftValues = buildIntegerList((List<?>) left.getValue());
        List<Integer> rightValues = buildIntegerList((List<?>) right.getValue());
        return rightValues.containsAll(leftValues);
    }

    @OperationMapping(operations = {
            NotIn.class
    })
    public boolean notIn(LiteralExpression<?> left, LiteralExpression<?> right) {
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
