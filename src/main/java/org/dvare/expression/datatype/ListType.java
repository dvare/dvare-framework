package org.dvare.expression.datatype;

import org.dvare.annotations.OperationMapping;
import org.dvare.annotations.Type;
import org.dvare.expression.ExpressionVisitor;
import org.dvare.expression.literal.ListLiteral;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.operation.relational.Equals;
import org.dvare.expression.operation.relational.In;
import org.dvare.expression.operation.relational.NotEquals;
import org.dvare.expression.operation.relational.NotIn;

import java.util.List;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
@Type(dataType = DataType.ListType)
public class ListType extends DataTypeExpression {
    public ListType() {
        super(DataType.ListType);
    }


    public ListType(DataType dataType) {
        super(dataType);
    }

    @OperationMapping(operations = {
            Equals.class
    })
    public boolean equal(LiteralExpression<?> left, LiteralExpression<?> right) {
        if (left instanceof ListLiteral && right instanceof ListLiteral) {
            List<?> leftValues = ((ListLiteral) left).getValue();
            List<?> rightValues = ((ListLiteral) right).getValue();
            return leftValues.equals(rightValues);

        }
        return false;
    }

    @OperationMapping(operations = {
            NotEquals.class
    })
    public boolean notEqual(LiteralExpression<?> left, LiteralExpression<?> right) {
        return !equal(left, right);
    }


    @OperationMapping(operations = {
            In.class
    })
    public boolean in(LiteralExpression<?> left, LiteralExpression<?> right) {
        if (left instanceof ListLiteral && right instanceof ListLiteral) {
            List<?> leftValues = ((ListLiteral) left).getValue();
            List<?> rightValues = ((ListLiteral) right).getValue();
            return rightValues.containsAll(leftValues);

        }
        return false;
    }


    @OperationMapping(operations = {
            NotIn.class
    })
    public boolean notIn(LiteralExpression<?> left, LiteralExpression<?> right) {
        return !in(left, right);
    }

    @Override
    public void accept(ExpressionVisitor v) {
        super.accept(v);
        v.visit(this);
    }
}

