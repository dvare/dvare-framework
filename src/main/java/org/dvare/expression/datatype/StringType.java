package org.dvare.expression.datatype;


import org.dvare.annotations.OperationMapping;
import org.dvare.annotations.Type;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.operation.relational.*;
import org.dvare.util.TrimString;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
@Type(dataType = DataType.StringType)
public class StringType extends DataTypeExpression {
    public StringType() {
        super(DataType.StringType);
    }

    @OperationMapping(operations = {
            Equals.class
    })
    public boolean equal(LiteralExpression left, LiteralExpression right) {
        String leftValue = left.getValue().toString();
        String rightValue = right.getValue().toString();

        leftValue = TrimString.trim(leftValue);
        rightValue = TrimString.trim(rightValue);

        if (right.getType().equals(StringType.class)) {
            return leftValue.equals(rightValue);
        } else if (right.getType().equals(RegexType.class)) {
            return leftValue.matches(rightValue);
        }

        return false;

    }

    @OperationMapping(operations = {
            NotEquals.class
    })
    public boolean notEqual(LiteralExpression left, LiteralExpression right) {
        String leftValue = left.getValue().toString();
        String rightValue = right.getValue().toString();

        leftValue = TrimString.trim(leftValue);
        rightValue = TrimString.trim(rightValue);

        if (right.getType().equals(StringType.class)) {
            return !leftValue.equals(rightValue);
        }
        return right.getType().equals(RegexType.class) && !leftValue.matches(rightValue);

    }

    @OperationMapping(operations = {
            LessThan.class
    })
    public boolean less(LiteralExpression left, LiteralExpression right) {
        String leftValue = left.getValue().toString();
        String rightValue = right.getValue().toString();
        return leftValue.length() < rightValue.length();
    }

    @OperationMapping(operations = {
            LessEqual.class
    })
    public boolean lessEqual(LiteralExpression left, LiteralExpression right) {
        String leftValue = left.getValue().toString();
        String rightValue = right.getValue().toString();
        return leftValue.length() <= rightValue.length();
    }

    @OperationMapping(operations = {
            GreaterThan.class
    })
    public boolean greater(LiteralExpression left, LiteralExpression right) {
        String leftValue = left.getValue().toString();
        String rightValue = right.getValue().toString();
        return leftValue.length() > rightValue.length();
    }

    @OperationMapping(operations = {
            GreaterEqual.class
    })
    public boolean greaterEqual(LiteralExpression left, LiteralExpression right) {
        String leftValue = (String) left.getValue();
        String rightValue = (String) right.getValue();
        return leftValue.length() >= rightValue.length();
    }

    @OperationMapping(operations = {
            In.class
    })
    public boolean in(LiteralExpression left, LiteralExpression right) {

        String leftValue = left.getValue().toString();

        List<?> tempValues = (List<?>) right.getValue();
        List<String> values = new ArrayList<>();
        for (Object tempValue : tempValues) {
            if (tempValue == null) {
                values.add(null);
            } else {
                values.add(tempValue.toString());
            }

        }

        leftValue = TrimString.trim(leftValue);

        for (String rightValue : values) {
            rightValue = TrimString.trim(rightValue);

            if (right.getType().equals(StringType.class) && leftValue.equals(rightValue)) {
                return true;
            } else if (right.getType().equals(RegexType.class) && leftValue.matches(rightValue)) {

                return true;

            }
        }
        return false;
    }


    @OperationMapping(operations = {
            NotIn.class
    })
    public boolean notIn(LiteralExpression left, LiteralExpression right) {
        return !in(left, right);
    }

}
