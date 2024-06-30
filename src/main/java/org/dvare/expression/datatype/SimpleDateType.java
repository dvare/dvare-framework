package org.dvare.expression.datatype;

import org.dvare.annotations.OperationMapping;
import org.dvare.annotations.Type;
import org.dvare.expression.ExpressionVisitor;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.operation.arithmetic.Subtract;
import org.dvare.expression.operation.relational.*;

import java.util.Date;
import java.util.List;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
@Type(dataType = DataType.SimpleDateType)
public class SimpleDateType extends DataTypeExpression {


    public SimpleDateType() {
        super(DataType.SimpleDateType);

    }

    @OperationMapping(operations = {
            Equals.class
    })
    public boolean equal(LiteralExpression<?> left, LiteralExpression<?> right) {
        Date leftValue = toDate(left.getValue());
        Date rightValue = toDate(right.getValue());
        return (leftValue != null && rightValue != null) && leftValue.compareTo(rightValue) == 0;
    }

    @OperationMapping(operations = {
            NotEquals.class
    })
    public boolean notEqual(LiteralExpression<?> left, LiteralExpression<?> right) {
        Date leftValue = toDate(left.getValue());
        Date rightValue = toDate(right.getValue());
        return (leftValue != null && rightValue != null) && leftValue.compareTo(rightValue) != 0;
    }

    @OperationMapping(operations = {
            LessThan.class
    })
    public boolean less(LiteralExpression<?> left, LiteralExpression<?> right) {
        Date leftValue = toDate(left.getValue());
        Date rightValue = toDate(right.getValue());
        return (leftValue != null && rightValue != null) && leftValue.compareTo(rightValue) < 0;
    }

    @OperationMapping(operations = {
            LessEqual.class
    })

    public boolean lessEqual(LiteralExpression<?> left, LiteralExpression<?> right) {
        Date leftValue = toDate(left.getValue());
        Date rightValue = toDate(right.getValue());
        return (leftValue != null && rightValue != null) && leftValue.compareTo(rightValue) <= 0;
    }

    @OperationMapping(operations = {
            GreaterThan.class
    })
    public boolean greater(LiteralExpression<?> left, LiteralExpression<?> right) {
        Date leftValue = toDate(left.getValue());
        Date rightValue = toDate(right.getValue());
        return (leftValue != null && rightValue != null) && leftValue.compareTo(rightValue) > 0;
    }

    @OperationMapping(operations = {
            GreaterEqual.class
    })
    public boolean greaterEqual(LiteralExpression<?> left, LiteralExpression<?> right) {
        Date leftValue = toDate(left.getValue());
        Date rightValue = toDate(right.getValue());
        return (leftValue != null && rightValue != null) && leftValue.compareTo(rightValue) >= 0;
    }

    @OperationMapping(operations = {
            In.class
    })
    public boolean in(LiteralExpression<?> left, LiteralExpression<?> right) {
        Date leftValue = toDate(left.getValue());
        List<Date> rightValues = buildDateList((List<?>) right.getValue());
        return leftValue != null && rightValues.contains(leftValue);
    }

    @OperationMapping(operations = {
            NotIn.class
    })
    public boolean notIn(LiteralExpression<?> left, LiteralExpression<?> right) {
        Date leftValue = toDate(left.getValue());
        List<Date> rightValues = buildDateList((List<?>) right.getValue());
        return leftValue != null && !rightValues.contains(leftValue);
    }

    @OperationMapping(operations = {
            Between.class
    })
    public boolean between(LiteralExpression<?> left, LiteralExpression<?> right) {
        Date leftValue = toDate(left.getValue());
        List<Date> values = buildDateList((List<?>) right.getValue());
        Date lower = values.get(0);
        Date upper = values.get(1);

        return leftValue != null && lower.compareTo(leftValue) <= 0 && leftValue.compareTo(upper) <= 0;
    }


    @OperationMapping(operations = {
            Subtract.class
    })
    public Date sub(LiteralExpression<?> left, LiteralExpression<?> right) {
        Date leftValue = toDate(left.getValue());
        Date rightValue = toDate(right.getValue());
        return new Date(rightValue.getTime() - leftValue.getTime());
    }


    @Override
    public <T> T accept(ExpressionVisitor<T> v) {
        return v.visit(this);
    }
}