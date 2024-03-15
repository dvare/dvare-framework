package org.dvare.expression.datatype;


import org.dvare.annotations.OperationMapping;
import org.dvare.annotations.Type;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.operation.relational.Equals;
import org.dvare.expression.operation.relational.In;
import org.dvare.expression.operation.relational.NotEquals;
import org.dvare.expression.operation.relational.NotIn;
import org.dvare.util.TrimString;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
@Type(dataType = DataType.StringListType)
public class StringListType extends ListType {
    public StringListType() {

        super(DataType.StringListType);
    }

    @OperationMapping(operations = {
            Equals.class
    })
    public boolean equal(LiteralExpression<?> left, LiteralExpression<?> right) {
        List<String> leftValues = buildStringList((List<?>) left.getValue());
        List<String> rightValues = buildStringList((List<?>) right.getValue());
        return leftValues.equals(rightValues);
    }


    @OperationMapping(operations = {
            NotEquals.class
    })
    public boolean notEqual(LiteralExpression<?> left, LiteralExpression<?> right) {
        List<String> leftValues = buildStringList((List<?>) left.getValue());
        List<String> rightValues = buildStringList((List<?>) right.getValue());
        return !leftValues.equals(rightValues);

    }


    @OperationMapping(operations = {
            In.class
    })
    public boolean in(LiteralExpression<?> left, LiteralExpression<?> right) {
        List<String> leftValues = buildStringList((List<?>) left.getValue());
        List<String> rightValues = buildStringList((List<?>) right.getValue());
        return rightValues.containsAll(leftValues);
    }

    @OperationMapping(operations = {
            NotIn.class
    })
    public boolean notIn(LiteralExpression<?> left, LiteralExpression<?> right) {
        List<String> leftValues = buildStringList((List<?>) left.getValue());
        List<String> rightValues = buildStringList((List<?>) right.getValue());
        return !rightValues.containsAll(leftValues);
    }


    private List<String> buildStringList(List<?> tempValues) {
        List<String> values = new ArrayList<>();
        for (Object tempValue : tempValues) {
            String stringValue = tempValue.toString();
            values.add(TrimString.trim(stringValue));
        }
        return values;
    }
}
