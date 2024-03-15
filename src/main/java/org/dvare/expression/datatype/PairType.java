package org.dvare.expression.datatype;


import org.dvare.annotations.OperationMapping;
import org.dvare.annotations.Type;
import org.dvare.expression.ExpressionVisitor;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.operation.relational.Equals;
import org.dvare.expression.operation.relational.NotEquals;
import org.dvare.util.Pair;

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


    @Override
    public <T> T accept(ExpressionVisitor<T> v) {
        return v.visit(this);
    }
}
