package org.dvare.expression.datatype;

import org.dvare.annotations.OperationMapping;
import org.dvare.annotations.Type;
import org.dvare.expression.ExpressionVisitor;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.operation.relational.Equals;
import org.dvare.expression.operation.relational.NotEquals;
import org.dvare.util.Triple;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
@Type(dataType = DataType.TripleType)
public class TripleType extends DataTypeExpression {
    public TripleType() {
        super(DataType.TripleType);

    }

    @OperationMapping(operations = {
            Equals.class
    })
    public boolean equal(LiteralExpression<?> left, LiteralExpression<?> right) {
        Triple<?, ?, ?> leftValue = (Triple<?, ?, ?>) left.getValue();
        Triple<?, ?, ?> rightValue = (Triple<?, ?, ?>) right.getValue();
        return leftValue.equals(rightValue);
    }

    @OperationMapping(operations = {
            NotEquals.class
    })
    public boolean notEqual(LiteralExpression<?> left, LiteralExpression<?> right) {
        Triple<?, ?, ?> leftValue = (Triple<?, ?, ?>) left.getValue();
        Triple<?, ?, ?> rightValue = (Triple<?, ?, ?>) right.getValue();
        return !leftValue.equals(rightValue);
    }


    @Override
    public void accept(ExpressionVisitor v) {
        super.accept(v);
        v.visit(this);
    }
}
