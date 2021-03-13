package org.dvare.expression.datatype;


import org.apache.commons.lang3.tuple.Pair;
import org.dvare.annotations.OperationMapping;
import org.dvare.annotations.Type;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.operation.relational.Equals;
import org.dvare.expression.operation.relational.NotEquals;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
@Type(dataType = DataType.PairType)
public class PairType extends DataTypeExpression {
    public PairType() {
        super(DataType.PairType);

    }

    @OperationMapping(operations = {
            Equals.class
    })
    public boolean equal(LiteralExpression<?> left, LiteralExpression<?> right) {
        Pair<?, ?> leftValue = (Pair<?, ?>) left.getValue();
        Pair<?, ?> rightValue = (Pair<?, ?>) right.getValue();
        return leftValue.equals(rightValue);
    }

    @OperationMapping(operations = {
            NotEquals.class
    })
    public boolean notEqual(LiteralExpression<?> left, LiteralExpression<?> right) {
        Pair<?, ?> leftValue = (Pair<?, ?>) left.getValue();
        Pair<?, ?> rightValue = (Pair<?, ?>) right.getValue();
        return !leftValue.equals(rightValue);
    }


}
