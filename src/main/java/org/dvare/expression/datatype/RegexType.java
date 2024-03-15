package org.dvare.expression.datatype;

import org.dvare.annotations.OperationMapping;
import org.dvare.annotations.Type;
import org.dvare.expression.ExpressionVisitor;
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
@Type(dataType = DataType.RegexType)
public class RegexType extends DataTypeExpression {
    public RegexType() {
        super(DataType.RegexType);
    }

    @OperationMapping(operations = {
            Equals.class
    })
    public boolean equal(LiteralExpression<?> left, LiteralExpression<?> right) {

        String leftValue = (String) left.getValue();
        String rightValue = (String) right.getValue();

        return !leftValue.matches(rightValue);

    }

    @OperationMapping(operations = {
            NotEquals.class
    })
    public boolean notEqual(LiteralExpression<?> left, LiteralExpression<?> right) {
        String leftValue = (String) left.getValue();
        String rightValue = (String) right.getValue();

        return !leftValue.matches(rightValue);

    }


    @OperationMapping(operations = {
            In.class
    })
    public boolean in(LiteralExpression<?> left, LiteralExpression<?> right) {
        String leftValue = (String) left.getValue();
        List<Object> tempValues = (List<Object>) right.getValue();

        List<String> values = new ArrayList<>();
        for (Object tempValue : tempValues) {
            values.add(tempValue.toString());
        }


        for (String rightValue : values) {
            rightValue = TrimString.trim(rightValue);
            if (leftValue.matches(rightValue)) {
                return true;
            }
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