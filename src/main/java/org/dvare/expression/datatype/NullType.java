package org.dvare.expression.datatype;


import org.dvare.annotations.OperationMapping;
import org.dvare.annotations.Type;
import org.dvare.expression.ExpressionVisitor;
import org.dvare.expression.literal.ListLiteral;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.NullLiteral;
import org.dvare.expression.operation.arithmetic.Add;
import org.dvare.expression.operation.arithmetic.Subtract;
import org.dvare.expression.operation.relational.*;

import java.util.List;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
@Type(dataType = DataType.NullType)
public class NullType extends DataTypeExpression {
    public NullType() {
        super(DataType.NullType);
    }

    @OperationMapping(operations = {
            Equals.class
    })
    public boolean equal(LiteralExpression<?> left, LiteralExpression<?> right) {
        return left instanceof NullLiteral && right instanceof NullLiteral;
    }

    @OperationMapping(operations = {
            NotEquals.class
    })
    public boolean notEqual(LiteralExpression<?> left, LiteralExpression<?> right) {
        return !equal(left, right);
    }

    @OperationMapping(operations = {
            LessThan.class
    })
    public boolean less(LiteralExpression<?> left, LiteralExpression<?> right) {
        return false;
    }

    @OperationMapping(operations = {
            LessEqual.class
    })
    public boolean lessEqual(LiteralExpression<?> left, LiteralExpression<?> right) {
        return false;
    }

    @OperationMapping(operations = {
            GreaterThan.class
    })
    public boolean greater(LiteralExpression<?> left, LiteralExpression<?> right) {
        return false;
    }

    @OperationMapping(operations = {
            GreaterEqual.class
    })
    public boolean greaterEqual(LiteralExpression<?> left, LiteralExpression<?> right) {
        return false;
    }

    @OperationMapping(operations = {
            Between.class
    })
    public boolean between(LiteralExpression<?> left, LiteralExpression<?> right) {
        return false;
    }

    @OperationMapping(operations = {
            In.class
    })
    public boolean in(LiteralExpression<?> left, LiteralExpression<?> right) {
        if (right instanceof ListLiteral) {
            List<Object> tempValues = (List<Object>) right.getValue();
            return tempValues.contains(null);

        }
        return false;
    }


    @OperationMapping(operations = {
            NotIn.class
    })
    public boolean notIn(LiteralExpression<?> left, LiteralExpression<?> right) {
        return !in(left, right);
    }


    @OperationMapping(operations = {
            org.dvare.expression.operation.aggregation.Sum.class,
            Add.class
    })
    public Object sum(LiteralExpression<?> left, LiteralExpression<?> right) {
        return null;
    }

    @OperationMapping(operations = {
            Subtract.class
    })
    public Object sub(LiteralExpression<?> left, LiteralExpression<?> right) {
        return null;
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> v) {
        return v.visit(this);
    }
}
